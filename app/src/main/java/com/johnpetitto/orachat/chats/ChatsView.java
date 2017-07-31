package com.johnpetitto.orachat.chats;

import java.util.List;

public interface ChatsView {
    void showLoading(boolean show);
    void displayChatsByDate(List<Object> chatsGroupedByDate);
}
