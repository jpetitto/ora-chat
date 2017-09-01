package com.johnpetitto.orachat.ui.chats

interface ChatsView {
    fun showLoading(show: Boolean)
    fun showError()
    fun displayChatData(chatData: List<Any>)
    fun showMoreChatData(chatData: List<Any>)
    fun pageLoaded()
}
