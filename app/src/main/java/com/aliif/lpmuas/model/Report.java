package com.aliif.lpmuas.model;


public class Report {

    private String user_id;

    private String id;
    private String title;
    private String content;
    private String date;
    private String location;

    public Report()
    {
        // Default constructor required for calls to DataSnapshot.getValue(Report.class)
    }

    public Report(String user_id, String title, String content, String date, String location) {
        this.user_id = user_id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.location = location;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
