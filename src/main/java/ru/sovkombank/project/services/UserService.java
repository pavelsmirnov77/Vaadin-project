package ru.sovkombank.project.services;

import ru.sovkombank.project.entities.User;

public interface UserService {
    /**
     * Регистрирует пользователя
     * @param user пользователь
     */
    void signUp(User user);

    /**
     * Авторизирует пользователя
     * @param email электронная почта пользователя
     * @param password пароль пользователя
     * @return true, если авторизация прошла успешно
     */
    boolean signIn(String email, String password);

    /**
     * Получает авторизированного пользователя по email
     *
     * @param email электронная почта пользователя
     * @return пользователя
     */
    User getCurrentUser(String email);

    /**
     * Проверяет авторизирован пользователь или нет
     *
     * @return true, если авторизирован
     */
    boolean isAuthenticated();

    /**
     * Получает авторизированного пользователя
     *
     * @return пользователя
     */
    User getCurrentUser();

    /**
     * выходит из аккаунта
     */
    void logout();
}
