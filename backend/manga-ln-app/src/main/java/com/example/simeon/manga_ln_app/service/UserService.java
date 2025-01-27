package com.example.simeon.manga_ln_app.service;

import com.example.simeon.manga_ln_app.dto.UserCredentialsDTO;
import com.example.simeon.manga_ln_app.mapper.UserMapper;
import com.example.simeon.manga_ln_app.models.User;
import com.example.simeon.manga_ln_app.repository.UserRepository;
import com.example.simeon.manga_ln_app.dto.UserDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::convertToDTO)
                .toList();
    }

    @Transactional
    public UserDTO getById(@NotEmpty int id) {
        return userMapper.convertToDTO(userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " does not exist!")));
    }

    @Transactional
    public void addUser(@Valid UserCredentialsDTO userCredentialsDTO) {
        if (userRepository.findByUsername(userCredentialsDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User with username " + userCredentialsDTO.getUsername() + " already exists!");
        }
        User user = userMapper.convertCredentialsToUser(userCredentialsDTO);
        userRepository.save(user);
    }

    public UserCredentialsDTO getCredentials(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username: " + username + " not found!"));
        return userMapper.convertUserToCredentials(user);
    }

    @Transactional
    public UserDTO getByUsername(@NotBlank String username) {
        return userMapper.convertToDTO(userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " does not exist!")));
    }
}