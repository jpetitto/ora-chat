package com.johnpetitto.orachat.data.user;

import com.johnpetitto.orachat.data.ApiResponse;

import io.reactivex.Single;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {
    @POST("users")
    Single<ApiResponse<User>> create(@Body CreateUser createUser);

    @POST("auth/login")
    Single<Result<ApiResponse<User>>> login(@Body LoginUser loginUser);

    @GET("users/current")
    Single<ApiResponse<User>> currentUser();
}
