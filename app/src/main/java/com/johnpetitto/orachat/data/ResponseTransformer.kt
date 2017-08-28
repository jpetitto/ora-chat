package com.johnpetitto.orachat.data

import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer

class ResponseTransformer<T>: SingleTransformer<ApiResponse<T>, T> {
    override fun apply(upstream: Single<ApiResponse<T>>): SingleSource<T> =
            upstream.flatMap { (data, errorMessage) ->
                if (data != null) {
                    Single.just(data)
                } else {
                    Single.error(Throwable(errorMessage))
                }
            }
}
