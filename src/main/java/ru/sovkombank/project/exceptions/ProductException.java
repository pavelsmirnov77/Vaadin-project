package ru.sovkombank.project.exceptions;

public class ProductException extends RuntimeException {
    /**
     * Выбрасывает сообщение при неудачном взаимодействии с товаром
     *
     * @param message сообщение об ошибке
     */
    public ProductException(String message) {
        super(message);
    }
}
