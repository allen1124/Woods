package com.hkucs.woods;

public class User {
    private String uid;
    private String username;
    private String avatarImageUrl;


    public User() {
    }

    public User(String uid, String username, String avatarImageUrl){
       this.uid = uid;
       this.username = username;
       this.avatarImageUrl = avatarImageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarImageUrl() {
        return avatarImageUrl;
    }

    public void setAvatarImageUrl(String avatarImageUrl) {
        this.avatarImageUrl = avatarImageUrl;
    }
}
