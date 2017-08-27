package com.johnpetitto.orachat

import android.app.Application
import com.johnpetitto.orachat.data.chat.ChatModel
import com.johnpetitto.orachat.data.chat.ChatService
import com.johnpetitto.orachat.data.user.UserModel
import com.johnpetitto.orachat.data.user.UserPreferences
import com.johnpetitto.orachat.data.user.UserService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class OraChatApplication : Application() {
    private lateinit var userPreferences: UserPreferences
    lateinit var userModel: UserModel
        private set
    lateinit var chatModel: ChatModel
        private set

    override fun onCreate() {
        super.onCreate()

        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor { chain ->
                    var request = chain.request()

                    val token = userPreferences.authorizationToken
                    if (token != null) {
                        request = request.newBuilder()
                                .header("Authorization", token)
                                .build()
                    }

                    chain.proceed(request)
                }
                .build()

        val retrofit = Retrofit.Builder()
                .client(httpClient)
                .baseUrl("https://private-93240c-oracodechallenge.apiary-mock.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build()

        userPreferences = UserPreferences(this)
        userModel = UserModel(retrofit.create(UserService::class.java), userPreferences)

        chatModel = ChatModel(retrofit.create(ChatService::class.java))
    }
}
