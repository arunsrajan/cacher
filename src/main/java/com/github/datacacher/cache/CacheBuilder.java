package com.github.datacacher.cache;

import com.github.datacacher.exceptions.CacheManagerException;

public class CacheBuilder {
    private CacheBuilder(){
    }
    public static CacheBuilder builder(){
        return new CacheBuilder();
    }
    long expiry = 10;
    boolean persist = false;
    String cacheName = "default";
    public CacheBuilder setCacheExpiry(long expiry){
        this.expiry = expiry;
        return this;
    }
    public CacheBuilder setCacheName(String cacheName){
        this.cacheName = cacheName;
        return this;
    }
    public Cache build() throws CacheManagerException {
        return new Cache(cacheName,expiry);
    }

}
