package com.johnpetitto.orachat.ui.chatroom

import com.johnpetitto.orachat.data.chat.ChatMessage

interface ChatroomView {
    fun showLoading(show: Boolean)
    fun showError()
    fun showInitialMessages(messages: List<ChatMessage>)
    fun showNewMessage(message: ChatMessage)
    fun showMoreMessages(messages: List<ChatMessage>)
    fun pageLoaded()
}
