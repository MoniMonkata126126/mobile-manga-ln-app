package com.example.simeon.manga_ln_app.service;

import com.example.simeon.manga_ln_app.models.User;
import com.example.simeon.manga_ln_app.models.UserPrincipal;
import com.example.simeon.manga_ln_app.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " does not exist!"));
        return new UserPrincipal(user);
    }
}
