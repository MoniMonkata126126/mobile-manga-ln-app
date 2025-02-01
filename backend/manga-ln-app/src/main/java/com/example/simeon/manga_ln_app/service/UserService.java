package com.example.simeon.manga_ln_app.service;

import com.example.simeon.manga_ln_app.dto.UserCredentialsDTO;
import com.example.simeon.manga_ln_app.mapper.UserMapper;
import com.example.simeon.manga_ln_app.models.Role;
import com.example.simeon.manga_ln_app.models.User;
import com.example.simeon.manga_ln_app.repository.UserRepository;
import com.example.simeon.manga_ln_app.dto.UserDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       AuthenticationManager authenticationManager,
                       JWTService jwtService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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
    public UserDTO addUser(@Valid UserCredentialsDTO userCredentialsDTO) {
        if (userRepository.findByUsername(userCredentialsDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User with username " + userCredentialsDTO.getUsername() + " already exists!");
        }
        User user = userMapper.convertCredentialsToUser(userCredentialsDTO);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userMapper.convertToDTO(userRepository.save(user));
    }

    @Transactional
    public UserDTO getByUsername(@NotBlank String username) {
        return userMapper.convertToDTO(userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " does not exist!")));
    }

    public String verify(UserCredentialsDTO userCredentialsDTO) throws CredentialNotFoundException {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userCredentialsDTO.getUsername(), userCredentialsDTO.getPassword()
                        )
                );

        if(authentication.isAuthenticated()){
            return jwtService.generateToken(userCredentialsDTO.getUsername());
        }
        throw new CredentialNotFoundException("Invalid credentials!");
    }

    @Transactional
    public UserDTO changeRole(String username, Role role) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User with username: " + username + " not found!"
                ));
        user.setRole(role);
        return userMapper.convertToDTO(user);
    }
}