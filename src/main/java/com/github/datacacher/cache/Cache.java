package com.github.datacacher.cache;

import lombok.SneakyThrows;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cache {
    Logger logger = Logger.getLogger(Cache.class.getName());
    private String cacheName;
    private Timer timer;
    private ConcurrentMap<String, Object> cacheCollectionEntries;
    private List<String> audit;
    public Cache(final String cacheName, final long expiry) {
        this.cacheName = cacheName;
        this.timer = new Timer();
        this.cacheCollectionEntries = new ConcurrentHashMap<>();
        this.audit = new Vector<>();
        if (expiry > 0) {
            timer.schedule(new TimerTask() {
                @SneakyThrows
                @Override
                public void run() {
                    CacheManager.get().removeCache(cacheName);
                    timer.cancel();
                    logger.log(Level.INFO, "Cache " + cacheName + " Expired");
                }
            }, expiry * 1000);
        }
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public ConcurrentMap<String, Object> getCacheCollectionEntries() {
        return cacheCollectionEntries;
    }

    public void setCacheCollectionEntries(ConcurrentMap<String, Object> cacheCollectionEntries) {
        this.cacheCollectionEntries = cacheCollectionEntries;
    }

    public List<String> getAudit() {
        return audit;
    }

    public void setAudit(List<String> audit) {
        this.audit = audit;
    }
}
