package com.johnpetitto.orachat;

import io.reactivex.Single;
import okhttp3.ResponseBody;

public class UserModel {
    private UserService service;
    private UserPreferences preferences;

    public UserModel(UserService service, UserPreferences preferences) {
        this.service = service;
        this.preferences = preferences;
    }

    public Single<ResponseBody> createUser(String name, String email, String password, String passwordConfirmation) {
        return service.create(new CreateUser(name, email, password, passwordConfirmation));
    }

    public Single<Boolean> loginUser(String email, String password) {
        return service.login(new LoginUser(email, password))
                .doOnSuccess(result -> {
                    retrofit2.Response<ResponseBody> response = result.response();
                    if (response != null) {
                        String token = response.headers().get("Authorization");
                        preferences.setAuthorizationToken(token);
                    }
                })
                .map(result -> !result.isError());
    }

    public Single<ResponseBody> currentUser() {
        return service.currentUser();
    }
}
