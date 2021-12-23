package com.github.datacacher.route;

import com.github.datacacher.exceptions.ListException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static com.github.datacacher.constants.CacheConstants.AUDIT;
import static com.github.datacacher.constants.ListConstants.LISTREQUEST;
import static com.github.datacacher.constants.RouteConstants.*;

@Component("listRoute")
@Profile("cacherApi")
public class ListRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        onException(ListException.class)
                .handled(true)
                .bean("listProcessor","processErrorResponse")
                .removeHeaders("*")
                .to("mock:error");
        from(LIST_CREATE_ROUTE).routeId(CREATE_LIST_ID)
                .onCompletion()
                .log("List ${exchangeProperty[LIST_REQUEST].listName} Created successfully...")
                .end()
                .setProperty(LISTREQUEST, body())
                .bean("listProcessor", "buildCacheRequest")
                .to(CACHE_GET_ROUTE)
                .bean("listValidator", "validate")
                .process("listProcessor")
                .bean("listProcessor", "processResponse")
                .setProperty(AUDIT, simple("List ${exchangeProperty[LIST_REQUEST].listName} Created successfully..."))
                .bean("cacheProcessor", "auditCache")
                .removeHeaders("*");
        from(LIST_ADD_ROUTE).routeId(ADD_LIST_ID)
                .onCompletion()
                .log("List ${exchangeProperty[LIST_REQUEST].listName} Added successfully...")
                .end()
                .setProperty(LISTREQUEST, body())
                .bean("listProcessor", "buildCacheRequest")
                .to(CACHE_GET_ROUTE)
                .bean("listValidator", "validate")
                .bean("listProcessor", "addToList")
                .bean("listProcessor", "processResponse")
                .setProperty(AUDIT, simple("List ${exchangeProperty[LIST_REQUEST].listName} Added successfully..."))
                .bean("cacheProcessor", "auditCache");
        from(LIST_GET_ROUTE).routeId(GET_LIST_ID)
                .onCompletion()
                .log("List ${exchangeProperty[LIST_REQUEST].listName} Obtained successfully...")
                .end()
                .setProperty(LISTREQUEST, body())
                .bean("listProcessor", "buildCacheAndListRequest")
                .to(CACHE_GET_ROUTE)
                .bean("listValidator", "validate")
                .bean("listProcessor", "getList")
                .bean("listProcessor", "processResponse")
                .setProperty(AUDIT, simple("List ${exchangeProperty[LIST_REQUEST].listName} Obtained successfully..."))
                .bean("cacheProcessor", "auditCache");
        from(LIST_UPDATE_ROUTE).routeId(UPDATE_LIST_ID)
                .onCompletion()
                .log("List ${exchangeProperty[LIST_REQUEST].listName} Updated successfully...")
                .end()
                .setProperty(LISTREQUEST, body())
                .bean("listProcessor", "buildCacheRequest")
                .to(CACHE_GET_ROUTE)
                .bean("listValidator", "validate")
                .bean("listProcessor", "updateList")
                .bean("listProcessor", "processResponse")
                .setProperty(AUDIT, simple("List ${exchangeProperty[LIST_REQUEST].listName} Updated successfully..."))
                .bean("cacheProcessor", "auditCache");
        from(LIST_REMOVE_ROUTE).routeId(REMOVE_LIST_ID)
                .onCompletion()
                .log("List ${exchangeProperty[LIST_REQUEST].listName} removed successfully...")
                .end()
                .setProperty(LISTREQUEST, body())
                .bean("listProcessor", "buildCacheRequest")
                .to(CACHE_GET_ROUTE)
                .bean("listValidator", "validate")
                .bean("listProcessor", "removeList")
                .bean("listProcessor", "processResponse")
                .setProperty(AUDIT, simple("List ${exchangeProperty[LIST_REQUEST].listName} Removed successfully..."))
                .bean("cacheProcessor", "auditCache");

        from(LIST_REMOVE_VALUES_ROUTE).routeId(REMOVE_LIST_VALUES_ID)
                .onCompletion()
                .log("Values for list ${exchangeProperty[LIST_REQUEST].listName} removed successfully...")
                .end()
                .setProperty(LISTREQUEST, body())
                .bean("listProcessor", "buildCacheRequest")
                .to(CACHE_GET_ROUTE)
                .bean("listValidator", "validate")
                .bean("listProcessor", "removeListValues")
                .bean("listProcessor", "processResponse")
                .setProperty(AUDIT, simple("Values for list ${exchangeProperty[LIST_REQUEST].listName} Removed successfully..."))
                .bean("cacheProcessor", "auditCache");

        from(LIST_SORT_ROUTE).routeId(SORT_LIST_VALUES_ID)
                .onCompletion()
                .log("List ${exchangeProperty[LIST_REQUEST].listName} sorted successfully...")
                .end()
                .setProperty(LISTREQUEST, body())
                .bean("listProcessor", "buildCacheAndListRequest")
                .to(CACHE_GET_ROUTE)
                .bean("listValidator", "validate")
                .bean("listProcessor", "sortAndGetList")
                .bean("listProcessor", "processResponse")
                .setProperty(AUDIT, simple("List ${exchangeProperty[LIST_REQUEST].listName} sorted successfully..."))
                .bean("cacheProcessor", "auditCache");

    }
}
