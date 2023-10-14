package com.example.chitchat.model;

public class MessageModel {
    String message,senderID;

    public MessageModel(String message, String senderID) {
        this.message = message;
        this.senderID = senderID;
    }

    public MessageModel()
    {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }
}
