package com.github.datacacher.route;

import com.github.datacacher.exceptions.ListException;
import com.github.datacacher.exceptions.MapException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static com.github.datacacher.constants.CacheConstants.AUDIT;
import static com.github.datacacher.constants.ListConstants.LISTREQUEST;
import static com.github.datacacher.constants.MapConstants.MAPREQUEST;
import static com.github.datacacher.constants.RouteConstants.*;

@Component("mapRoute")
@Profile("cacherApi")
public class MapRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        onException(MapException.class)
                .handled(true)
                .bean("mapProcessor","processErrorResponse")
                .removeHeaders("*")
                .to("mock:error");

        from(MAP_CREATE_ROUTE).routeId(CREATE_MAP_ID)
                .onCompletion()
                .log("List ${exchangeProperty[MAP_REQUEST].mapName} Created successfully...")
                .end()
                .setProperty(MAPREQUEST, body())
                .bean("mapProcessor", "buildCacheRequest")
                .to(CACHE_GET_ROUTE)
                .bean("mapValidator", "validate")
                .process("mapProcessor")
                .bean("mapProcessor", "processResponse")
                .setProperty(AUDIT, simple("List ${exchangeProperty[MAP_REQUEST].mapName} Created successfully..."))
                .bean("cacheProcessor", "auditCache")
                .removeHeaders("*");
        from(MAP_GET_ROUTE).routeId(GET_MAP_ID)
                .onCompletion()
                .log("Map ${exchangeProperty[MAP_REQUEST].mapName} Obtained successfully...")
                .end()
                .setProperty(MAPREQUEST, body())
                .bean("mapProcessor", "buildCacheAndMapRequest")
                .to(CACHE_GET_ROUTE)
                .bean("mapValidator", "validate")
                .bean("mapProcessor", "getMap")
                .bean("mapProcessor", "processResponse")
                .setProperty(AUDIT, simple("Map ${exchangeProperty[MAP_REQUEST].mapName} Obtained successfully..."))
                .bean("cacheProcessor", "auditCache")
                .removeHeaders("*");
        from(MAP_PUT_ROUTE).routeId(PUT_MAP_ID)
                .onCompletion()
                .log("Map ${exchangeProperty[MAP_REQUEST].mapName} updated successfully...")
                .end()
                .setProperty(MAPREQUEST, body())
                .bean("mapProcessor", "buildCacheRequest")
                .to(CACHE_GET_ROUTE)
                .bean("mapValidator", "validate")
                .bean("mapProcessor", "putMap")
                .bean("mapProcessor", "processResponse")
                .setProperty(AUDIT, simple("Map ${exchangeProperty[MAP_REQUEST].mapName} updated successfully..."))
                .bean("cacheProcessor", "auditCache")
                .removeHeaders("*");
        from(MAP_REMOVE_ROUTE).routeId(REMOVE_MAP_ID)
                .onCompletion()
                .log("Map ${exchangeProperty[MAP_REQUEST].mapName} removed successfully...")
                .end()
                .setProperty(MAPREQUEST, body())
                .bean("mapProcessor", "buildCacheRequest")
                .to(CACHE_GET_ROUTE)
                .bean("mapValidator", "validate")
                .bean("mapProcessor", "removeMap")
                .bean("mapProcessor", "processResponse")
                .setProperty(AUDIT, simple("Map ${exchangeProperty[MAP_REQUEST].mapName} removed successfully..."))
                .bean("cacheProcessor", "auditCache")
                .removeHeaders("*");
        from(MAP_REMOVE_KEY_ROUTE).routeId(REMOVE_MAP_KEY_ID)
                .onCompletion()
                .log("Map ${exchangeProperty[MAP_REQUEST].mapName} removed successfully...")
                .end()
                .setProperty(MAPREQUEST, body())
                .bean("mapProcessor", "buildCacheRequest")
                .to(CACHE_GET_ROUTE)
                .bean("mapValidator", "validate")
                .bean("mapProcessor", "removeKeyInMap")
                .bean("mapProcessor", "processResponse")
                .setProperty(AUDIT, simple("Map ${exchangeProperty[MAP_REQUEST].mapName} removed successfully..."))
                .bean("cacheProcessor", "auditCache")
                .removeHeaders("*");
    }
}
