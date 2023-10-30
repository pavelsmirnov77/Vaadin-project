package ru.sovkombank.project.services;

import com.vaadin.flow.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sovkombank.project.entities.Cart;
import ru.sovkombank.project.entities.User;
import ru.sovkombank.project.exceptions.UserException;
import ru.sovkombank.project.repositories.CartRepository;
import ru.sovkombank.project.repositories.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    private final Map<String, User> loggedInUsers = new HashMap<>();

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CartRepository cartRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void signUp(User user) {
        Optional<User> existingUser = userRepository.findUserByEmail(user.getEmail());
        Cart cart = new Cart();
        cart.setUser(user);

        if (existingUser.isPresent()) {
            throw new UserException("Пользователь с таким email уже существует");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Создаем пользователя");
        userRepository.save(user);
        log.info("Создаем корзину пользователя");
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public boolean signIn(String email, String password) {
        Optional<User> user = userRepository.findUserByEmail(email);

        if (user.isPresent()) {
            User authenticatedUser = user.get();
            if (passwordEncoder.matches(password, authenticatedUser.getPassword())) {
                loggedInUsers.put(email, authenticatedUser);
                VaadinSession session = VaadinSession.getCurrent();
                session.setAttribute("userEmail", email);
                log.info("Входим в аккаунт пользователя с id {}", user.get().getId());
                return true;
            }
        }

        return false;
    }

    @Override
    public User getCurrentUser(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        log.info("Получаем авторизированного пользователя с email: {}", email);
        return user.orElse(null);
    }

    @Override
    public boolean isAuthenticated() {
        VaadinSession session = VaadinSession.getCurrent();
        String email = (String) session.getAttribute("userEmail");
        return email != null && loggedInUsers.containsKey(email);
    }

    @Override
    public User getCurrentUser() {
        VaadinSession session = VaadinSession.getCurrent();
        String email = (String) session.getAttribute("userEmail");

        if (email != null) {
            log.info("Получаем авторизированного пользователя");
            return userRepository.findUserByEmail(email).orElse(null);
        }

        return null;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        log.info("Обновляем информацию о пользователе");
        userRepository.save(user);
    }

    @Override
    public void logout() {
        VaadinSession session = VaadinSession.getCurrent();
        String email = (String) session.getAttribute("userEmail");
        log.info("Выходим из аккаунта");
        loggedInUsers.remove(email);
        session.setAttribute("userEmail", null);
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        log.info("Удаляем пользователя с id {}", userId);
        userRepository.deleteById(userId);
    }
}
