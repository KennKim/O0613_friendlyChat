package com.project.tk.o0613_friendlychat.model;

/**
 * Created by conscious on 2017-06-15.
 */

public class User {
    private String key;
    private String userName;
    private String faceUrl;
    private Boolean isDeleted;
    private String insertedDate;
    private String updatedDate;
    private int type=10;

    public User() {
    }

    public User(String key, String userName, String faceUrl, String insertedDate, String updatedDate) {
        this.key = key;
        this.userName = userName;
        this.faceUrl = faceUrl;
        this.insertedDate = insertedDate;
        this.updatedDate = updatedDate;
        this.isDeleted = false;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getInsertedDate() {
        return insertedDate;
    }

    public void setInsertedDate(String insertedDate) {
        this.insertedDate = insertedDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }


}
