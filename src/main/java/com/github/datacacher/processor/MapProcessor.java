package com.github.datacacher.processor;

import com.github.datacacher.exceptions.MapException;
import com.github.datacacher.model.MapRequest;
import com.github.datacacher.model.MapResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.github.datacacher.constants.CacheConstants.CACHENAME;
import static com.github.datacacher.constants.MapConstants.*;

@Component("mapProcessor")
@Profile("cacherApi")
public class MapProcessor implements Processor {

    public void buildCacheRequest(Exchange exchange) throws Exception {
        MapRequest request = (MapRequest) exchange.getProperty(MAPREQUEST);
        exchange.getIn().setHeader(CACHENAME, request.getCacheName());
        exchange.setProperty(CACHENAME, request.getCacheName());
    }
    public void buildCacheAndMapRequest(Exchange exchange) throws Exception {
        String cacheName = (String) exchange.getIn().getHeader(CACHENAME);
        String mapName = (String) exchange.getIn().getHeader(MAPNAME);
        exchange.getIn().setHeader(CACHENAME, cacheName);
        exchange.setProperty(CACHENAME, cacheName);
        MapRequest request = new MapRequest();
        request.setCacheName(cacheName);
        request.setMapName(mapName);
        exchange.setProperty(MAPREQUEST, request);
    }
    @Override
    public void process(Exchange exchange) throws Exception {
        MapRequest request = (MapRequest) exchange.getProperty(MAPREQUEST);
        ConcurrentMap<String, ConcurrentMap<String, Map<String,Object>>> collections =
                (ConcurrentMap<String, ConcurrentMap<String, Map<String,Object>>>) exchange.getProperty("CachePayload");
        ConcurrentMap<String, Map<String,Object>> maps = (ConcurrentMap<String, Map<String,Object>>) collections.get(MAP);
        if (Objects.isNull(maps)) {
            maps = new ConcurrentHashMap<>();
            collections.put(MAP, maps);
        }
        if(maps.containsKey(request.getMapName())) {
            throw new MapException(MAPAVAILABLE);
        }
        maps.put(request.getMapName(), new ConcurrentHashMap<>());
        if(!Objects.isNull(request.getKey()) && !Objects.isNull(request.getValues())){
            maps.get(request.getMapName()).put(request.getKey(), request.getValues());
        }
        exchange.setProperty("MapStatusMessage", "Map created successfully");
    }

    public void processResponse(Exchange exchange) throws Exception {
        MapRequest request = (MapRequest) exchange.getProperty(MAPREQUEST);
        MapResponse response = new MapResponse();
        response.setMapName(request.getMapName());
        response.setHttpErrorMessage("");
        response.setHttpSuccessMessage(exchange.getProperty("MapStatusMessage", String.class));
        response.setPayload((Map<String, Object>) exchange.getProperty("MapPayload"));
        response.setHttpStatus("success");
        exchange.getIn().setBody(response);
    }

    public void processErrorResponse(Exchange exchange) throws Exception {
        MapRequest request = (MapRequest) exchange.getProperty(MAPREQUEST);
        MapException MapException = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, MapException.class);
        MapResponse response = new MapResponse();
        response.setMapName(request.getMapName());
        response.setHttpErrorMessage(MapException.getMessage());
        response.setHttpStatus("failed");
        exchange.getIn().setBody(response);
    }

    public synchronized void getMap(Exchange exchange) throws Exception {
        Map<String, Map<String, Map<String,Object>>> map
                = (Map<String, Map<String, Map<String,Object>>>) exchange.getProperty("CachePayload");
        MapRequest request = (MapRequest) exchange.getProperty(MAPREQUEST);
        Map<String, Map<String,Object>> maps = map.get(MAP);
        if (Objects.isNull(maps)||
                !maps.containsKey(request.getMapName())) {
            throw new MapException(MAPTOBECREATED);
        }
        exchange.setProperty("MapPayload",maps.get(request.getMapName()));
        exchange.setProperty("MapStatusMessage", "Map obtained successfully");
    }

    public synchronized void putMap(Exchange exchange) throws Exception {
        ConcurrentMap<String, ConcurrentMap<String, Map<String,Object>>> cache = (ConcurrentMap<String, ConcurrentMap<String, Map<String,Object>>>) exchange.getProperty("CachePayload");
        MapRequest request = (MapRequest) exchange.getProperty(MAPREQUEST);
        ConcurrentMap<String, Map<String,Object>> maps = cache.get(MAP);
        if (Objects.isNull(maps)||
                !maps.containsKey(request.getMapName())) {
            throw new MapException(NULLMAP);
        }
        if(Objects.isNull(request.getKey())){
            throw new MapException(NULLKEYNOTALLOWABLE);
        }
        maps.get(request.getMapName()).put(request.getKey(), request.getValues());
        exchange.setProperty("MapStatusMessage", "Map updated successfully");
    }

    public synchronized void removeMap(Exchange exchange) throws Exception {
        ConcurrentMap<String, ConcurrentMap<String, Map<String,Object>>> cache = (ConcurrentMap<String, ConcurrentMap<String, Map<String,Object>>>) exchange.getProperty("CachePayload");
        MapRequest request = (MapRequest) exchange.getProperty(MAPREQUEST);
        ConcurrentMap<String, Map<String,Object>> maps = cache.get(MAP);
        if (Objects.isNull(maps)||
                !maps.containsKey(request.getMapName())) {
            throw new MapException(NOMAPAVAILABLE);
        }
        maps.remove(request.getMapName());
        exchange.setProperty("MapStatusMessage", "Map removed successfully");
    }

    public synchronized void removeKeyInMap(Exchange exchange) throws Exception {
        ConcurrentMap<String, ConcurrentMap<String, Map<String,Object>>> cache = (ConcurrentMap<String, ConcurrentMap<String, Map<String,Object>>>) exchange.getProperty("CachePayload");
        MapRequest request = (MapRequest) exchange.getProperty(MAPREQUEST);
        ConcurrentMap<String, Map<String,Object>> maps = cache.get(MAP);
        if (Objects.isNull(maps)||
                !maps.containsKey(request.getMapName())) {
            throw new MapException(NOMAPAVAILABLE);
        }
        if(Objects.isNull(request.getKey())){
            throw new MapException(NULLKEYNOTALLOWABLE);
        }
        Map<String,Object> map = maps.get(request.getMapName());
        if(!map.containsKey(request.getKey())){
            throw new MapException(NOKEYAVAILABLE);
        }
        map.remove(request.getKey());
        exchange.setProperty("MapStatusMessage", "Value for key "+request.getKey()+" in map "+request.getMapName()+" Removed Successfully");
    }
}
