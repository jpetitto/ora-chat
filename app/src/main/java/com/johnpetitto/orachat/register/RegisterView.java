package com.johnpetitto.orachat.register;

public interface RegisterView {
    void showLoading(boolean show);
    void showRegistrationFailure();
    void navigateToMain();
}
