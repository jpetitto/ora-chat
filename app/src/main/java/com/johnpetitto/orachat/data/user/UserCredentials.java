package com.johnpetitto.orachat.data.user;

import com.google.gson.annotations.SerializedName;

public class UserCredentials {
    private String name;
    private String email;
    private String password;
    private @SerializedName("password_confirmation") String passwordConfirmation;

    public UserCredentials(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserCredentials(String name, String email, String password, String passwordConfirmation) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }
}
