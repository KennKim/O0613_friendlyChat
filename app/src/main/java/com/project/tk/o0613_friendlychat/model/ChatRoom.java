package com.project.tk.o0613_friendlychat.model;

/**
 * Created by conscious on 2017-06-15.
 */

public class ChatRoom {

    public static final String CHILD_CHAT_ROOM = "chatroom";

    private String key;
    private String uID;
    private Boolean isDeleted;
    private String insertedDate;
    private String updatedDate;

    public ChatRoom() {
    }

    public ChatRoom(String key, String uID,  String insertedDate, String updatedDate) {
        this.key = key;
        this.uID = uID;
        this.insertedDate = insertedDate;
        this.updatedDate = updatedDate;
        this.isDeleted = false;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
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
