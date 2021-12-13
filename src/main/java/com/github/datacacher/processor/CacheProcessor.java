package com.github.datacacher.processor;

import com.github.datacacher.cache.Cache;
import com.github.datacacher.cache.CacheManager;
import com.github.datacacher.exceptions.CacheException;
import com.github.datacacher.exceptions.CacheManagerException;
import com.github.datacacher.model.CacheAuditResponse;
import com.github.datacacher.model.CacheRequest;
import com.github.datacacher.model.CacheResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.github.datacacher.constants.CacheConstants.*;

@Component("cacheProcessor")
public class CacheProcessor implements Processor {

    @Autowired
    CacheManager cacheManager;

    @Override
    public void process(Exchange exchange) throws Exception {
        CacheRequest cacheRequest = exchange.getIn().getBody(CacheRequest.class);
        boolean cacheCreateResponse = cacheManager.createCache(cacheRequest.cacheName, cacheRequest.expiry);
        if (cacheCreateResponse) {
            exchange.setProperty("CacheStatusMessage", CACHECREATEDSUCCESSFULLY);
        }
    }

    public void processErrorResponse(Exchange exchange) throws Exception {
        CacheException cacheException = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, CacheException.class);
        CacheResponse response = new CacheResponse();
        response.setHttpErrorMessage(cacheException.getMessage());
        response.setHttpStatus("failed");
        exchange.getIn().setBody(response);
    }

    public void processResponse(Exchange exchange) throws Exception {
        CacheResponse response = new CacheResponse();
        response.setHttpErrorMessage("");
        response.setHttpSuccessMessage(exchange.getProperty("CacheStatusMessage", String.class));
        response.setPayload(exchange.getProperty("CachePayload"));
        response.setHttpStatus("success");
        exchange.getIn().setBody(response);
    }

    public void removeCache(Exchange exchange) throws Exception {
        String cacheName = exchange.getIn().getHeader(CACHENAME, String.class);
        boolean cacheRemoveResponse = cacheManager.removeCache(cacheName);
        if (cacheRemoveResponse) {
            exchange.setProperty("CacheStatusMessage", CACHEREMOVEDSUCCESSFULLY);
        }
    }

    public void getCache(Exchange exchange) throws Exception {
        String cacheName = exchange.getIn().getHeader(CACHENAME, String.class);
        Cache cache = cacheManager.getCache(cacheName);
        if (!Objects.isNull(cache)) {
            exchange.setProperty("CacheStatusMessage", CACHEOBTAINEDSUCCESSFULLY);
            exchange.setProperty("CachePayload", cache.getCacheCollectionEntries());
        } else {
            throw new CacheException(CACHENOTAVAILABLE);
        }
    }

    public void getAuditCache(Exchange exchange) throws Exception {
        String cacheName = exchange.getIn().getHeader(CACHENAME, String.class);
        Cache cache = cacheManager.getCache(cacheName);
        if (!Objects.isNull(cache)) {
            exchange.setProperty("CacheStatusMessage", AUDITOBTAINEDSUCCESSFULLY);
            exchange.setProperty("CachePayload", cache.getAudit());
        } else {
            throw new CacheException(CACHENOTAVAILABLE);
        }
    }

    public void getCacheAvailability(Exchange exchange) throws Exception {
        String cacheName = exchange.getIn().getHeader(CACHENAME, String.class);
        try {
            Cache cache = cacheManager.getCache(cacheName);
            exchange.setProperty("CacheStatusMessage", CACHEISAVAILABLE);
            exchange.setProperty("CachePayload", true);
        } catch (Exception ex) {
            exchange.setProperty("CacheStatusMessage", CACHEISUNAVAILABLE);
            exchange.setProperty("CachePayload", false);
        }
    }

    public void auditCache(Exchange exchange) throws Exception {
        String cacheName = exchange.getProperty(CACHENAME, String.class);
        Cache cache = cacheManager.getCache(cacheName);
        if (Objects.isNull(cache)) {
            throw new CacheException(CACHENOTAVAILABLE);
        }
        cache.getAudit().add((String) exchange.getProperty(AUDIT));
    }

    public void processAuditResponse(Exchange exchange) throws Exception {
        CacheAuditResponse response = new CacheAuditResponse();
        response.setHttpErrorMessage("");
        response.setHttpSuccessMessage(exchange.getProperty("CacheStatusMessage", String.class));
        response.setAudit(exchange.getProperty("CachePayload"));
        response.setHttpStatus("success");
        exchange.getIn().setBody(response);
    }
}
