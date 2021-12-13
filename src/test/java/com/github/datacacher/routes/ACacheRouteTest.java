package com.github.datacacher.routes;

import com.github.datacacher.model.CacheRequest;
import com.github.datacacher.model.CacheResponse;
import org.apache.camel.Exchange;
import org.apache.camel.builder.AdviceWithBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.model.AdviceWithDefinition;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static com.github.datacacher.constants.CacheConstants.*;
import static com.github.datacacher.constants.RouteConstants.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ACacheRouteTest extends AnAbstractRouteTest{

    @Test
    @DirtiesContext
    public void testCacheCreate() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycache19");
        cacheRequest.setExpiry(120);
        resultEndpoint.expectedMessageCount(1);
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(cacheRequest);
        producer.send(CACHE_CREATE_ROUTE, exchange);
        resultEndpoint.assertIsSatisfied();
        List<Exchange> exchanges = resultEndpoint.getReceivedExchanges();
        exchange = exchanges.get(0);
        CacheResponse response = exchange.getIn().getBody(CacheResponse.class);
        assertEquals("success", response.getHttpStatus());
        assertEquals(CACHECREATEDSUCCESSFULLY, response.getHttpSuccessMessage());
    }
    @Test
    @DirtiesContext
    public void testCacheRemove() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycache20");
        cacheRequest.setExpiry(120);
        resultEndpoint.expectedMessageCount(1);
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(cacheRequest);
        producer.send(CACHE_CREATE_ROUTE, exchange);
        resultEndpoint.assertIsSatisfied();
        List<Exchange> exchanges = resultEndpoint.getReceivedExchanges();
        exchange = exchanges.get(0);
        CacheResponse response = exchange.getIn().getBody(CacheResponse.class);
        assertNotNull(response);
        assertEquals("success", response.getHttpStatus());
        assertEquals(CACHECREATEDSUCCESSFULLY, response.getHttpSuccessMessage());
        resultEndpoint.reset();
        resultEndpoint.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(CACHENAME, "mycache20");
        producer.send(CACHE_REMOVE_ROUTE, exchange);
        resultEndpoint.assertIsSatisfied();
        exchanges = resultEndpoint.getReceivedExchanges();
        exchange = exchanges.get(0);
        response = exchange.getIn().getBody(CacheResponse.class);
        assertEquals("success", response.getHttpStatus());
        assertEquals(CACHEREMOVEDSUCCESSFULLY, response.getHttpSuccessMessage());
    }

    @Test
    @DirtiesContext
    public void testCacheAutoRemoveOnExpiry() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycache21");
        cacheRequest.setExpiry(2);
        resultEndpoint.expectedMessageCount(1);
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(cacheRequest);
        producer.send(CACHE_CREATE_ROUTE, exchange);
        resultEndpoint.assertIsSatisfied();
        List<Exchange> exchanges = resultEndpoint.getReceivedExchanges();
        exchange = exchanges.get(0);
        CacheResponse response = exchange.getIn().getBody(CacheResponse.class);
        assertNotNull(response);
        assertEquals("success", response.getHttpStatus());
        assertEquals(CACHECREATEDSUCCESSFULLY, response.getHttpSuccessMessage());
        resultEndpoint.reset();
        Thread.sleep(3000);
        errorEndpoint.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(CACHENAME, "mycache21");
        producer.send(CACHE_GET_ROUTE, exchange);
        errorEndpoint.assertIsSatisfied();
        exchanges = errorEndpoint.getReceivedExchanges();
        exchange = exchanges.get(0);
        Exception exception = (Exception) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
        assertEquals(CACHENOTAVAILABLE, exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void testCacheGet() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycache22");
        cacheRequest.setExpiry(0);
        resultEndpoint.expectedMessageCount(1);
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(cacheRequest);
        producer.send(CACHE_CREATE_ROUTE, exchange);
        resultEndpoint.assertIsSatisfied();
        List<Exchange> exchanges = resultEndpoint.getReceivedExchanges();
        exchange = exchanges.get(0);
        CacheResponse response = exchange.getIn().getBody(CacheResponse.class);
        assertNotNull(response);
        assertEquals("success", response.getHttpStatus());
        assertEquals(CACHECREATEDSUCCESSFULLY, response.getHttpSuccessMessage());
        resultEndpoint.reset();
        resultEndpoint.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(CACHENAME, "mycache22");
        producer.send(CACHE_GET_ROUTE, exchange);
        resultEndpoint.assertIsSatisfied();
        exchanges = resultEndpoint.getReceivedExchanges();
        exchange = exchanges.get(0);
        response = exchange.getIn().getBody(CacheResponse.class);
        assertEquals("success", response.getHttpStatus());
        assertEquals(CACHEOBTAINEDSUCCESSFULLY, response.getHttpSuccessMessage());
    }

    @Test
    @DirtiesContext
    public void testCacheGetAvailability() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycacheavailable");
        cacheRequest.setExpiry(0);
        resultEndpoint.expectedMessageCount(1);
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(cacheRequest);
        producer.send(CACHE_CREATE_ROUTE, exchange);
        resultEndpoint.assertIsSatisfied();
        List<Exchange> exchanges = resultEndpoint.getReceivedExchanges();
        exchange = exchanges.get(0);
        CacheResponse response = exchange.getIn().getBody(CacheResponse.class);
        assertNotNull(response);
        assertEquals("success", response.getHttpStatus());
        assertEquals(CACHECREATEDSUCCESSFULLY, response.getHttpSuccessMessage());
        resultEndpoint.reset();
        resultEndpoint.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(CACHENAME, "mycacheavailable");
        producer.send(CACHE_GET_AVAILABLE_ROUTE, exchange);
        resultEndpoint.assertIsSatisfied();
        exchanges = resultEndpoint.getReceivedExchanges();
        exchange = exchanges.get(0);
        response = exchange.getIn().getBody(CacheResponse.class);
        assertEquals("success", response.getHttpStatus());
        assertEquals(CACHEISAVAILABLE, response.getHttpSuccessMessage());
        assertTrue((Boolean)response.getPayload());
    }

    @Test
    @DirtiesContext
    public void testCacheGetUnAvailable() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycacheunavailable");
        cacheRequest.setExpiry(0);
        resultEndpoint.expectedMessageCount(1);
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(cacheRequest);
        resultEndpoint.reset();
        resultEndpoint.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(CACHENAME, "mycacheunavailable");
        producer.send(CACHE_GET_AVAILABLE_ROUTE, exchange);
        resultEndpoint.assertIsSatisfied();
        List<Exchange> exchanges = resultEndpoint.getReceivedExchanges();
        exchange = exchanges.get(0);
        CacheResponse response = exchange.getIn().getBody(CacheResponse.class);
        assertEquals("success", response.getHttpStatus());
        assertEquals(CACHEISUNAVAILABLE, response.getHttpSuccessMessage());
        assertFalse((Boolean)response.getPayload());
    }
}
