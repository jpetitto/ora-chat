package com.johnpetitto.orachat.chats;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.johnpetitto.orachat.TimeUtils;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;

public class ChatModel {
    private ChatService service;

    public ChatModel(ChatService service) {
        this.service = service;
    }

    // TODO: add pagination
    public Single<List<Object>> getAllChatsByDate() {
        return service.list(1, 50)
                .map(responseBody -> {
                    // extract "data" field from response
                    JsonParser parser = new JsonParser();
                    JsonObject object = parser.parse(responseBody.string()).getAsJsonObject();
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Chat>>(){}.getType();
                    List<Chat> chats = gson.fromJson(object.getAsJsonArray("data"), type);

                    // create list of chats grouped by formatted date labels
                    List<Object> chatsGroupedByDate = new ArrayList<>();
                    DateFormat monthDayFormatter = new SimpleDateFormat("MMMM d", Locale.ENGLISH);
                    DateFormat monthDayYearFormatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

                    Calendar today = Calendar.getInstance();
                    Calendar currentDate = Calendar.getInstance();

                    String previousFormattedDate = null;
                    for (Chat chat : chats) {
                        // parse date for most recent chat message
                        String createdAt = chat.getLastChatMessage().getCreatedAt();
                        Date parsedDate = TimeUtils.dateParser.parse(createdAt);
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
                });
    }
}
