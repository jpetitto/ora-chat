package com.johnpetitto.orachat.ui.account

import com.johnpetitto.orachat.data.user.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class AccountPresenter(private val view: AccountView, private val model: UserModel) {
    private var disposable: Disposable? = null

    fun destroy() = disposable?.dispose()

    fun getCurrentUserInfo() {
        view.showLoading(true)

        disposable = model.getCurrentUser()
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { view.showLoading(false) }
                .subscribe(
                        { (_, name, email) -> view.populateAccountInfo(name, email) },
                        { view.showError() }
                )
    }

    fun updateCurrentUser(name: String, email: String) {
        view.showLoading(true)

        disposable = model.updateCurrentUser(name, email)
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { view.showLoading(false) }
                .subscribe(
                        { view.populateAccountInfo(name, email) },
                        { view.showError() }
                )
    }

    fun logout() = model.logout().onErrorComplete().subscribe()
}
