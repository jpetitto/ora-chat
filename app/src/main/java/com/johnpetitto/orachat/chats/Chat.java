package com.johnpetitto.orachat.chats;

import com.google.gson.annotations.SerializedName;
import com.johnpetitto.orachat.user.User;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat chat = (Chat) o;

        return id == chat.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return name;
    }
}
