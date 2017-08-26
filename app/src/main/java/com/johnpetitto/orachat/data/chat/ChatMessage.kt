package com.johnpetitto.orachat.data.chat

import com.google.gson.annotations.SerializedName
import com.johnpetitto.orachat.data.user.User

data class ChatMessage(
        val id: Long?,
        @SerializedName("chat_id") val chatId: Long?,
        @SerializedName("user_id") val userId: Long,
        val message: String,
        @SerializedName("created_at") val createdAt: String,
        val user: User
) {
    constructor(userId: Long, message: String, createdAt: String, user: User) :
            this(null, null, userId, message, createdAt, user)

    override fun toString() = message
}
