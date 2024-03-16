package com.app.notelock;

import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Item {
    private String id;
    private String name;
    private String username;
    private String password;
    private String url;
    private String creation;

    public Item() {
    }

    public Item(String name, String username, String password, String url) {

        this.name = name;
        this.username = username;
        this.password = password;
        this.url = url;
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        creation = simpleDateFormat.format(date);
    }

    @Exclude
    public String getId() {
        return id;
    }

    public String getCreation() {
        return creation;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
