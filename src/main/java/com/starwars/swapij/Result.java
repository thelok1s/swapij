package com.starwars.swapij;

/**
 * Обобщенный класс для представления результата операции.
 *
 * @param <T> тип данных результата.
 */
public class Result<T> {
    private final T data;
    private final String error;
    private final boolean success;

    /**
     * Конструктор успешного результата.
     *
     * @param data данные результата.
     */
    public Result(T data) {
        this.data = data;
        this.error = null;
        this.success = true;
    }

    /**
     * Конструктор результата ошибки.
     *
     * @param error текст ошибки.
     */
    public Result(String error) {
        this.data = null;
        this.error = error;
        this.success = false;
    }

    /** @return данные результата. */
    public T getData() {
        return data;
    }

    /** @return текст ошибки. */
    public String getError() {
        return error;
    }

    /** @return true, если операция выполнена успешно, иначе false. */
    public boolean isSuccess() {
        return success;
    }
}
