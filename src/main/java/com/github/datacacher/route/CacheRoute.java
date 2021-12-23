package com.github.datacacher.route;

import com.github.datacacher.exceptions.CacheException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static com.github.datacacher.constants.CacheConstants.*;
import static com.github.datacacher.constants.RouteConstants.*;

@Component
@Profile("cacherApi")
public class CacheRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        onException(CacheException.class)
                .handled(true)
                .bean("cacheProcessor", "processErrorResponse")
                .to("mock:error");
        from(CACHE_CREATE_ROUTE).routeId(CREATE_CACHE_ID)
                .onCompletion()
                .log("Completed Creating Cache")
                .end()
                .bean("cacheValidator", "validateCacheRequest")
                .log("Creating Cache ${body.cacheName}...")
                .process("cacheProcessor")
                .bean("cacheProcessor", "processResponse")
                .removeHeaders("*");

        from(CACHE_REMOVE_ROUTE).routeId(REMOVE_CACHE_ID)
                .onCompletion()
                .log("Completed Removing Cache")
                .end()
                .bean("cacheValidator", "validate")
                .log("Removing Cache ${header.cacheName}...")
                .bean("cacheProcessor", "removeCache")
                .bean("cacheProcessor", "processResponse")
                .removeHeaders("*");

        from(CACHE_GET_ROUTE).routeId(GET_CACHE_ID)
                .onCompletion()
                .log("Completed Getting Cache")
                .end()
                .bean("cacheValidator", "validate")
                .log("Getting Cache ${header.cacheName}...")
                .bean("cacheProcessor", "getCache")
                .bean("cacheProcessor", "processResponse")
                .removeHeaders("*");

        from(CACHE_AUDIT_ROUTE).routeId(CACHE_AUDIT_ID)
                .onCompletion()
                .log("Completed Auditing Cache")
                .end()
                .bean("cacheValidator", "validate")
                .log("Auditing Cache ${header.cacheName}...")
                .bean("cacheProcessor", "auditCache");

        from(CACHE_AUDIT_GET_ROUTE).routeId(CACHE_AUDIT_GET_ID)
                .onCompletion()
                .log("Completed Obtaining Audit Cache")
                .end()
                .bean("cacheValidator", "validate")
                .log("Get Audit from Cache ${header.cacheName}...")
                .bean("cacheProcessor", "getAuditCache")
                .bean("cacheProcessor", "processAuditResponse");

        from(CACHE_GET_AVAILABLE_ROUTE).routeId(CACHE_GET_AVAILABLE_ID)
                .onCompletion()
                .log("Completed Cache Availability")
                .end()
                .bean("cacheValidator", "validate")
                .log("Get Cache Availability for cache ${header.cacheName}...")
                .bean("cacheProcessor", "getCacheAvailability")
                .bean("cacheProcessor", "processResponse");
    }
}
