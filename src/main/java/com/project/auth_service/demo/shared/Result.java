package com.project.auth_service.demo.shared;

import java.util.List;

public final class Result<T> {

    private final T value;
    private final List<String> errors;

    private Result(T value, List<String> errors) {
        this.value = value;
        this.errors = errors;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, List.of());
    }

    public static <T> Result<T> failure(String error) {
        return new Result<>(null, List.of(error));
    }

    public static <T> Result<T> failure(List<String> errors) {
        return new Result<>(null, List.copyOf(errors));
    }

    public boolean isSuccess() {
        return errors.isEmpty();
    }

    public boolean isFailure() {
        return !isSuccess();
    }

    public T getValue() {
        if (isFailure()) throw new IllegalStateException("Cannot get the value of a failed result");
        return value;
    }

    public List<String> getErrors() {
        return errors;
    }
}
