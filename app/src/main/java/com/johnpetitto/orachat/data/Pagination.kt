package com.johnpetitto.orachat.data

import com.google.gson.annotations.SerializedName

data class Pagination(
        @SerializedName("current_page") val currentPage: Int,
        @SerializedName("per_page") val perPage: Int,
        @SerializedName("page_count") val pageCount: Int,
        @SerializedName("total_count") val totalCount: Int
)
