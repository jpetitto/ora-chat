package com.johnpetitto.orachat.data.user;

import com.johnpetitto.orachat.data.ApiResponse;
import com.johnpetitto.orachat.data.ResponseTransformer;

import io.reactivex.Completable;
import io.reactivex.Single;

public class UserModel {
    private UserService service;
    private UserPreferences preferences;
    private User currentUser;

    public UserModel(UserService service, UserPreferences preferences) {
        this.service = service;
        this.preferences = preferences;
    }

    public Completable createUser(String name, String email, String password, String passwordConfirmation) {
        return service.create(new UserCredentials(name, email, password, passwordConfirmation))
                .flatMapCompletable(response -> loginUser(email, password));
    }

    public Completable loginUser(String email, String password) {
        return service.login(new UserCredentials(email, password))
                .doOnSuccess(result -> {
                    retrofit2.Response<ApiResponse<User>> response = result.response();
                    if (response != null) {
                        String token = response.headers().get("Authorization");
                        preferences.setAuthorizationToken(token);
                    }
                })
                .flatMap(result -> {
                    retrofit2.Response<ApiResponse<User>> response = result.response();
                    if (response != null) {
                        // obtain authorization token from response header
                        String token = response.headers().get("Authorization");
                        preferences.setAuthorizationToken(token);

                        // route response depending on success/failure
                        ApiResponse<User> responseBody = response.body();
                        if (responseBody != null) {
                            return Single.just(responseBody);
                        } else {
                            return Single.error(new Throwable(response.message()));
                        }
                    }

                    return Single.error(new Throwable()); // this should never happen
                })
                .compose(new ResponseTransformer<>())
                .toCompletable();
    }

    public Completable logout() {
        preferences.setAuthorizationToken(null);
        currentUser = null;
        return service.logout();
    }

    public Single<User> getCurrentUser() {
        if (currentUser != null) {
            return Single.just(currentUser);
        } else {
            return service.read().compose(new ResponseTransformer<>());
        }
    }

    public Completable updateCurrentUser(String name, String email) {
        return service.update(new UserCredentials(name, email))
                .compose(new ResponseTransformer<>())
                .doOnSuccess(user -> currentUser = user)
                .toCompletable();
    }
}
