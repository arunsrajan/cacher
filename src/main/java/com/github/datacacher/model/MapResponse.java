package com.github.datacacher.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.Map;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonSerialize
public class MapResponse {
    @JsonProperty
    private String mapName;
    @JsonProperty
    private String httpStatus;
    @JsonProperty
    private String httpSuccessMessage;
    @JsonProperty
    private String httpErrorMessage;
    @JsonProperty
    private String key;
    @JsonProperty
    private String value;
    @JsonProperty
    private Map<String, Object> payload;

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String listName) {
        this.mapName = mapName;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getHttpErrorMessage() {
        return httpErrorMessage;
    }

    public void setHttpErrorMessage(String httpErrorMessage) {
        this.httpErrorMessage = httpErrorMessage;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public String getHttpSuccessMessage() {
        return httpSuccessMessage;
    }

    public void setHttpSuccessMessage(String httpSuccessMessage) {
        this.httpSuccessMessage = httpSuccessMessage;
    }
}
