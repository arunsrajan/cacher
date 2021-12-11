package com.github.datacacher.constants;

public class RouteConstants {
    //Cache Routes
    public static final String CACHE_CREATE_ROUTE = "direct:cacheCreate";
    public static final String CACHE_REMOVE_ROUTE = "direct:cacheRemove";
    public static final String CACHE_GET_ROUTE = "direct:cacheGet";
    public static final String CACHE_AUDIT_ROUTE = "direct:audit";
    public static final String CACHE_AUDIT_GET_ROUTE = "direct:auditGet";

    //List Routes
    public static final String LIST_CREATE_ROUTE = "direct:listCreate";
    public static final String LIST_REMOVE_ROUTE = "direct:listRemove";
    public static final String LIST_REMOVE_VALUES_ROUTE = "direct:listremoveValues";
    public static final String LIST_ADD_ROUTE = "direct:listAdd";
    public static final String LIST_GET_ROUTE = "direct:listGet";
    public static final String LIST_UPDATE_ROUTE = "direct:listUpdate";

    //List Routes
    public static final String MAP_CREATE_ROUTE = "direct:mapCreate";
    public static final String MAP_REMOVE_ROUTE = "direct:mapRemove";
    public static final String MAP_REMOVE_KEY_ROUTE = "direct:mapRemoveKey";
    public static final String MAP_PUT_ROUTE = "direct:mapPut";
    public static final String MAP_GET_ROUTE = "direct:mapGet";

    //Cache route Ids
    public static final String CREATE_CACHE_ID = "CREATE_CACHE_ID";
    public static final String REMOVE_CACHE_ID = "REMOVE_CACHE_ID";
    public static final String GET_CACHE_ID = "GET_CACHE_ID";
    public static final String CACHE_AUDIT_ID = "GET_AUDIT_ID";
    public static final String CACHE_AUDIT_GET_ID = "CACHE_AUDIT_GET_ID";

    //List route Ids
    public static final String CREATE_LIST_ID = "CREATE_LIST_ID";
    public static final String REMOVE_LIST_ID = "REMOVE_LIST_ID";
    public static final String REMOVE_LIST_VALUES_ID = "REMOVE_VALUES_LIST_ID";
    public static final String ADD_LIST_ID = "ADD_LIST_ID";
    public static final String GET_LIST_ID = "GET_LIST_ID";
    public static final String UPDATE_LIST_ID = "UPDATE_LIST_ID";

    //Map Route Ids
    public static final String CREATE_MAP_ID = "CREATE_MAP_ID";
    public static final String REMOVE_MAP_ID = "REMOVE_MAP_ID";
    public static final String REMOVE_MAP_KEY_ID = "REMOVE_MAP_KEY_ID";
    public static final String PUT_MAP_ID = "PUT_MAP_ID";
    public static final String GET_MAP_ID = "GET_MAP_ID";

}
