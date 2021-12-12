package com.github.datacacher.routes;

import com.github.datacacher.Cacher;
import com.github.datacacher.model.CacheRequest;
import com.github.datacacher.model.CacheResponse;
import com.github.datacacher.model.MapRequest;
import com.github.datacacher.model.MapResponse;
import org.apache.camel.Exchange;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;

import static com.github.datacacher.constants.CacheConstants.CACHECREATEDSUCCESSFULLY;
import static com.github.datacacher.constants.CacheConstants.CACHENAME;
import static com.github.datacacher.constants.MapConstants.MAPNAME;
import static com.github.datacacher.constants.RouteConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = {Cacher.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AMapRouteTest extends AnAbstractRouteTest {

    @Before
    public void initializeMapRoutes() throws Exception {
        AdviceWithRouteBuilder.adviceWith(camelContext,CREATE_MAP_ID,(builder)->{
            builder.weaveAddLast()
                    .to(resultEndpointMap);
        });
        AdviceWithRouteBuilder.adviceWith(camelContext,GET_MAP_ID,(builder)->{
            builder.weaveAddLast()
                    .to(resultEndpointMap);
        });
        AdviceWithRouteBuilder.adviceWith(camelContext,PUT_MAP_ID,(builder)->{
            builder.weaveAddLast()
                    .to(resultEndpointMap);
        });
        AdviceWithRouteBuilder.adviceWith(camelContext,REMOVE_MAP_ID,(builder)->{
            builder.weaveAddLast()
                    .to(resultEndpointMap);
        });
        AdviceWithRouteBuilder.adviceWith(camelContext,REMOVE_MAP_KEY_ID,(builder)->{
            builder.weaveAddLast()
                    .to(resultEndpointMap);
        });
        resultEndpointMap.reset();
    }
    
    
    @Test
    @DirtiesContext
    public void testMapCreate() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycache33");
        cacheRequest.setExpiry(0);
        resultEndpoint.expectedMessageCount(1);
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(cacheRequest);
        producer.send(CACHE_CREATE_ROUTE, exchange);
        resultEndpoint.assertIsSatisfied();
        List<Exchange> exchanges = resultEndpoint.getExchanges();
        exchange = exchanges.get(0);
        CacheResponse response = exchange.getIn().getBody(CacheResponse.class);
        assertEquals("success", response.getHttpStatus());
        assertEquals(CACHECREATEDSUCCESSFULLY, response.getHttpSuccessMessage());
        resultEndpoint.reset();
        exchanges.clear();
        MapRequest mapRequest = new MapRequest();
        mapRequest.setCacheName("mycache33");
        mapRequest.setMapName("mymap");
        resultEndpointMap.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(mapRequest);
        producer.send(MAP_CREATE_ROUTE, exchange);
        resultEndpointMap.assertIsSatisfied();
        exchanges = resultEndpointMap.getExchanges();
        exchange = exchanges.get(0);
        MapResponse mapResponse = exchange.getIn().getBody(MapResponse.class);
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map created successfully", mapResponse.getHttpSuccessMessage());
    }

    @Test
    @DirtiesContext
    public void testMapCreateUpdateGet() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycache29");
        cacheRequest.setExpiry(0);
        resultEndpoint.expectedMessageCount(1);
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(cacheRequest);
        producer.send(CACHE_CREATE_ROUTE, exchange);
        resultEndpoint.assertIsSatisfied();
        List<Exchange> exchanges = resultEndpoint.getExchanges();
        exchange = exchanges.get(0);
        CacheResponse response = exchange.getIn().getBody(CacheResponse.class);
        assertEquals("success", response.getHttpStatus());
        assertEquals(CACHECREATEDSUCCESSFULLY, response.getHttpSuccessMessage());
        resultEndpoint.reset();
        exchanges.clear();
        MapRequest mapRequest = new MapRequest();
        mapRequest.setCacheName("mycache29");
        mapRequest.setMapName("mymap");
        resultEndpointMap.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(mapRequest);
        producer.send(MAP_CREATE_ROUTE, exchange);
        resultEndpointMap.assertIsSatisfied();
        exchanges = resultEndpointMap.getExchanges();
        exchange = exchanges.get(0);
        MapResponse mapResponse = exchange.getIn().getBody(MapResponse.class);
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map created successfully", mapResponse.getHttpSuccessMessage());
        resultEndpointMap.reset();
        mapRequest = new MapRequest();
        mapRequest.setCacheName("mycache29");
        mapRequest.setMapName("mymap");
        mapRequest.setKeyName("key");
        mapRequest.setValues(Arrays.asList(1,2,3));
        resultEndpointMap.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(mapRequest);
        producer.send(MAP_PUT_ROUTE, exchange);
        resultEndpointMap.assertIsSatisfied();
        exchanges = resultEndpointMap.getExchanges();
        exchange = exchanges.get(0);
        mapResponse = exchange.getIn().getBody(MapResponse.class);
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map updated successfully", mapResponse.getHttpSuccessMessage());

        resultEndpointMap.reset();
        resultEndpointMap.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(CACHENAME, "mycache29");
        exchange.getIn().setHeader(MAPNAME, "mymap");
        producer.send(MAP_GET_ROUTE, exchange);
        resultEndpointMap.assertIsSatisfied();
        exchanges = resultEndpointMap.getExchanges();
        exchange = exchanges.get(0);
        mapResponse = exchange.getIn().getBody(MapResponse.class);
        assertNotNull(mapResponse);
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map obtained successfully", mapResponse.getHttpSuccessMessage());
        assertNotNull(mapResponse.getPayload());
        assertEquals(3, ((List)mapResponse.getPayload().get("key")).size());
    }

    @Test
    @DirtiesContext
    public void testMapCreateGet() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycache30");
        cacheRequest.setExpiry(0);
        resultEndpoint.expectedMessageCount(1);
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(cacheRequest);
        producer.send(CACHE_CREATE_ROUTE, exchange);
        resultEndpoint.assertIsSatisfied();
        List<Exchange> exchanges = resultEndpoint.getExchanges();
        exchange = exchanges.get(0);
        CacheResponse response = exchange.getIn().getBody(CacheResponse.class);
        assertEquals("success", response.getHttpStatus());
        assertEquals(CACHECREATEDSUCCESSFULLY, response.getHttpSuccessMessage());
        resultEndpoint.reset();
        exchanges.clear();
        MapRequest mapRequest = new MapRequest();
        mapRequest.setCacheName("mycache30");
        mapRequest.setMapName("mymap");
        mapRequest.setKeyName("key");
        mapRequest.setValues(Arrays.asList(1,2,3,4,5,6));
        resultEndpointMap.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(mapRequest);
        producer.send(MAP_CREATE_ROUTE, exchange);
        resultEndpointMap.assertIsSatisfied();
        exchanges = resultEndpointMap.getExchanges();
        exchange = exchanges.get(0);
        MapResponse mapResponse = exchange.getIn().getBody(MapResponse.class);
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map created successfully", mapResponse.getHttpSuccessMessage());

        resultEndpointMap.reset();
        resultEndpointMap.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(CACHENAME, "mycache30");
        exchange.getIn().setHeader(MAPNAME, "mymap");
        producer.send(MAP_GET_ROUTE, exchange);
        resultEndpointMap.assertIsSatisfied();
        exchanges = resultEndpointMap.getExchanges();
        exchange = exchanges.get(0);
        mapResponse = exchange.getIn().getBody(MapResponse.class);
        assertNotNull(mapResponse);
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map obtained successfully", mapResponse.getHttpSuccessMessage());
        assertNotNull(mapResponse.getPayload());
        assertEquals(6, ((List)mapResponse.getPayload().get("key")).size());
    }



    @Test
    @DirtiesContext
    public void testMapCreateDestroy() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycache31");
        cacheRequest.setExpiry(0);
        resultEndpoint.expectedMessageCount(1);
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(cacheRequest);
        producer.send(CACHE_CREATE_ROUTE, exchange);
        resultEndpoint.assertIsSatisfied();
        List<Exchange> exchanges = resultEndpoint.getExchanges();
        exchange = exchanges.get(0);
        CacheResponse response = exchange.getIn().getBody(CacheResponse.class);
        assertEquals("success", response.getHttpStatus());
        assertEquals(CACHECREATEDSUCCESSFULLY, response.getHttpSuccessMessage());
        resultEndpoint.reset();
        exchanges.clear();
        MapRequest mapRequest = new MapRequest();
        mapRequest.setCacheName("mycache31");
        mapRequest.setMapName("mymap");
        mapRequest.setKeyName("key");
        mapRequest.setValues(Arrays.asList(1,2,3,4,5,6));
        resultEndpointMap.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(mapRequest);
        producer.send(MAP_CREATE_ROUTE, exchange);
        resultEndpointMap.assertIsSatisfied();
        exchanges = resultEndpointMap.getExchanges();
        exchange = exchanges.get(0);
        MapResponse mapResponse = exchange.getIn().getBody(MapResponse.class);
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map created successfully", mapResponse.getHttpSuccessMessage());

        resultEndpointMap.reset();
        resultEndpointMap.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(mapRequest);
        producer.send(MAP_REMOVE_ROUTE, exchange);
        resultEndpointMap.assertIsSatisfied();
        exchanges = resultEndpointMap.getExchanges();
        exchange = exchanges.get(0);
        mapResponse = exchange.getIn().getBody(MapResponse.class);
        assertNotNull(mapResponse);
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map removed successfully", mapResponse.getHttpSuccessMessage());
    }

    @Test
    @DirtiesContext
    public void testMapCreateRemoveValues() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycache32");
        cacheRequest.setExpiry(0);
        resultEndpoint.expectedMessageCount(1);
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(cacheRequest);
        producer.send(CACHE_CREATE_ROUTE, exchange);
        resultEndpoint.assertIsSatisfied();
        List<Exchange> exchanges = resultEndpoint.getExchanges();
        exchange = exchanges.get(0);
        CacheResponse response = exchange.getIn().getBody(CacheResponse.class);
        assertEquals("success", response.getHttpStatus());
        assertEquals(CACHECREATEDSUCCESSFULLY, response.getHttpSuccessMessage());
        resultEndpoint.reset();
        exchanges.clear();
        MapRequest mapRequest = new MapRequest();
        mapRequest.setCacheName("mycache32");
        mapRequest.setMapName("mymap");
        mapRequest.setKeyName("key");
        mapRequest.setValues(Arrays.asList(1,2,3,4,5,6));
        resultEndpointMap.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(mapRequest);
        producer.send(MAP_CREATE_ROUTE, exchange);
        resultEndpointMap.assertIsSatisfied();
        exchanges = resultEndpointMap.getExchanges();
        exchange = exchanges.get(0);
        MapResponse mapResponse = exchange.getIn().getBody(MapResponse.class);
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map created successfully", mapResponse.getHttpSuccessMessage());

        resultEndpointMap.reset();
        resultEndpointMap.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(mapRequest);
        producer.send(MAP_REMOVE_KEY_ROUTE, exchange);
        resultEndpointMap.assertIsSatisfied();
        exchanges = resultEndpointMap.getExchanges();
        exchange = exchanges.get(0);
        mapResponse = exchange.getIn().getBody(MapResponse.class);
        assertNotNull(mapResponse);
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Value for key "+mapRequest.getKey()+" in map "+mapRequest.getMapName()+" Removed Successfully", mapResponse.getHttpSuccessMessage());
    }
}
