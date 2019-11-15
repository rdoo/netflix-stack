package com.rdoo.netflixstack.imageservice.image;

import java.io.InputStream;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.client.gridfs.model.GridFSFile;

public class FileDTO {
    private String id;
    private String filename;
    private long size;
    private Date uploadDate;

    @JsonIgnore
    private InputStream inputStream;

    public FileDTO() {
    }

    public FileDTO(GridFSFile file) {
        this.setId(file.getObjectId().toString());
        this.setFilename(file.getFilename());
        this.setSize(file.getLength());
        this.setUploadDate(file.getUploadDate());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}