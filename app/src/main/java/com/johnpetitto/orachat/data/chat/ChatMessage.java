package com.johnpetitto.orachat.data.chat;

import com.google.gson.annotations.SerializedName;
import com.johnpetitto.orachat.data.user.User;

public class ChatMessage {
    private long id;
    private @SerializedName("chat_id") long chatId;
    private @SerializedName("user_id") long userId;
    private String message;
    private @SerializedName("created_at") String createdAt;
    private User user;

    // allows message to be created for demo purposes
    public ChatMessage(long userId, String message, String createdAt, User user) {
        this.userId = userId;
        this.message = message;
        this.createdAt = createdAt;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public long getChatId() {
        return chatId;
    }

    public long getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return message;
    }
}
