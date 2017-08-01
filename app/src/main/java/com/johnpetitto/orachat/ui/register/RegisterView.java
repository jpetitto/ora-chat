package com.johnpetitto.orachat.ui.register;

public interface RegisterView {
    void showLoading(boolean show);
    void showRegistrationFailure();
    void navigateToMain();
}
