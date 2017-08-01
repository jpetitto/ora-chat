package com.johnpetitto.orachat.data;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.NonNull;

public class ResponseTransformer<T> implements SingleTransformer<ApiResponse<T>, T> {
    @Override
    public SingleSource<T> apply(@NonNull Single<ApiResponse<T>> upstream) {
        return upstream.flatMap(response -> {
            String errorMessage = response.getMessage();
            if (errorMessage != null) {
                return Single.error(new Throwable(errorMessage));
            } else {
                return Single.just(response.getData());
            }
        });
    }
}
