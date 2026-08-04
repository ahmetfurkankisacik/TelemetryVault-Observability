package com.telemetryvault.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PublishEventRequest {

    private String eventId;

    @NotBlank(message = "File ID is required")
    private String fileId;

    @NotNull(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Event type is required")
    private String eventType;

    @NotBlank(message = "Filename is required")
    private String filename;

    private long fileSize;
    private boolean simulateFailure;

    public PublishEventRequest() {
    }

    public PublishEventRequest(String eventId, String fileId, String userId, String eventType, String filename, long fileSize, boolean simulateFailure) {
        this.eventId = eventId;
        this.fileId = fileId;
        this.userId = userId;
        this.eventType = eventType;
        this.filename = filename;
        this.fileSize = fileSize;
        this.simulateFailure = simulateFailure;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isSimulateFailure() {
        return simulateFailure;
    }

    public void setSimulateFailure(boolean simulateFailure) {
        this.simulateFailure = simulateFailure;
    }
}
