package com.johnpetitto.orachat.ui.chats

import com.johnpetitto.orachat.data.chat.ChatModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class ChatsPresenter(private val view: ChatsView, private val model: ChatModel) {
    private var disposable: Disposable? = null

    fun destroy() = disposable?.dispose()

    fun getAllChatsByDate() {
        view.showLoading(true)

        disposable = model.getAllChatsByDate()
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { view.showLoading(false) }
                .subscribe(
                        { view.displayChatData(it) },
                        { view.showError() }
                )
    }

    fun searchChatsByName(name: String) {
        destroy()
        view.showLoading(true)

        disposable = model.searchChatsByName(name)
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { view.showLoading(false) }
                .subscribe(
                        { view.displayChatData(it) },
                        { view.showError() }
                )
    }

    fun loadMoreChats() {
        destroy()

        disposable = model.loadMoreChats()
                .onErrorComplete()
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { view.pageLoaded() }
                .subscribe { view.showMoreChatData(it) }
    }
}
