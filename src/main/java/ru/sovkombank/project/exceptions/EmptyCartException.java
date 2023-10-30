package ru.sovkombank.project.exceptions;

public class EmptyCartException extends RuntimeException {
    /**
     * Выбрасывает сообщение при пустой корзине
     *
     * @param message сообщение об ошибке
     */
    public EmptyCartException(String message) {
        super(message);
    }
}
