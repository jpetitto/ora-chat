package com.johnpetitto.orachat.ui.chatroom;

import com.johnpetitto.orachat.data.chat.ChatMessage;

import java.util.List;

public interface ChatroomView {
    void showLoading(boolean show);
    void showError();
    void showInitialMessages(List<ChatMessage> messages);
    void showNewMessage(ChatMessage message);
    void showMoreMessages(List<ChatMessage> messages);
    void pageLoaded();
}
