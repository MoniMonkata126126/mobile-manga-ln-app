package com.example.simeon.manga_ln_app.service;

import com.example.simeon.manga_ln_app.models.User;
import com.example.simeon.manga_ln_app.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getById(@NotEmpty int id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " does not exist!"));
    }

    public void addUser(@Valid User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User with username " + user.getUsername() + " already exists!");
        }
        userRepository.save(user);
    }

    public User getByUsername(@NotBlank String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " does not exist!"));
    }
}