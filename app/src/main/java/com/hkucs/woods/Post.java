package com.hkucs.woods;


import java.util.Date;

public class Post {
    public String pid;
    public String author_uid;
    public String author_username;
    public Boolean moods;
    public String event;

    public Post(String pid, String author_uid, String author_username, Boolean moods, String event){
        this.pid = pid;
        this.author_uid = author_uid;
        this.author_username = author_username;
        this.moods = moods;
        this.event = event;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public String thought;
    public String action;
    public Date returnTime;

    public Post(){}




}
