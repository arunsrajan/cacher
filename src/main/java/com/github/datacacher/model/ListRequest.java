package com.github.datacacher.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.datacacher.constants.ListConstants;

import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonSerialize
public class ListRequest {
    @JsonProperty
    private String cacheName;
    @JsonProperty
    private String listName;
    @JsonProperty
    private ListConstants.LISTCOMMANDS command;
    @JsonProperty
    private List<Object> payload;

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public ListConstants.LISTCOMMANDS getCommand() {
        return command;
    }

    public void setCommand(ListConstants.LISTCOMMANDS command) {
        this.command = command;
    }

    public List<Object> getPayload() {
        return payload;
    }

    public void setPayload(List<Object> payload) {
        this.payload = payload;
    }
}
