package com.johnpetitto.orachat.data.chat;

import com.johnpetitto.orachat.data.ApiResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChatService {
    @GET("chats")
    Single<ApiResponse<List<Chat>>> list(
            @Query("name") String name,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @POST("chats")
    Single<ApiResponse<Chat>> create(@Body CreateChat createChat);

    @PATCH("chats/{id}")
    Single<ApiResponse<Chat>> update(@Path("id") long id, @Body String name);

    @GET("chats/{id}/chat_messages")
    Single<ApiResponse<List<ChatMessage>>> listMessages(
            @Path("id") long id,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @POST("chats/{id}/chat_messages")
    Single<ApiResponse<ChatMessage>> createMessage(@Path("id") long id, @Body String message);
}
