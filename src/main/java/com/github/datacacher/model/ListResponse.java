package com.github.datacacher.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonSerialize
public class ListResponse {
    @JsonProperty
    private String listName;
    @JsonProperty
    private String httpStatus;
    @JsonProperty
    private String httpSuccessMessage;
    @JsonProperty
    private String httpErrorMessage;
    @JsonProperty
    private List<Object> payload;

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
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

    public List<Object> getPayload() {
        return payload;
    }

    public void setPayload(List<Object> payload) {
        this.payload = payload;
    }

    public String getHttpSuccessMessage() {
        return httpSuccessMessage;
    }

    public void setHttpSuccessMessage(String httpSuccessMessage) {
        this.httpSuccessMessage = httpSuccessMessage;
    }
}
