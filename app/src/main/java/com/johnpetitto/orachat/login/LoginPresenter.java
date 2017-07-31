package com.johnpetitto.orachat.login;

import com.johnpetitto.orachat.user.UserModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class LoginPresenter {
    private LoginView view;
    private UserModel userModel;
    private Disposable disposable;

    public LoginPresenter(LoginView view, UserModel userModel) {
        this.view = view;
        this.userModel = userModel;
    }

    public void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void login(String email, String password) {
        view.showLoading(true);

        disposable = userModel.loginUser(email, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.navigateToMain(), throwable -> {
                    view.showLoading(false);
                    view.showLoginFailure();
                });
    }
}
