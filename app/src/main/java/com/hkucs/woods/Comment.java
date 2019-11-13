package com.hkucs.woods;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Comment {
    public String cid;
    public String author_uid;
    public String author_username;
    public String avatar_url;
    public String content;
    public String timestamp;

    public Comment(String cid, String author_uid, String author_username, String avatar_url, String content, String timestamp) {
        this.cid = cid;
        this.author_uid = author_uid;
        this.author_username = author_username;
        this.avatar_url = avatar_url;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getAuthor_uid() {
        return author_uid;
    }

    public void setAuthor_uid(String author_uid) {
        this.author_uid = author_uid;
    }

    public String getAuthor_username() {
        return author_username;
    }

    public void setAuthor_username(String author_username) {
        this.author_username = author_username;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("cid", cid);
        result.put("author_uid", author_uid);
        result.put("author_username", author_username);
        result.put("avatar_url", avatar_url);
        result.put("content", content);
        result.put("timestamp", timestamp);
        return result;
    }
}
