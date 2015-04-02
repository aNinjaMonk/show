package com.example.abhi.show;

import java.util.ArrayList;

/**
 * Created by abhi on 24/02/15.
 */
public class Show {
    private String id;
    private String title;
    private String duration;
    private String briefUrl;
    private ArrayList<Entry> entries;

    public Show(String id,String title,String duration,String briefUrl,ArrayList<Entry> entries){
        this.id = id;
        this.title = title;
        this.briefUrl = briefUrl;
        this.duration = duration;
        this.entries = entries;
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<Entry> entries) {
        this.entries = entries;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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

    public String getBriefUrl() {
        return briefUrl;
    }

    public void setBriefUrl(String briefUrl) {
        this.briefUrl = briefUrl;
    }
}
