package com.github.datacacher.routes;

import com.github.datacacher.Cacher;
import com.github.datacacher.client.CacherClientBuilder;
import com.github.datacacher.model.CacheResponse;
import com.github.datacacher.model.ListResponse;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = {Cacher.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AListRouteIntegrationTest extends AnAbstractRouteTest{

    @Test
    @DirtiesContext
    public void testListCreate() throws Exception {
        CacherClientBuilder.CacherClient
                cacherClient = CacherClientBuilder.builder().setHost("localhost").setPort(8081).build();
        CacheResponse response = cacherClient.createCache("mycache4", 0);
        assertEquals("Cache created successfully", response.getHttpSuccessMessage());
        ListResponse listResponse = cacherClient.createList("mycache4", "mylist1", Arrays.asList("1","2","3","4","5"));
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List created successfully", listResponse.getHttpSuccessMessage());
    }

    @Test
    @DirtiesContext
    public void testListCreateGet() throws Exception {
        CacherClientBuilder.CacherClient
                cacherClient = CacherClientBuilder.builder().setHost("localhost").setPort(8081).build();
        CacheResponse response = cacherClient.createCache("mycache5", 0);
        assertEquals("Cache created successfully", response.getHttpSuccessMessage());
        ListResponse listResponse = cacherClient.createList("mycache5", "mylist1", Arrays.asList("1","2","3","4","5"));
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List created successfully", listResponse.getHttpSuccessMessage());
        listResponse = cacherClient.getList("mycache5", "mylist1");
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List obtained successfully", listResponse.getHttpSuccessMessage());
        assertEquals(5, listResponse.getPayload().size());
    }

    @Test
    @DirtiesContext
    public void testListCreateUpdateGet() throws Exception {
        CacherClientBuilder.CacherClient
                cacherClient = CacherClientBuilder.builder().setHost("localhost").setPort(8081).build();
        CacheResponse response = cacherClient.createCache("mycache6", 0);
        assertEquals("Cache created successfully", response.getHttpSuccessMessage());
        ListResponse listResponse = cacherClient.createList("mycache6", "mylist1", Arrays.asList("1","2","3","4","5"));
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List created successfully", listResponse.getHttpSuccessMessage());

        listResponse = cacherClient.updateList("mycache6", "mylist1", Arrays.asList("1","2","3"));
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List updated successfully", listResponse.getHttpSuccessMessage());

        listResponse = cacherClient.getList("mycache6", "mylist1");
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List obtained successfully", listResponse.getHttpSuccessMessage());
        assertEquals(3, listResponse.getPayload().size());
    }


    @Test
    @DirtiesContext
    public void testListCreateAddToListGet() throws Exception {
        CacherClientBuilder.CacherClient
                cacherClient = CacherClientBuilder.builder().setHost("localhost").setPort(8081).build();
        CacheResponse response = cacherClient.createCache("mycache7", 0);
        assertEquals("Cache created successfully", response.getHttpSuccessMessage());
        ListResponse listResponse = cacherClient.createList("mycache7", "mylist1", Arrays.asList("1","2","3","4","5"));
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List created successfully", listResponse.getHttpSuccessMessage());

        listResponse = cacherClient.addToList("mycache7", "mylist1", Arrays.asList("1","2","3"));
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List added successfully", listResponse.getHttpSuccessMessage());

        listResponse = cacherClient.getList("mycache7", "mylist1");
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List obtained successfully", listResponse.getHttpSuccessMessage());
        assertEquals(8, listResponse.getPayload().size());
    }

    @Test
    @DirtiesContext
    public void testListCreateDestroy() throws Exception {
        CacherClientBuilder.CacherClient
                cacherClient = CacherClientBuilder.builder().setHost("localhost").setPort(8081).build();
        CacheResponse response = cacherClient.createCache("mycache8", 0);
        assertEquals("Cache created successfully", response.getHttpSuccessMessage());
        ListResponse listResponse = cacherClient.createList("mycache8", "mylist1", Arrays.asList("1","2","3","4","5"));
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List created successfully", listResponse.getHttpSuccessMessage());

        listResponse = cacherClient.destroyList("mycache8", "mylist1");
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List removed successfully", listResponse.getHttpSuccessMessage());
    }

    @Test
    @DirtiesContext
    public void testListCreateRemoveListValues() throws Exception {
        CacherClientBuilder.CacherClient
                cacherClient = CacherClientBuilder.builder().setHost("localhost").setPort(8081).build();
        CacheResponse response = cacherClient.createCache("mycache9", 0);
        assertEquals("Cache created successfully", response.getHttpSuccessMessage());
        ListResponse listResponse = cacherClient.createList("mycache9", "mylist1", Arrays.asList("1","2","3","4","5"));
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List created successfully", listResponse.getHttpSuccessMessage());

        listResponse = cacherClient.removeListValues("mycache9", "mylist1", Arrays.asList("1","2","3"));
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List values removed successfully", listResponse.getHttpSuccessMessage());

        listResponse = cacherClient.getList("mycache9", "mylist1");
        assertEquals("success", listResponse.getHttpStatus());
        assertEquals("List obtained successfully", listResponse.getHttpSuccessMessage());
        assertEquals(2, listResponse.getPayload().size());
    }
}
