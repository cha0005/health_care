package com.nibm.medlink_healthcare;

public class RecordModel {
    private String id;
    private String name;
    private String data; // base64 string
    private long timestamp;

    // Empty constructor for Firestore
    public RecordModel() {}

    public RecordModel(String id, String name, String data, long timestamp) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.timestamp = timestamp;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
