package com.johnpetitto.orachat.ui.register

import com.johnpetitto.orachat.data.user.UserModel

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class RegisterPresenter(private val view: RegisterView, private val model: UserModel) {
    private var disposable: Disposable? = null

    fun onDestroy() = disposable?.dispose()

    fun register(name: String, email: String, password: String, passwordConfirmation: String) {
        view.showLoading(true)

        disposable = model.createUser(name, email, password, passwordConfirmation)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view.navigateToMain() },
                        { view.showLoading(false); view.showRegistrationFailure() }
                )
    }
}
