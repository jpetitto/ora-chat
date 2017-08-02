package com.johnpetitto.orachat.data.user;

import com.google.gson.annotations.SerializedName;

public class CreateUser {
    private String name;
    private String email;
    private String password;
    private @SerializedName("password_confirmation") String passwordConfirmation;

    public CreateUser(String name, String email, String password, String passwordConfirmation) {
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
