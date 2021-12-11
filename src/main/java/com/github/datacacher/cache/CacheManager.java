package com.github.datacacher.cache;

import com.github.datacacher.constants.CacheConstants;
import com.github.datacacher.exceptions.CacheException;
import com.github.datacacher.exceptions.CacheManagerException;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.github.datacacher.constants.CacheConstants.CACHEALREADYCREATED;
import static com.github.datacacher.constants.CacheConstants.CACHENOTAVAILABLE;

public class CacheManager {

    protected CacheManager(ConcurrentMap<String, Cache> cacheEntries){
        this.cacheEntries = cacheEntries;
        cacheManager = this;
    }
    private static ConcurrentMap<String, Cache> cacheEntries;
    private static CacheManager cacheManager;
    public static CacheManager get() throws CacheManagerException {
        if(isCacheManagerInitialized()) {
            return cacheManager;
        }else{
            throw new CacheManagerException(CacheConstants.CACHEINITIALIZEXCEPTION) ;
        }
    }

    protected static boolean isCacheManagerInitialized() throws CacheManagerException {
        if(Objects.isNull(cacheManager)){
            return false;
        }
        return true;
    }
    private static boolean isCacheCreated(String cacheName) {
        if(cacheEntries.containsKey(cacheName)&&!Objects.isNull(cacheEntries.get(cacheName))){
            return true;
        }
        return false;
    }
    public synchronized boolean createCache(String cacheName, long expire) throws CacheException, CacheManagerException {
        if(isCacheCreated(cacheName)){
            throw new CacheException(CACHEALREADYCREATED);
        }
        Cache cache = CacheBuilder.builder().setCacheName(cacheName).setCacheExpiry(expire).build();
        cacheEntries.put(cacheName,cache);
        return true;
    }
    public synchronized boolean removeCache(String cacheName) throws CacheException {
        if(isCacheCreated(cacheName)){
            Cache cache = cacheEntries.remove(cacheName);
            if(!Objects.isNull(cache.getTimer())){
                cache.getTimer().cancel();
            }
            return true;
        }else{
            throw new CacheException(CACHENOTAVAILABLE);
        }
    }
    public synchronized Cache getCache(String cacheName) throws CacheException {
        if(isCacheCreated(cacheName)){
            Cache cache = cacheEntries.get(cacheName);
            return cache;
        }else{
            throw new CacheException(CACHENOTAVAILABLE);
        }
    }
}
