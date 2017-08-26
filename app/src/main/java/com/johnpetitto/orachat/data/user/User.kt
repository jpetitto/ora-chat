package com.johnpetitto.orachat.data.user

data class User(val id: Long?, val name: String, val email: String?) {
    constructor(name: String) : this(null, name, null)
}
