package com.johnpetitto.orachat.data

data class ApiResponse<T>(
        val data: T?,
        val message: String?,
        val errors: Map<String, Any>?,
        val meta: Meta?
)
