package com.johnpetitto.orachat.ui.login

import com.johnpetitto.orachat.data.user.UserModel

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class LoginPresenter(private val view: LoginView, private val userModel: UserModel) {
    private var disposable: Disposable? = null

    fun onDestroy() = disposable?.dispose()

    fun login(email: String, password: String) {
        view.showLoading(true)

        disposable = userModel.loginUser(email, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view.navigateToMain() },
                        { view.showLoading(false); view.showLoginFailure() }
                )
    }
}
