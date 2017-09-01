package com.johnpetitto.orachat.ui.account

interface AccountView {
    fun showLoading(show: Boolean)
    fun showError()
    fun populateAccountInfo(name: String, email: String)
}
