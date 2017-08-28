package com.johnpetitto.orachat.data.user

import com.johnpetitto.orachat.data.ResponseTransformer
import io.reactivex.Completable
import io.reactivex.Single

class UserModel(private val service: UserService, private val preferences: UserPreferences) {
    private var currentUser: User? = null

    val isAuthorized: Boolean
        get() = preferences.authorizationToken != null

    fun createUser(name: String, email: String, password: String, passwordConfirmation: String): Completable =
            service.create(UserCredentials(name, email, password, passwordConfirmation))
                    .compose(ResponseTransformer())
                    .flatMapCompletable { loginUser(email, password) }

    fun loginUser(email: String, password: String): Completable =
            service.login(UserCredentials(email, password))
                    .flatMap { result ->
                        val response = result.response()
                        if (response != null) {
                            // obtain authorization token from response header
                            val token = response.headers().get("Authorization")
                            preferences.authorizationToken = token

                            // route response depending on success/failure
                            val responseBody = response.body()
                            if (responseBody != null)
                                Single.just(responseBody)
                            else
                                Single.error(Throwable(response.message()))

                        } else {
                            Single.error(Throwable())
                        }
                    }
                    .compose(ResponseTransformer())
                    .toCompletable()

    fun logout(): Completable {
        preferences.authorizationToken = null
        currentUser = null
        return service.logout()
    }

    fun getCurrentUser(): Single<User> =
            if (currentUser != null)
                Single.just(currentUser)
            else
                service.read().compose(ResponseTransformer())

    fun updateCurrentUser(name: String, email: String): Completable =
            service.update(UserCredentials(name, email))
                    .compose(ResponseTransformer())
                    .doOnSuccess { currentUser = it }
                    .toCompletable()

}
