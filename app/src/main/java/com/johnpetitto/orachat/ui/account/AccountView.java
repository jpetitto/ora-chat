package com.johnpetitto.orachat.ui.account;

public interface AccountView {
    void showLoading(boolean show);
    void showError();
    void populateAccountInfo(String name, String email);
}
