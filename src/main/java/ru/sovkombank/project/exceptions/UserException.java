package ru.sovkombank.project.exceptions;

public class UserException extends RuntimeException {
    /**
     * Выбрасывает сообщение при неудачном взаимодействии с пользователем
     *
     * @param message сообщение об ошибке
     */
    public UserException(String message) {
        super(message);
    }
}
