package com.johnpetitto.orachat.data.chat

import com.johnpetitto.orachat.data.ApiResponse
import com.johnpetitto.orachat.data.Pagination
import com.johnpetitto.orachat.data.ResponseTransformer
import com.johnpetitto.orachat.dateFormatter
import io.reactivex.*
import java.text.SimpleDateFormat
import java.util.*

class ChatModel(private val service: ChatService) {
    private var currentSearchName: String? = null
    private lateinit var currentChatPage: Pagination

    private var activeChatroomId: Long = 0
    private lateinit var currentChatroomPage: Pagination

    fun getAllChatsByDate() = searchChatsByName(null)

    fun searchChatsByName(name: String?): Single<List<Any>> {
        currentSearchName = name
        return service.list(name, 1, 50).compose(ChatsTransformer())
    }

    fun loadMoreChats(): Maybe<List<Any>> {
        val currentPage = currentChatPage.currentPage
        return if (currentPage < currentChatPage.pageCount) {
            service.list(currentSearchName, currentPage + 1, currentChatPage.perPage)
                    .compose(ChatsTransformer())
                    .toMaybe()
        } else {
            Maybe.empty() // no more pages available
        }
    }

    fun createNewChat(name: String, message: String): Single<ChatMessage> =
            service.create(NewChat(name, message))
                    .compose(ResponseTransformer())
                    .map { chat ->
                        activeChatroomId = chat.id
                        chat.lastChatMessage
                    }

    fun getMessagesForChat(chatId: Long): Single<List<ChatMessage>> {
        activeChatroomId = chatId
        return service.listMessages(chatId, 1, 50)
                .doOnSuccess { response -> currentChatroomPage = response.meta!!.pagination }
                .compose(ResponseTransformer())
    }

    fun loadMoreMessages(): Maybe<List<ChatMessage>> {
        val currentPage = currentChatroomPage.currentPage
        return if (currentPage < currentChatroomPage.pageCount) {
            service.listMessages(activeChatroomId, currentPage + 1, currentChatroomPage.perPage)
                    .compose(ResponseTransformer())
                    .toMaybe()
        } else {
            Maybe.empty() // no more pages available
        }
    }

    fun sendMessage(message: String): Single<ChatMessage> =
            service.createMessage(activeChatroomId, message)
                    .compose(ResponseTransformer())

    fun updateChatName(name: String): Completable =
            service.update(activeChatroomId, name)
                    .compose(ResponseTransformer())
                    .toCompletable()

    private fun createChatsGroupedByDate(chats: List<Chat>): List<Any> {
        val chatsGroupedByDate = ArrayList<Any>()

        val monthDayFormatter = SimpleDateFormat("MMMM d", Locale.ENGLISH)
        val monthDayYearFormatter = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
        val today = Calendar.getInstance()
        val currentDate = Calendar.getInstance()

        var previousFormattedDate: String? = null
        for (chat in chats) {
            // parse date for most recent chat message
            val createdAt = chat.lastChatMessage.createdAt
            val parsedDate = dateFormatter.parse(createdAt)
            currentDate.time = parsedDate

            // create properly formatted date label
            val formattedDate = if (currentDate.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
                if (currentDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                    "Today"
                } else {
                    monthDayFormatter.format(parsedDate)
                }
            } else {
                // dates in previous calendar years include year in label
                monthDayYearFormatter.format(parsedDate)
            }

            if (formattedDate == previousFormattedDate) {
                // date label already added for previous chat
                chatsGroupedByDate.add(chat)
            } else {
                // date label is new, add it to list with chat
                chatsGroupedByDate.add(formattedDate)
                chatsGroupedByDate.add(chat)
                previousFormattedDate = formattedDate
            }
        }

        return chatsGroupedByDate
    }

    private inner class ChatsTransformer: SingleTransformer<ApiResponse<List<Chat>>, List<Any>> {
        override fun apply(upstream: Single<ApiResponse<List<Chat>>>): SingleSource<List<Any>> =
                upstream.doOnSuccess { response -> currentChatPage = response.meta!!.pagination }
                        .compose(ResponseTransformer())
                        .map { createChatsGroupedByDate(it) }
    }
}
