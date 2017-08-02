package com.johnpetitto.orachat.data.user;

import com.johnpetitto.orachat.data.ApiResponse;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface UserService {
    @POST("users")
    Single<ApiResponse<User>> create(@Body UserCredentials credentials);

    @POST("auth/login")
    Single<Result<ApiResponse<User>>> login(@Body UserCredentials credentials);

    @GET("auth/logout")
    Completable logout();

    @GET("users/current")
    Single<ApiResponse<User>> read();

    @PATCH("users/current")
    Single<ApiResponse<User>> update(@Body UserCredentials credentials);
}
