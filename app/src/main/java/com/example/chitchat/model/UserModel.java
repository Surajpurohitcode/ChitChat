package com.example.chitchat.model;

public class UserModel {

    String name,phoneNumber,profileImage,userBio,lastMessage,uid;
    boolean isCompleted,isOnline;

    public UserModel(String name, String phoneNumber, String profileImage, String userBio, String lastMessage, String uid, boolean isCompleted, boolean isOnline) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.userBio = userBio;
        this.lastMessage = lastMessage;
        this.uid = uid;
        this.isCompleted = isCompleted;
        this.isOnline = isOnline;
    }

    public UserModel(String name, String phoneNumber, String profileImage) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
    }

    public UserModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
