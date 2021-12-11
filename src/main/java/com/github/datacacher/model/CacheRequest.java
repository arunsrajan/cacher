package com.github.datacacher.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.datacacher.constants.CacheConstants.CACHECOMMANDS;
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonSerialize
public class CacheRequest {
    @JsonProperty
    public String cacheName;
    @JsonProperty
    public Long expiry;
    @JsonProperty
    public CACHECOMMANDS command;

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public CACHECOMMANDS getCommand() {
        return command;
    }

    public void setCommand(CACHECOMMANDS command) {
        this.command = command;
    }
}
