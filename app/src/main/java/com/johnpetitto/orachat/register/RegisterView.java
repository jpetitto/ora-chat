package com.johnpetitto.orachat.register;

public interface RegisterView {
    void showProgress(boolean show);
    void showRegistrationFailure();
    void navigateToMain();
}
