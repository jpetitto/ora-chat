package com.johnpetitto.orachat.login;

public interface LoginView {
    void showProgress(boolean show);
    void showLoginFailure();
    void navigateToMain();
}
