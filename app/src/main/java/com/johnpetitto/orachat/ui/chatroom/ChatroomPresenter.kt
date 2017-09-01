package com.johnpetitto.orachat.ui.chatroom

import com.johnpetitto.orachat.data.chat.ChatModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class ChatroomPresenter(private val view: ChatroomView, private val model: ChatModel) {
    private var disposables = CompositeDisposable()

    fun destroy() = disposables.dispose()

    fun createNewChat(name: String, message: String) {
        disposables.add(model.createNewChat(name, message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view.showNewMessage(it) },
                        { view.showError() }
                )
        )
    }

    fun getInitialMessages(chatId: Long) {
        view.showLoading(true)

        disposables.add(model.getMessagesForChat(chatId)
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { view.showLoading(false) }
                .subscribe(
                        { view.showInitialMessages(it) },
                        { view.showError() }
                )
        )
    }

    fun getMoreMessages() {
        disposables.add(model.loadMoreMessages()
                .onErrorComplete()
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { view.pageLoaded() }
                .subscribe { view.showMoreMessages(it) }
        )
    }

    fun sendMessage(message: String) {
        disposables.add(model.sendMessage(message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view.showNewMessage(it) },
                        { view.showError() }
                )
        )
    }

    fun updateChatName(name: String) {
        disposables.add(model.updateChatName(name)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { view.showError() }
                .subscribe()
        )
    }
}
