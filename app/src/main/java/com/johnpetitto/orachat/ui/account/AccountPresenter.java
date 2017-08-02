package com.johnpetitto.orachat.ui.account;

import com.johnpetitto.orachat.data.user.UserModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class AccountPresenter {
    private AccountView view;
    private UserModel model;
    private Disposable disposable;

    public AccountPresenter(AccountView view, UserModel model) {
        this.view = view;
        this.model = model;
    }

    public void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void getCurrentUserInfo() {
        view.showLoading(true);

        disposable = model.getCurrentUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    view.populateAccountInfo(user.getName(), user.getEmail());
                    view.showLoading(false);
                }, throwable -> {
                    view.showError();
                    view.showLoading(false);
                });
    }

    public void updateCurrentUser(String name, String email) {
        view.showLoading(true);

        model.updateCurrentUser(name, email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.showLoading(false), throwable -> {
                    view.showError();
                    view.showLoading(false);
                });
    }

    public void logout() {
        model.logout().onErrorComplete().subscribe();
    }
}
