package com.johnpetitto.orachat.data.chat

import com.johnpetitto.orachat.data.ApiResponse
import io.reactivex.Single
import retrofit2.http.*

interface ChatService {
    @GET("chats")
    fun list(@Query("name") name: String?, @Query("page") page: Int, @Query("limit") limit: Int): Single<ApiResponse<List<Chat>>>

    @POST("chats")
    fun create(@Body chat: NewChat): Single<ApiResponse<Chat>>

    @PATCH("chats/{id}")
    fun update(@Path("id") id: Long, @Body name: String): Single<ApiResponse<Chat>>

    @GET("chats/{id}/chat_messages")
    fun listMessages(@Path("id") id: Long, @Query("page") page: Int, @Query("limit") limit: Int): Single<ApiResponse<List<ChatMessage>>>

    @POST("chats/{id}/chat_messages")
    fun createMessage(@Path("id") id: Long, @Body message: String): Single<ApiResponse<ChatMessage>>
}
