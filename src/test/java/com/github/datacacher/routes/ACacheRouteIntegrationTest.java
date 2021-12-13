package com.github.datacacher.routes;

import com.github.datacacher.Cacher;
import com.github.datacacher.client.CacherClientBuilder;
import com.github.datacacher.model.CacheAuditResponse;
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
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.github.datacacher.constants.CacheConstants.*;
import static com.github.datacacher.constants.RouteConstants.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = {Cacher.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ACacheRouteIntegrationTest extends AnAbstractRouteTest{

    @Test
    @DirtiesContext
    public void testCacheCreate() throws Exception {
        CacherClientBuilder.CacherClient
        cacherClient = CacherClientBuilder.builder().setHost("localhost").setPort(8081).build();
        CacheResponse response = cacherClient.createCache("mycache15", 0);
        assertEquals("Cache created successfully", response.getHttpSuccessMessage());
    }

    @Test
    @DirtiesContext
    public void testCacheCreateGet() throws Exception {
        CacherClientBuilder.CacherClient
                cacherClient = CacherClientBuilder.builder().setHost("localhost").setPort(8081).build();
        CacheResponse response = cacherClient.createCache("mycache16", 0);
        assertEquals("Cache created successfully", response.getHttpSuccessMessage());
        response = cacherClient.getCache("mycache16");
        assertEquals("Cache obtained successfully", response.getHttpSuccessMessage());
    }

    @Test
    @DirtiesContext
    public void testCacheCreateDestroy() throws Exception {
        CacherClientBuilder.CacherClient
                cacherClient = CacherClientBuilder.builder().setHost("localhost").setPort(8081).build();
        CacheResponse response = cacherClient.createCache("mycache17", 0);
        assertEquals("Cache created successfully", response.getHttpSuccessMessage());
        response = cacherClient.destroyCache("mycache17");
        assertEquals("Cache removed successfully", response.getHttpSuccessMessage());
    }

    @Test
    @DirtiesContext
    public void testCacheCreateGetAudit() throws Exception {
        CacherClientBuilder.CacherClient
                cacherClient = CacherClientBuilder.builder().setHost("localhost").setPort(8081).build();
        CacheResponse response = cacherClient.createCache("mycache18", 0);
        assertEquals("Cache created successfully", response.getHttpSuccessMessage());
        CacheAuditResponse auditResponse = cacherClient.getCacheAudit("mycache18");
        assertEquals("Audit obtained successfully", auditResponse.getHttpSuccessMessage());
        assertNotNull(auditResponse.getAudit());
    }

    @Test
    @DirtiesContext
    public void testCacheGetAvailability() throws Exception {
        CacherClientBuilder.CacherClient
                cacherClient = CacherClientBuilder.builder().setHost("localhost").setPort(8081).build();
        CacheResponse response = cacherClient.createCache("mycacheintgavailable", 0);
        assertEquals("Cache created successfully", response.getHttpSuccessMessage());
        response = cacherClient.getCacheAvailability("mycacheintgavailable");
        assertEquals(CACHEISAVAILABLE, response.getHttpSuccessMessage());
        assertTrue((Boolean) response.getPayload());
    }

    @Test
    @DirtiesContext
    public void testCacheGetUnAvailable() throws Exception {
        CacherClientBuilder.CacherClient
                cacherClient = CacherClientBuilder.builder().setHost("localhost").setPort(8081).build();
        CacheResponse auditResponse = cacherClient.getCacheAvailability("mycacheintgunavailable");
        assertEquals(CACHEISUNAVAILABLE, auditResponse.getHttpSuccessMessage());
        assertFalse((Boolean) auditResponse.getPayload());
    }
}

