package com.nibm.medlink_healthcare;

public class FileModel {
    private String id;
    private String name;
    private String uri;
    private long timestamp;

    public FileModel() {
        // Empty constructor needed for Firestore
    }

    public FileModel(String name, String uri, long timestamp) {
        this.name = name;
        this.uri = uri;
        this.timestamp = timestamp;
    }

    // Getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
