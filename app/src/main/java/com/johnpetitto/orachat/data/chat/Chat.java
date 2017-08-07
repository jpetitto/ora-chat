package com.johnpetitto.orachat.data.chat;

import com.google.gson.annotations.SerializedName;
import com.johnpetitto.orachat.data.user.User;

import java.util.List;

public class Chat {
    private long id;
    private String name;
    private List<User> users;
    private @SerializedName("last_chat_message") ChatMessage lastChatMessage;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    public ChatMessage getLastChatMessage() {
        return lastChatMessage;
    }
}
