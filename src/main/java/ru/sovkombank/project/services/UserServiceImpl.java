package ru.sovkombank.project.services;

import org.springframework.stereotype.Service;
import ru.sovkombank.project.entities.Cart;
import ru.sovkombank.project.entities.User;
import ru.sovkombank.project.exceptions.UserException;
import ru.sovkombank.project.repositories.CartRepository;
import ru.sovkombank.project.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

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
        return user.isPresent();
    }
}
