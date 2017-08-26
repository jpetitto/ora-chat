package com.johnpetitto.orachat.data.chat

import com.google.gson.annotations.SerializedName
import com.johnpetitto.orachat.data.user.User

data class Chat(
        val id: Long,
        val name: String,
        val users: List<User>,
        @SerializedName("last_chat_message") val lastChatMessage: ChatMessage
)
