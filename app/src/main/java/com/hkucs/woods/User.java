package com.hkucs.woods;

public class User {
    public String uid;
    public String username;
    public String avatarImageUrl;

    public User() {
    }

    public User(String uid, String username, String avatarImageUrl){
       this.uid = uid;
       this.username = username;
       this.avatarImageUrl = avatarImageUrl;
    }

}
