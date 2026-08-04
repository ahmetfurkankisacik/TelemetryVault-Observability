package com.telemetryvault.event.model;

import java.io.Serializable;

public class FileVaultEvent implements Serializable {

    private String eventId;
    private String fileId;
    private String userId;
    private String eventType;
    private String filename;
    private long fileSize;
    private boolean simulateFailure;
    private String timestamp;

    public FileVaultEvent() {
    }

    public FileVaultEvent(String eventId, String fileId, String userId, String eventType, String filename, long fileSize, boolean simulateFailure, String timestamp) {
        this.eventId = eventId;
        this.fileId = fileId;
        this.userId = userId;
        this.eventType = eventType;
        this.filename = filename;
        this.fileSize = fileSize;
        this.simulateFailure = simulateFailure;
        this.timestamp = timestamp;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
