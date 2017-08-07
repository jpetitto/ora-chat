package com.johnpetitto.orachat.data.user;

public class User {
    private long id;
    private String name;
    private String email;

    // allows user to be created to demo chat messages
    public User(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
