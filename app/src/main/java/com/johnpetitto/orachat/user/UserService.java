package com.johnpetitto.orachat.user;

import com.johnpetitto.orachat.login.LoginUser;
import com.johnpetitto.orachat.register.CreateUser;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {
    @POST("users")
    Completable create(@Body CreateUser createUser);

    @POST("auth/login")
    Single<Result<ResponseBody>> login(@Body LoginUser loginUser);

    @GET("users/current")
    Single<ResponseBody> currentUser();
}
