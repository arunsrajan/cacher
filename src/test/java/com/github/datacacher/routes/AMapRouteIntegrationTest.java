package com.github.datacacher.routes;

import com.github.datacacher.Cacher;
import com.github.datacacher.client.CacherClientBuilder;
import com.github.datacacher.model.CacheResponse;
import com.github.datacacher.model.ListResponse;
import com.github.datacacher.model.MapResponse;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = {Cacher.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AMapRouteIntegrationTest extends AnAbstractRouteTest{

    @Test
    @DirtiesContext
    public void testMapCreate() throws Exception {
        CacherClientBuilder.CacherClient
                cacherClient = CacherClientBuilder.builder().setHost("localhost").setPort(8081).build();
        CacheResponse response = cacherClient.createCache("mycache10", 0);
        assertEquals("Cache created successfully", response.getHttpSuccessMessage());
        MapResponse mapResponse = cacherClient.createMap("mycache10", "mymap", "numbers", Arrays.asList("1","2","3","4","5"));
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map created successfully", mapResponse.getHttpSuccessMessage());
    }

    @Test
    @DirtiesContext
    public void testMapCreateGet() throws Exception {
        CacherClientBuilder.CacherClient
                cacherClient = CacherClientBuilder.builder().setHost("localhost").setPort(8081).build();
        CacheResponse response = cacherClient.createCache("mycache11", 0);
        assertEquals("Cache created successfully", response.getHttpSuccessMessage());
        MapResponse mapResponse = cacherClient.createMap("mycache11", "mymap", "vegetables", Arrays.asList("carrot","radish","brinjal"));
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map created successfully", mapResponse.getHttpSuccessMessage());
        mapResponse = cacherClient.getMap("mycache11", "mymap");
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map obtained successfully", mapResponse.getHttpSuccessMessage());
        assertEquals(1, mapResponse.getPayload().keySet().size());
    }

    @Test
    @DirtiesContext
    public void testMapCreateUpdateGet() throws Exception {
        CacherClientBuilder.CacherClient
                cacherClient = CacherClientBuilder.builder().setHost("localhost").setPort(8081).build();
        CacheResponse response = cacherClient.createCache("mycache12", 0);
        assertEquals("Cache created successfully", response.getHttpSuccessMessage());
        MapResponse mapResponse = cacherClient.createMap("mycache12", "mymap", "numbers", Arrays.asList("1","2","3","4","5"));
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map created successfully", mapResponse.getHttpSuccessMessage());

        mapResponse = cacherClient.putMap("mycache12", "mymap", "fruits", Arrays.asList("apple","banana","chikku"));
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map updated successfully", mapResponse.getHttpSuccessMessage());

        mapResponse = cacherClient.getMap("mycache12", "mymap");
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map obtained successfully", mapResponse.getHttpSuccessMessage());
        assertEquals(2, mapResponse.getPayload().keySet().size());
    }



    @Test
    @DirtiesContext
    public void testMapCreateDestroy() throws Exception {
        CacherClientBuilder.CacherClient
                cacherClient = CacherClientBuilder.builder().setHost("localhost").setPort(8081).build();
        CacheResponse response = cacherClient.createCache("mycache13", 0);
        assertEquals("Cache created successfully", response.getHttpSuccessMessage());
        MapResponse mapResponse = cacherClient.createMap("mycache13", "mymap", "eatables",Arrays.asList("read Sandwich", "Burger", "hotdogs"));
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map created successfully", mapResponse.getHttpSuccessMessage());

        mapResponse = cacherClient.destroyMap("mycache13", "mymap");
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map removed successfully", mapResponse.getHttpSuccessMessage());
    }

    @Test
    @DirtiesContext
    public void testMapCreateRemoveValues() throws Exception {
        CacherClientBuilder.CacherClient
                cacherClient = CacherClientBuilder.builder().setHost("localhost").setPort(8081).build();
        CacheResponse response = cacherClient.createCache("mycache14", 0);
        assertEquals("Cache created successfully", response.getHttpSuccessMessage());
        MapResponse mapResponse = cacherClient.createMap("mycache14", "mymap", "eatables",Arrays.asList("read Sandwich", "Burger", "hotdogs"));
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Map created successfully", mapResponse.getHttpSuccessMessage());

        mapResponse = cacherClient.destroyMapKey("mycache14", "mymap", "eatables");
        assertEquals("success", mapResponse.getHttpStatus());
        assertEquals("Value for key eatables in map mymap Removed Successfully", mapResponse.getHttpSuccessMessage());
    }
}
