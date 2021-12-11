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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ACacheRouteTest extends AnAbstractRouteTest{

    @Test
    @DirtiesContext
    public void testCacheCreate() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycache");
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
        cacheRequest.setCacheName("mycache1");
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
        exchange.getIn().setHeader(CACHENAME, "mycache1");
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
        cacheRequest.setCacheName("mycache2");
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
        exchange.getIn().setHeader(CACHENAME, "mycache2");
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
        cacheRequest.setCacheName("mycache3");
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
        exchange.getIn().setHeader(CACHENAME, "mycache3");
        producer.send(CACHE_GET_ROUTE, exchange);
        resultEndpoint.assertIsSatisfied();
        exchanges = resultEndpoint.getReceivedExchanges();
        exchange = exchanges.get(0);
        response = exchange.getIn().getBody(CacheResponse.class);
        assertEquals("success", response.getHttpStatus());
        assertEquals(CACHEOBTAINEDSUCCESSFULLY, response.getHttpSuccessMessage());
    }
}
