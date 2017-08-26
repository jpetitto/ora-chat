package com.johnpetitto.orachat.data.user

import com.johnpetitto.orachat.data.ApiResponse

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserService {
    @POST("users")
    fun create(@Body credentials: UserCredentials): Single<ApiResponse<User>>

    @POST("auth/login")
    fun login(@Body credentials: UserCredentials): Single<Result<ApiResponse<User>>>

    @GET("auth/logout")
    fun logout(): Completable

    @GET("users/current")
    fun read(): Single<ApiResponse<User>>

    @PATCH("users/current")
    fun update(@Body credentials: UserCredentials): Single<ApiResponse<User>>
}
