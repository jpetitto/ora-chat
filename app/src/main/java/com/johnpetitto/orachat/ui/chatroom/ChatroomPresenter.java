package com.johnpetitto.orachat.ui.chatroom;

import com.johnpetitto.orachat.data.chat.ChatModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class ChatroomPresenter {
    private ChatroomView view;
    private ChatModel model;
    private CompositeDisposable disposables = new CompositeDisposable();

    public ChatroomPresenter(ChatroomView view, ChatModel model) {
        this.view = view;
        this.model = model;
    }

    public void onDestroy() {
        disposables.dispose();
    }

    public void createNewChat(String name, String message) {
        disposables.add(model.createNewChat(name, message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(chatMessage -> view.showNewMessage(chatMessage),
                        throwable -> view.showError())
        );
    }

    public void getInitialMessages(long chatId) {
        view.showLoading(true);

        disposables.add(model.getMessagesForChat(chatId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(chatMessages -> {
                    view.showInitialMessages(chatMessages);
                    view.showLoading(false);
                }, throwable -> {
                    view.showError();
                    view.showLoading(false);
                })
        );
    }

    public void getMoreMessages() {
        disposables.add(model.loadMoreMessages()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messages -> {
                    view.showMoreMessages(messages);
                    view.pageLoaded();
                }, throwable -> view.pageLoaded(), () -> view.pageLoaded())
        );
    }

    public void sendMessage(String message) {
        disposables.add(model.sendMessage(message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(chatMessage -> view.showNewMessage(chatMessage),
                        throwable -> view.showError())
        );
    }

    public void updateChatName(String name) {
        model.updateChatName(name)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> view.showError())
                .subscribe();
    }
}
