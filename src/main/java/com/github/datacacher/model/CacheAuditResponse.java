package com.github.datacacher.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonSerialize
public class CacheAuditResponse {
    @JsonProperty
    private String cacheName;
    @JsonProperty
    private String httpStatus;
    @JsonProperty
    private String httpErrorMessage;
    @JsonProperty
    private String httpSuccessMessage;
    @JsonProperty
    private Object audit;


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

    public String getHttpSuccessMessage() {
        return httpSuccessMessage;
    }

    public void setHttpSuccessMessage(String httpSuccessMessage) {
        this.httpSuccessMessage = httpSuccessMessage;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public Object getAudit() {
        return audit;
    }

    public void setAudit(Object audit) {
        this.audit = audit;
    }
}
