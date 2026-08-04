package com.telemetryvault.event.dto;

import com.telemetryvault.event.model.FileVaultEvent;

public class EventResponse {

    private String status;
    private String message;
    private String topic;
    private FileVaultEvent event;

    public EventResponse() {
    }

    public EventResponse(String status, String message, String topic, FileVaultEvent event) {
        this.status = status;
        this.message = message;
        this.topic = topic;
        this.event = event;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public FileVaultEvent getEvent() {
        return event;
    }

    public void setEvent(FileVaultEvent event) {
        this.event = event;
    }
}
