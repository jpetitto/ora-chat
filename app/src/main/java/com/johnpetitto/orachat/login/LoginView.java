package com.johnpetitto.orachat.login;

public interface LoginView {
    void showLoading(boolean show);
    void showLoginFailure();
    void navigateToMain();
}
