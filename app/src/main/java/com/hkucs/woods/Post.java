package com.hkucs.woods;




import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

public class Post {
    private String pid;
    private String uid;
    private String username;
    private String avatar_url;
    private Boolean moods;
    private String event;
    private String thought;
    private String action;
    private String remindDate;

    public Post(){}

    public Post(String pid, String uid, String username, String avatar_url, Boolean moods, String event,String thought,String action,String remindDate){
        this.pid = pid;
        this.uid = uid;
        this.username = username;
        this.avatar_url = avatar_url;
        this.moods = moods;
        this.event = event;
        this.thought = thought;
        this.action = action;
        this.remindDate = remindDate;
    }

    public Post(String pid, String uid, String username, Boolean moods, String event){
        this.pid = pid;
        this.uid = uid;
        this.username = username;
        this.moods = moods;
        this.event = event;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public void setUsername(String author_username) {
        this.username = author_username;
    }

    public Boolean getMoods() {
        return moods;
    }

    public void setMoods(Boolean moods) {
        this.moods = moods;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(String remindDate) {
        this.remindDate = remindDate;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public Map<String, Object> toMap(){
       HashMap<String, Object> result = new HashMap<>();
       result.put("pid",pid);
       result.put("uid",uid);
       result.put("username",username);
       result.put("avatar_url",avatar_url);
       result.put("moods",moods);
       result.put("event",event);
       result.put("thought",thought);
       result.put("action",action);
       result.put("remindDate",remindDate);
       return result;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj) {
        Post post = (Post) obj;
        return pid.matches(post.getPid());
    }

}
