package com.johnpetitto.orachat.data.chat;

import com.johnpetitto.orachat.data.ApiResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChatService {
    @GET("chats")
    Single<ApiResponse<List<Chat>>> chats(
            @Query("name") String name,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @GET("chats/{id}/chat_messages")
    Single<ApiResponse<List<ChatMessage>>> chatMessages(
            @Path("id") long id,
            @Query("page") int page,
            @Query("limit") int limit
    );
}
