package com.github.datacacher.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonSerialize
public class MapRequest {
    @JsonProperty
    private String cacheName;
    @JsonProperty
    private String mapName;
    @JsonProperty
    private String key;
    @JsonProperty
    private Object values;

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getKey() {
        return key;
    }

    public void setKeyName(String key) {
        this.key = key;
    }

    public Object getValues() {
        return values;
    }

    public void setValues(Object values) {
        this.values = values;
    }
}
