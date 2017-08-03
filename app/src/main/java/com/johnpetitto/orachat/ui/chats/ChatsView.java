package com.johnpetitto.orachat.ui.chats;

import java.util.List;

public interface ChatsView {
    void showLoading(boolean show);
    void showError();
    void displayChatsByDate(List<Object> chatsGroupedByDate);
    void showMoreChatsByDate(List<Object> chatsGroupedByDate);
    void pageLoaded();
}
