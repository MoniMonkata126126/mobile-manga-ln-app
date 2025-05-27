package com.example.simeon.manga_ln_app.service;

import com.example.simeon.manga_ln_app.dto.UserCredentialsDTO;
import com.example.simeon.manga_ln_app.mapper.UserMapper;
import com.example.simeon.manga_ln_app.models.Content;
import com.example.simeon.manga_ln_app.models.Role;
import com.example.simeon.manga_ln_app.models.User;
import com.example.simeon.manga_ln_app.repository.UserRepository;
import com.example.simeon.manga_ln_app.dto.UserDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
    public String addUser(@Valid UserCredentialsDTO userCredentialsDTO) {
        if (userRepository.findByUsername(userCredentialsDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User with username " + userCredentialsDTO.getUsername() + " already exists!");
        }
        User user = userMapper.convertCredentialsToUser(userCredentialsDTO);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return jwtService.generateToken(userRepository.save(user).getUsername());
    }

    @Transactional
    public UserDTO getByUsername(@NotBlank String username) {
        return userMapper.convertToDTO(userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " does not exist!")));
    }

    public Map<String, String> verify(UserCredentialsDTO userCredentialsDTO) throws CredentialNotFoundException {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userCredentialsDTO.getUsername(), userCredentialsDTO.getPassword()
                        )
                );

        if(authentication.isAuthenticated()){
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("username", userCredentialsDTO.getUsername());
            responseMap.put("role", Role.valueOf(authentication.getAuthorities().iterator().next().getAuthority()).name());
            responseMap.put("token", jwtService.generateToken(userCredentialsDTO.getUsername()));
            log.info(responseMap.toString());
            return responseMap;
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

    @Transactional
    public List<Content> findContentByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException( "User with username: " + username + " not found!" )
        );
        return user.getCreatedWorks();
    }
}