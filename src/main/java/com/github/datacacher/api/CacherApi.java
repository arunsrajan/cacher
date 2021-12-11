package com.github.datacacher.api;

import com.github.datacacher.model.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;

import static com.github.datacacher.constants.CacheConstants.CACHENAME;
import static com.github.datacacher.constants.ListConstants.LISTNAME;
import static com.github.datacacher.constants.MapConstants.MAPNAME;
import static com.github.datacacher.constants.RouteConstants.*;


@Component("cacherApi")
public class CacherApi extends RouteBuilder {

    @Value("${cache.baseurl}")
    public String baseUrl;

    @Override
    public void configure() throws Exception {
        restConfiguration().component("netty-http").bindingMode(RestBindingMode.json)
                .enableCORS(true)
                // and output using pretty print
                .dataFormatProperty("prettyPrint", "true")
                // setup context path and port number that netty will use
                .contextPath(baseUrl).port(8081)
                // add swagger api-doc out of the box
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "User API").apiProperty("api.version", "1.2.3")
                // and enable CORS
                .apiProperty("cors", "true");

        rest("/cache").consumes(MediaType.APPLICATION_JSON).produces(MediaType.APPLICATION_JSON)
                .post().type(CacheRequest.class).outType(CacheResponse.class).to(CACHE_CREATE_ROUTE)
                .get("/{" + CACHENAME + "}")
                .outType(CacheResponse.class).to(CACHE_GET_ROUTE)
                .get("/{" + CACHENAME + "}/audit")
                .outType(CacheAuditResponse.class).to(CACHE_AUDIT_GET_ROUTE)
                .delete("/{" + CACHENAME + "}")
                .outType(CacheResponse.class).to(CACHE_REMOVE_ROUTE);

        rest("/list").produces(MediaType.APPLICATION_JSON).consumes(MediaType.APPLICATION_JSON)
                .get("/{" + CACHENAME + "}"+"/{" + LISTNAME + "}").type(ListRequest.class).outType(ListResponse.class).to(LIST_GET_ROUTE)
                .post().type(ListRequest.class).outType(ListResponse.class).to(LIST_CREATE_ROUTE)
                .put("/add").type(ListRequest.class).outType(ListResponse.class).to(LIST_ADD_ROUTE)
                .put().type(ListRequest.class).outType(ListResponse.class).to(LIST_UPDATE_ROUTE)
                .delete().type(ListRequest.class).outType(ListResponse.class).to(LIST_REMOVE_ROUTE)
                .delete("/values").type(ListRequest.class).outType(ListResponse.class).to(LIST_REMOVE_VALUES_ROUTE);

        rest("/map").produces(MediaType.APPLICATION_JSON).consumes(MediaType.APPLICATION_JSON)
                .get("/{" + CACHENAME + "}"+"/{" + MAPNAME + "}").type(MapRequest.class).outType(MapResponse.class).to(MAP_GET_ROUTE)
                .post().type(MapRequest.class).outType(MapResponse.class).to(MAP_CREATE_ROUTE)
                .put().type(MapRequest.class).outType(MapResponse.class).to(MAP_PUT_ROUTE)
                .delete().type(MapRequest.class).outType(MapResponse.class).to(MAP_REMOVE_ROUTE)
                .delete("/key").type(MapRequest.class).outType(MapResponse.class).to(MAP_REMOVE_KEY_ROUTE);
    }
}
