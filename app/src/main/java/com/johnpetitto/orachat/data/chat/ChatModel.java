package com.johnpetitto.orachat.data.chat;

import com.johnpetitto.orachat.TimeUtils;
import com.johnpetitto.orachat.data.ApiResponse;
import com.johnpetitto.orachat.data.Pagination;
import com.johnpetitto.orachat.data.ResponseTransformer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.NonNull;

public class ChatModel {
    private ChatService service;

    private String currentSearchName;
    private Pagination currentChatPage;

    private long activeChatroomId;
    private Pagination currentChatroomPage;

    public ChatModel(ChatService service) {
        this.service = service;
    }

    public Single<List<Object>> getAllChatsByDate() {
        return searchChatsByName(null);
    }

    public Single<List<Object>> searchChatsByName(String name) {
        currentSearchName = name;
        return service.list(name, 1, 50).compose(new ChatsTransformer());
    }

    public Maybe<List<Object>> loadMoreChats() {
        int currentPage = currentChatPage.getCurrentPage();
        if (currentPage == currentChatPage.getPageCount()) {
            return Maybe.empty(); // no more pages available
        }

        return service.list(currentSearchName, currentPage + 1, currentChatPage.getPerPage())
                .compose(new ChatsTransformer())
                .toMaybe();
    }

    private List<Object> createChatsGroupedByDate(List<Chat> chats) throws ParseException {
        List<Object> chatsGroupedByDate = new ArrayList<>();

        DateFormat monthDayFormatter = new SimpleDateFormat("MMMM d", Locale.ENGLISH);
        DateFormat monthDayYearFormatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Calendar today = Calendar.getInstance();
        Calendar currentDate = Calendar.getInstance();

        String previousFormattedDate = null;
        for (Chat chat : chats) {
            // parse date for most recent chat message
            String createdAt = chat.getLastChatMessage().getCreatedAt();
            Date parsedDate = TimeUtils.dateFormatter.parse(createdAt);
            currentDate.setTime(parsedDate);

            // create properly formatted date label
            String formattedDate = null;
            if (currentDate.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
                if (currentDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                    formattedDate = "Today";
                } else {
                    formattedDate = monthDayFormatter.format(parsedDate);
                }
            } else {
                // dates in previous calendar years include year in label
                formattedDate = monthDayYearFormatter.format(parsedDate);
            }

            if (formattedDate.equals(previousFormattedDate)) {
                // date label already added for previous chat
                chatsGroupedByDate.add(chat);
            } else {
                // date label is new, add it to list with chat
                chatsGroupedByDate.add(formattedDate);
                chatsGroupedByDate.add(chat);
                previousFormattedDate = formattedDate;
            }
        }

        return chatsGroupedByDate;
    }

    private class ChatsTransformer implements SingleTransformer<ApiResponse<List<Chat>>, List<Object>> {
        @Override
        public SingleSource<List<Object>> apply(@NonNull Single<ApiResponse<List<Chat>>> upstream) {
            return upstream
                    .doOnSuccess(response -> currentChatPage = response.getMeta().getPagination())
                    .compose(new ResponseTransformer<>())
                    .map(ChatModel.this::createChatsGroupedByDate);
        }
    }

    public Single<ChatMessage> createNewChat(String name, String message) {
        return service.create(new NewChat(name, message))
                .compose(new ResponseTransformer<>())
                .map(chat -> {
                    activeChatroomId = chat.getId();
                    return chat.getLastChatMessage();
                });
    }

    public Single<List<ChatMessage>> getMessagesForChat(long chatId) {
        activeChatroomId = chatId;
        return service.listMessages(chatId, 1, 50)
                .doOnSuccess(response -> currentChatroomPage = response.getMeta().getPagination())
                .compose(new ResponseTransformer<>());
    }

    public Maybe<List<ChatMessage>> loadMoreMessages() {
        int currentPage = currentChatroomPage.getCurrentPage();
        if (currentPage == currentChatroomPage.getPageCount()) {
            return Maybe.empty(); // no more pages available;
        }

        return service.listMessages(activeChatroomId, currentPage + 1, currentChatroomPage.getPerPage())
                .doOnSuccess(response -> currentChatroomPage = response.getMeta().getPagination())
                .compose(new ResponseTransformer<>())
                .toMaybe();
    }

    public Single<ChatMessage> sendMessage(String message) {
        return service.createMessage(activeChatroomId, message)
                .compose(new ResponseTransformer<>());
    }

    public Completable updateChatName(String name) {
        return service.update(activeChatroomId, name)
                .compose(new ResponseTransformer<>())
                .toCompletable();
    }
}
