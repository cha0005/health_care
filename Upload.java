package com.nibm.medlink_healthcare;

public class Upload {
    private String fileName;
    private String fileUrl;
    private String key; // for delete reference
    private String name;
    private String url;
    private String id;

    public Upload() {
        // required empty constructor for Firebase
    }

    public Upload(String fileName, String fileUrl) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public Upload(String fileName, String string, String uploadId) {
        this.name = name;
        this.url = url;
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
