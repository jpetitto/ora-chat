package com.johnpetitto.orachat.chats;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class ChatsPresenter {
    private ChatsView view;
    private ChatModel model;
    private Disposable disposable;

    public ChatsPresenter(ChatsView view, ChatModel model) {
        this.view = view;
        this.model = model;
    }

    public void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void getAllChatsByDate() {
        view.showLoading(true);

        disposable = model.getAllChatsByDate()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(chatsGroupedByDate -> {
                    view.displayChatsByDate(chatsGroupedByDate);
                    view.showLoading(false);
                });
    }
}
