package com.johnpetitto.orachat.ui.chats;

import com.johnpetitto.orachat.data.chat.ChatModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;

public class ChatsPresenter {
    private ChatsView view;
    private ChatModel model;
    private Disposable disposable;

    private BiConsumer<List<Object>, Throwable> subscriber = (chatsGroupedByDate, throwable) -> {
        if (chatsGroupedByDate != null) {
            view.displayChatsByDate(chatsGroupedByDate);
        } else {
            view.showError();
        }

        view.showLoading(false);
    };

    public ChatsPresenter(ChatsView view, ChatModel model) {
        this.view = view;
        this.model = model;
    }

    public void onDestroy() {
        closeDisposable();
    }

    public void getAllChatsByDate() {
        view.showLoading(true);

        disposable = model.getAllChatsByDate()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void searchChatsByName(String name) {
        closeDisposable();
        view.showLoading(true);

        disposable = model.searchChatsByName(name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private void closeDisposable() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
