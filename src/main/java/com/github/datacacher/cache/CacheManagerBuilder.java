package com.github.datacacher.cache;

import com.github.datacacher.exceptions.CacheManagerException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

import static com.github.datacacher.cache.CacheManager.isCacheManagerInitialized;

@Component
public class CacheManagerBuilder {
    @Bean
    public CacheManager cacheManager() throws CacheManagerException {
        if(!isCacheManagerInitialized()){
            new CacheManager(new ConcurrentHashMap<>());
        }
        return CacheManager.get();
    }
}
