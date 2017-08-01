package com.johnpetitto.orachat.chats;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChatService {
    @GET("chats")
    Single<ResponseBody> list(@Query("name") String name, @Query("page") int page, @Query("limit") int limit);
}
