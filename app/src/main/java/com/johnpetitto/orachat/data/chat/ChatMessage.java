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

    public ChatMessage(long id, long chatId, long userId, String message, String createdAt, User user) {
        this.id = id;
        this.chatId = chatId;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatMessage that = (ChatMessage) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return message;
    }
}
