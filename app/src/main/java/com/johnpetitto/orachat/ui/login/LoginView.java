package com.johnpetitto.orachat.ui.login;

public interface LoginView {
    void showLoading(boolean show);
    void showLoginFailure();
    void navigateToMain();
}
