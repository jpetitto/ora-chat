package com.johnpetitto.orachat.data;

import java.util.Map;

public class ApiResponse<T> {
    private T data;
    private String message;
    private Map<String, Object> errors;
    private Meta meta;

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getErrors() {
        return errors;
    }

    public Meta getMeta() {
        return meta;
    }
}
