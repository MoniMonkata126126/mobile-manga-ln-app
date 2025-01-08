package com.example.simeon.manga_ln_app.service;

import com.example.simeon.manga_ln_app.models.User;
import com.example.simeon.manga_ln_app.repository.UserRepository;
import com.example.simeon.manga_ln_app.dto.UserDTO;
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

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        return dto;
    }

    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public UserDTO getById(@NotEmpty int id) {
        return convertToDTO(userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " does not exist!")));
    }

    public void addUser(@Valid User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User with username " + user.getUsername() + " already exists!");
        }
        userRepository.save(user);
    }

    public UserDTO getByUsername(@NotBlank String username) {
        return convertToDTO(userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " does not exist!")));
    }
}