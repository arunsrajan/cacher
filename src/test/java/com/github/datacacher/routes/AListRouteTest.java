package com.github.datacacher.routes;

import com.github.datacacher.model.CacheRequest;
import com.github.datacacher.model.CacheResponse;
import com.github.datacacher.model.ListRequest;
import com.github.datacacher.model.ListResponse;
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
import static com.github.datacacher.constants.ListConstants.LISTNAME;
import static com.github.datacacher.constants.RouteConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AListRouteTest extends AnAbstractRouteTest {

    @Before
    public void initializeListRoutes() throws Exception {
        AdviceWithRouteBuilder.adviceWith(camelContext,CREATE_LIST_ID,(builder)->{
            builder.weaveAddLast()
                    .to(resultEndpointList);
        });
        AdviceWithRouteBuilder.adviceWith(camelContext,REMOVE_LIST_ID,(builder)->{
            builder.weaveAddLast()
                    .to(resultEndpointList);
        });
        AdviceWithRouteBuilder.adviceWith(camelContext,REMOVE_LIST_VALUES_ID,(builder)->{
            builder.weaveAddLast()
                    .to(resultEndpointList);
        });
        AdviceWithRouteBuilder.adviceWith(camelContext,ADD_LIST_ID,(builder)->{
            builder.weaveAddLast()
                    .to(resultEndpointList);
        });
        AdviceWithRouteBuilder.adviceWith(camelContext,GET_LIST_ID,(builder)->{
            builder.weaveAddLast()
                    .to(resultEndpointList);
        });
        AdviceWithRouteBuilder.adviceWith(camelContext,UPDATE_LIST_ID,(builder)->{
            builder.weaveAddLast()
                    .to(resultEndpointList);
        });
        resultEndpointList.reset();
    }

    @Test
    @DirtiesContext
    public void testListCreate() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycache23");
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
        ListRequest listRequest = new ListRequest();
        listRequest.setCacheName("mycache23");
        listRequest.setListName("mylist");
        resultEndpointList.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(listRequest);
        producer.send(LIST_CREATE_ROUTE, exchange);
        resultEndpointList.assertIsSatisfied();
        exchanges = resultEndpointList.getExchanges();
        exchange = exchanges.get(0);
        ListResponse listResponse = exchange.getIn().getBody(ListResponse.class);
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List created successfully", listResponse.getHttpSuccessMessage());

    }

    @Test
    @DirtiesContext
    public void testListUpdate() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycache24");
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
        ListRequest listRequest = new ListRequest();
        listRequest.setCacheName("mycache24");
        listRequest.setListName("mylist1");
        listRequest.setPayload(Arrays.asList(1,2,3,4,5));
        resultEndpointList.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(listRequest);
        producer.send(LIST_CREATE_ROUTE, exchange);
        resultEndpointList.assertIsSatisfied();
        exchanges = resultEndpointList.getExchanges();
        exchange = exchanges.get(0);
        ListResponse listResponse = exchange.getIn().getBody(ListResponse.class);
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List created successfully", listResponse.getHttpSuccessMessage());
        resultEndpointList.reset();
        listRequest = new ListRequest();
        listRequest.setCacheName("mycache24");
        listRequest.setListName("mylist1");
        listRequest.setPayload(Arrays.asList(1,2,3,4,5));
        resultEndpointList.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(listRequest);
        producer.send(LIST_UPDATE_ROUTE, exchange);
        resultEndpointList.assertIsSatisfied();
        exchanges = resultEndpointList.getExchanges();
        exchange = exchanges.get(0);
        listResponse = exchange.getIn().getBody(ListResponse.class);
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List updated successfully", listResponse.getHttpSuccessMessage());

        resultEndpointList.reset();
        resultEndpointList.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(CACHENAME, "mycache24");
        exchange.getIn().setHeader(LISTNAME, "mylist1");
        producer.send(LIST_GET_ROUTE, exchange);
        resultEndpointList.assertIsSatisfied();
        exchanges = resultEndpointList.getExchanges();
        exchange = exchanges.get(0);
        listResponse = exchange.getIn().getBody(ListResponse.class);
        assertNotNull(listResponse);
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List obtained successfully", listResponse.getHttpSuccessMessage());
        assertNotNull(listResponse.getPayload());
        assertEquals(5, listResponse.getPayload().size());
    }


    @Test
    @DirtiesContext
    public void testListAdd() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycache25");
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
        ListRequest listRequest = new ListRequest();
        listRequest.setCacheName("mycache25");
        listRequest.setListName("mylist1");
        listRequest.setPayload(Arrays.asList(1,2,3,4,5));
        resultEndpointList.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(listRequest);
        producer.send(LIST_CREATE_ROUTE, exchange);
        resultEndpointList.assertIsSatisfied();
        exchanges = resultEndpointList.getExchanges();
        exchange = exchanges.get(0);
        ListResponse listResponse = exchange.getIn().getBody(ListResponse.class);
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List created successfully", listResponse.getHttpSuccessMessage());
        resultEndpointList.reset();
        listRequest = new ListRequest();
        listRequest.setCacheName("mycache25");
        listRequest.setListName("mylist1");
        listRequest.setPayload(Arrays.asList(1,2,3,4,5));
        resultEndpointList.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(listRequest);
        producer.send(LIST_ADD_ROUTE, exchange);
        resultEndpointList.assertIsSatisfied();
        exchanges = resultEndpointList.getExchanges();
        exchange = exchanges.get(0);
        listResponse = exchange.getIn().getBody(ListResponse.class);
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List added successfully", listResponse.getHttpSuccessMessage());

        resultEndpointList.reset();
        resultEndpointList.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(CACHENAME, "mycache25");
        exchange.getIn().setHeader(LISTNAME, "mylist1");
        producer.send(LIST_GET_ROUTE, exchange);
        resultEndpointList.assertIsSatisfied();
        exchanges = resultEndpointList.getExchanges();
        exchange = exchanges.get(0);
        listResponse = exchange.getIn().getBody(ListResponse.class);
        assertNotNull(listResponse);
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List obtained successfully", listResponse.getHttpSuccessMessage());
        assertNotNull(listResponse.getPayload());
        assertEquals(10, listResponse.getPayload().size());
    }

    @Test
    @DirtiesContext
    public void testListGet() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycache26");
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
        ListRequest listRequest = new ListRequest();
        listRequest.setCacheName("mycache26");
        listRequest.setListName("mylist1");
        listRequest.setPayload(Arrays.asList(1,2,3,4,5));
        resultEndpointList.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(listRequest);
        producer.send(LIST_CREATE_ROUTE, exchange);
        resultEndpointList.assertIsSatisfied();
        exchanges = resultEndpointList.getExchanges();
        exchange = exchanges.get(0);
        ListResponse listResponse = exchange.getIn().getBody(ListResponse.class);
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List created successfully", listResponse.getHttpSuccessMessage());
        resultEndpointList.reset();

        resultEndpointList.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(CACHENAME, "mycache26");
        exchange.getIn().setHeader(LISTNAME, "mylist1");
        producer.send(LIST_GET_ROUTE, exchange);
        resultEndpointList.assertIsSatisfied();
        exchanges = resultEndpointList.getExchanges();
        exchange = exchanges.get(0);
        listResponse = exchange.getIn().getBody(ListResponse.class);
        assertNotNull(listResponse);
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List obtained successfully", listResponse.getHttpSuccessMessage());
        assertNotNull(listResponse.getPayload());
        assertEquals(5, listResponse.getPayload().size());
    }

    @Test
    @DirtiesContext
    public void testListRemove() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycache27");
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
        ListRequest listRequest = new ListRequest();
        listRequest.setCacheName("mycache27");
        listRequest.setListName("mylist1");
        listRequest.setPayload(Arrays.asList(1,2,3,4,5));
        resultEndpointList.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(listRequest);
        producer.send(LIST_CREATE_ROUTE, exchange);
        resultEndpointList.assertIsSatisfied();
        exchanges = resultEndpointList.getExchanges();
        exchange = exchanges.get(0);
        ListResponse listResponse = exchange.getIn().getBody(ListResponse.class);
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List created successfully", listResponse.getHttpSuccessMessage());
        resultEndpointList.reset();

        resultEndpointList.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        listRequest = new ListRequest();
        listRequest.setCacheName("mycache27");
        listRequest.setListName("mylist1");
        exchange.getIn().setBody(listRequest);
        producer.send(LIST_REMOVE_ROUTE, exchange);
        resultEndpointList.assertIsSatisfied();
        exchanges = resultEndpointList.getExchanges();
        exchange = exchanges.get(0);
        listResponse = exchange.getIn().getBody(ListResponse.class);
        assertNotNull(listResponse);
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List removed successfully", listResponse.getHttpSuccessMessage());
    }

    @Test
    @DirtiesContext
    public void testListRemoveValues() throws Exception {
        CacheRequest cacheRequest  =new CacheRequest();
        cacheRequest.setCacheName("mycache28");
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
        ListRequest listRequest = new ListRequest();
        listRequest.setCacheName("mycache28");
        listRequest.setListName("mylist1");
        listRequest.setPayload(Arrays.asList(1,2,3,4,5));
        resultEndpointList.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(listRequest);
        producer.send(LIST_CREATE_ROUTE, exchange);
        resultEndpointList.assertIsSatisfied();
        exchanges = resultEndpointList.getExchanges();
        exchange = exchanges.get(0);
        ListResponse listResponse = exchange.getIn().getBody(ListResponse.class);
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List created successfully", listResponse.getHttpSuccessMessage());
        resultEndpointList.reset();

        resultEndpointList.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        listRequest = new ListRequest();
        listRequest.setCacheName("mycache28");
        listRequest.setListName("mylist1");
        listRequest.setPayload(Arrays.asList(1,2));
        exchange.getIn().setBody(listRequest);
        producer.send(LIST_REMOVE_VALUES_ROUTE, exchange);
        resultEndpointList.assertIsSatisfied();
        exchanges = resultEndpointList.getExchanges();
        exchange = exchanges.get(0);
        listResponse = exchange.getIn().getBody(ListResponse.class);
        assertNotNull(listResponse);
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List values removed successfully", listResponse.getHttpSuccessMessage());

        resultEndpointList.reset();
        resultEndpointList.expectedMessageCount(1);
        exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(CACHENAME, "mycache28");
        exchange.getIn().setHeader(LISTNAME, "mylist1");
        producer.send(LIST_GET_ROUTE, exchange);
        resultEndpointList.assertIsSatisfied();
        exchanges = resultEndpointList.getExchanges();
        exchange = exchanges.get(0);
        listResponse = exchange.getIn().getBody(ListResponse.class);
        assertNotNull(listResponse);
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List obtained successfully", listResponse.getHttpSuccessMessage());
        assertNotNull(listResponse.getPayload());
        assertEquals(3, listResponse.getPayload().size());
    }
}
