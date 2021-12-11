package com.github.datacacher.processor;

import com.github.datacacher.cache.CacheManager;
import com.github.datacacher.exceptions.CacheException;
import com.github.datacacher.exceptions.ListException;
import com.github.datacacher.model.CacheRequest;
import com.github.datacacher.model.CacheResponse;
import com.github.datacacher.model.ListRequest;
import com.github.datacacher.model.ListResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.github.datacacher.constants.CacheConstants.CACHENAME;
import static com.github.datacacher.constants.ListConstants.*;

@Component("listProcessor")
public class ListProcessor implements Processor {
    @Autowired
    CacheManager cacheManager;

    public void buildCacheRequest(Exchange exchange) throws Exception {
        ListRequest request = (ListRequest) exchange.getProperty(LISTREQUEST);
        exchange.getIn().setHeader(CACHENAME, request.getCacheName());
        exchange.setProperty(CACHENAME, request.getCacheName());
    }
    public void buildCacheAndListRequest(Exchange exchange) throws Exception {
        String cacheName = (String) exchange.getIn().getHeader(CACHENAME);
        String listName = (String) exchange.getIn().getHeader(LISTNAME);
        exchange.getIn().setHeader(CACHENAME, cacheName);
        exchange.setProperty(CACHENAME, cacheName);
        ListRequest request = new ListRequest();
        request.setCacheName(cacheName);
        request.setListName(listName);
        exchange.setProperty(LISTREQUEST, request);
    }
    @Override
    public void process(Exchange exchange) throws Exception {
        ListRequest request = (ListRequest) exchange.getProperty(LISTREQUEST);
        ConcurrentMap<String, ConcurrentMap<String, List>> map = (ConcurrentMap<String, ConcurrentMap<String, List>>) exchange.getProperty("CachePayload");
        ConcurrentMap<String, List> lists = (ConcurrentMap<String, List>) map.get(LIST);
        if (Objects.isNull(lists)) {
            lists = new ConcurrentHashMap<>();
            map.put(LIST, lists);
        }
        if(lists.containsKey(request.getListName())) {
            throw new ListException(LISTAVAILABLE);
        }
        lists.put(request.getListName(), new ArrayList());
        if(!Objects.isNull(request.getPayload())){
            lists.get(request.getListName()).addAll(request.getPayload());
        }
        exchange.setProperty("ListStatusMessage", "List created successfully");
    }
    public void processResponse(Exchange exchange) throws Exception {
        ListRequest request = (ListRequest) exchange.getProperty(LISTREQUEST);
        ListResponse response = new ListResponse();
        response.setListName(request.getListName());
        response.setHttpErrorMessage("");
        response.setHttpSuccessMessage(exchange.getProperty("ListStatusMessage", String.class));
        response.setPayload((List<Object>) exchange.getProperty("ListPayload"));
        response.setHttpStatus("success");
        exchange.getIn().setBody(response);
    }
    public synchronized void addToList(Exchange exchange) throws Exception {
        ConcurrentMap<String, ConcurrentMap<String, List>> map = (ConcurrentMap<String, ConcurrentMap<String, List>>) exchange.getProperty("CachePayload");
        ListRequest request = (ListRequest) exchange.getProperty(LISTREQUEST);
        if(Objects.isNull(request.getPayload())||request.getPayload().isEmpty()){
            throw new ListException(EMPTYLISTCANNOTEADDED);
        }
        ConcurrentMap<String, List> lists = (ConcurrentMap<String, List>) map.get(LIST);
        if (Objects.isNull(lists)||
                !lists.containsKey(request.getListName())) {
            throw new ListException(LISTTOBECREATED);
        }
        lists.get(request.getListName()).addAll(request.getPayload());
        exchange.setProperty("ListStatusMessage", "List added successfully");
    }
    public synchronized void updateList(Exchange exchange) throws Exception {
        ConcurrentMap<String, ConcurrentMap<String, List>> map = (ConcurrentMap<String, ConcurrentMap<String, List>>) exchange.getProperty("CachePayload");
        ListRequest request = (ListRequest) exchange.getProperty(LISTREQUEST);
        if(Objects.isNull(request.getPayload())){
            throw new ListException(NULLLIST);
        }
        ConcurrentMap<String, List> lists = (ConcurrentMap<String, List>) map.get(LIST);
        if (Objects.isNull(lists)||
                !lists.containsKey(request.getListName())) {
            throw new ListException(LISTTOBECREATED);
        }
        lists.put(request.getListName(), request.getPayload());
        exchange.setProperty("ListStatusMessage", "List updated successfully");
    }
    public synchronized void removeList(Exchange exchange) throws Exception {
        ConcurrentMap<String, ConcurrentMap<String, List>> map = (ConcurrentMap<String, ConcurrentMap<String, List>>) exchange.getProperty("CachePayload");
        ListRequest request = (ListRequest) exchange.getProperty(LISTREQUEST);
        ConcurrentMap<String, List> lists = (ConcurrentMap<String, List>) map.get(LIST);
        if (Objects.isNull(lists)||
                !lists.containsKey(request.getListName())) {
            throw new ListException(LISTNOTAVAILABLEFORDELETION);
        }
        lists.remove(request.getListName());
        exchange.setProperty("ListStatusMessage", "List removed successfully");
    }

    public synchronized void removeListValues(Exchange exchange) throws Exception {
        ConcurrentMap<String, ConcurrentMap<String, List>> map = (ConcurrentMap<String, ConcurrentMap<String, List>>) exchange.getProperty("CachePayload");
        ListRequest request = (ListRequest) exchange.getProperty(LISTREQUEST);
        ConcurrentMap<String, List> lists = (ConcurrentMap<String, List>) map.get(LIST);
        if (Objects.isNull(lists)||
                !lists.containsKey(request.getListName())) {
            throw new ListException(LISTNOTAVAILABLEFORVALUESDELETION);
        }
        if(Objects.isNull(request.getPayload()) || request.getPayload().isEmpty()){
            throw new ListException(EMPTYLISTCANNOTEREMOVED);
        }
        lists.get(request.getListName()).removeAll(request.getPayload());
        exchange.setProperty("ListStatusMessage", "List values removed successfully");
    }

    public void processErrorResponse(Exchange exchange) throws Exception {
        ListRequest request = (ListRequest) exchange.getProperty(LISTREQUEST);
        ListException listException = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, ListException.class);
        ListResponse response = new ListResponse();
        response.setListName(request.getListName());
        response.setHttpErrorMessage(listException.getMessage());
        response.setHttpStatus("failed");
        exchange.getIn().setBody(response);
    }

    public synchronized void getList(Exchange exchange) throws Exception {
        ConcurrentMap<String, ConcurrentMap<String, List>> map = (ConcurrentMap<String, ConcurrentMap<String, List>>) exchange.getProperty("CachePayload");
        ListRequest request = (ListRequest) exchange.getProperty(LISTREQUEST);
        ConcurrentMap<String, List> lists = (ConcurrentMap<String, List>) map.get(LIST);
        if (Objects.isNull(lists)||
                !lists.containsKey(request.getListName())) {
            throw new ListException(LISTTOBECREATED);
        }
        exchange.setProperty("ListPayload",lists.get(request.getListName()));
        exchange.setProperty("ListStatusMessage", "List obtained successfully");
    }
}
