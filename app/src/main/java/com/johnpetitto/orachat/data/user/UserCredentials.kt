package com.johnpetitto.orachat.data.user

import com.google.gson.annotations.SerializedName

data class UserCredentials(
        val name: String? = null,
        val email: String,
        val password: String,
        @SerializedName("password_confirmation") val passwordConfirmation: String? = null
) {
    constructor(email: String, password: String) : this(null, email, password, null)
}
