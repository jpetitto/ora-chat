package com.johnpetitto.orachat;

import android.app.Application;

import com.johnpetitto.orachat.data.chat.ChatModel;
import com.johnpetitto.orachat.data.chat.ChatService;
import com.johnpetitto.orachat.data.user.UserModel;
import com.johnpetitto.orachat.data.user.UserPreferences;
import com.johnpetitto.orachat.data.user.UserService;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class OraChatApplication extends Application {
    private Retrofit retrofit;
    private UserPreferences userPreferences;
    private UserModel userModel;
    private ChatModel chatModel;

    @Override
    public void onCreate() {
        super.onCreate();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    String token = userPreferences.getAuthorizationToken();
                    if (token == null) {
                        // no authorization token yet, proceed with original call
                        return chain.proceed(originalRequest);
                    }

                    // add authorization header
                    Request request = originalRequest.newBuilder()
                            .header("Authorization", token)
                            .build();

                    return chain.proceed(request);
                })
                .build();

        retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl("https://private-93240c-oracodechallenge.apiary-mock.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build();

        userPreferences = new UserPreferences(this);
        userModel = new UserModel(retrofit.create(UserService.class), userPreferences);

        chatModel = new ChatModel(retrofit.create(ChatService.class));
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public ChatModel getChatModel() {
        return chatModel;
    }
}
