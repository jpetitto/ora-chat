package com.johnpetitto.orachat.ui.login

interface LoginView {
    fun showLoading(show: Boolean)
    fun showLoginFailure()
    fun navigateToMain()
}
