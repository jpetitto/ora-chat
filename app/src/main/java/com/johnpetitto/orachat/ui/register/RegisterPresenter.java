package com.johnpetitto.orachat.ui.register;

import com.johnpetitto.orachat.data.user.UserModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class RegisterPresenter {
    private RegisterView view;
    private UserModel model;
    private Disposable disposable;

    public RegisterPresenter(RegisterView view, UserModel model) {
        this.view = view;
        this.model = model;
    }

    public void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void register(String name, String email, String password, String passwordConfirmation) {
        view.showLoading(true);

        disposable = model.createUser(name, email, password, passwordConfirmation)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.navigateToMain(), throwable -> {
                    view.showLoading(false);
                    view.showRegistrationFailure();
                });
    }
}