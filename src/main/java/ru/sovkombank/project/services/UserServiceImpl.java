package ru.sovkombank.project.services;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sovkombank.project.entities.Cart;
import ru.sovkombank.project.entities.User;
import ru.sovkombank.project.exceptions.UserException;
import ru.sovkombank.project.repositories.CartRepository;
import ru.sovkombank.project.repositories.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    private final Map<String, User> loggedInUsers = new HashMap<>();

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public void signUp(User user) {
        Optional<User> existingUser = userRepository.findUserByEmail(user.getEmail());
        Cart cart = new Cart();
        cart.setUser(user);

        if (existingUser.isPresent()) {
            throw new UserException("Пользователь с таким email уже существует");
        }

        userRepository.save(user);
        cartRepository.save(cart);
    }

    @Override
    public boolean signIn(String email, String password) {
        Optional<User> user = userRepository.findUserByEmail(email);

        if (user.isPresent()) {
            User authenticatedUser = user.get();
            loggedInUsers.put(email, authenticatedUser);
            VaadinSession session = VaadinSession.getCurrent();
            session.setAttribute("userEmail", email);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public User getCurrentUser(String email) {
        return null;
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
            return userRepository.findUserByEmail(email).orElse(null);
        }

        return null;
    }

    @Override
    public void logout() {
        VaadinSession session = VaadinSession.getCurrent();
        String email = (String) session.getAttribute("userEmail");
        loggedInUsers.remove(email);
        session.setAttribute("userEmail", null);
    }
}
