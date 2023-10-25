package ru.sovkombank.project.services;

import org.springframework.stereotype.Service;
import ru.sovkombank.project.entities.User;
import ru.sovkombank.project.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void signUp(User user) {
        userRepository.save(user);
    }
}
