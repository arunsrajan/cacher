package com.github.datacacher.constants;

public class CacheConstants {
    public static enum CACHECOMMANDS{CREATE,DELETE};
    public static final String CACHEINITIALIZEXCEPTION = "Cache manager is not initialized";
    public static final String CACHEALREADYCREATED = "Cache already available, Cannot create existing cache";
    public static final String CACHEISAVAILABLE = "Cache is available";
    public static final String CACHEISUNAVAILABLE = "Cache is not available";
    public static final String CACHENOTAVAILABLE = "Cache not available, either cache expired or not created";
    public static final String CACHECREATEDSUCCESSFULLY = "Cache created successfully";
    public static final String CACHEREMOVEDSUCCESSFULLY = "Cache removed successfully";
    public static final String CACHEOBTAINEDSUCCESSFULLY = "Cache obtained successfully";
    public static final String AUDITOBTAINEDSUCCESSFULLY = "Audit obtained successfully";
    public static final String PROVIDECACHENAME = "Please provide mandatory field cache name";
    public static final String PROVIDECACHEEXPIRY = "Please provide mandatory field cache expiry";
    public static final String CACHENAME = "cacheName";
    public static final String AUDIT = "audit";
}
