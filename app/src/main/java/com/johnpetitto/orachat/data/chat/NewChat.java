package com.johnpetitto.orachat.data.chat;

public class NewChat {
    private String name;
    private String message;

    public NewChat(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}
