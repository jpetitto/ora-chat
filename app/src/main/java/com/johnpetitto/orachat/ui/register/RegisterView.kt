package com.johnpetitto.orachat.ui.register

interface RegisterView {
    fun showLoading(show: Boolean)
    fun showRegistrationFailure()
    fun navigateToMain()
}
