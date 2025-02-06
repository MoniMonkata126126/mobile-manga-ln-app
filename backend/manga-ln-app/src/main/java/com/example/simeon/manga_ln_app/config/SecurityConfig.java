package com.example.simeon.manga_ln_app.config;

import com.example.simeon.manga_ln_app.filter.JWTFilter;
import com.example.simeon.manga_ln_app.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JWTFilter jwtFilter;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JWTFilter jwtFilter) {
        this.userDetailsService = customUserDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/user/register", "/user/login").permitAll()
                        .requestMatchers("/user", "user/id/**", "user/username/**").hasAnyAuthority("MODERATOR", "ADMIN")
                        .requestMatchers("user/role/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/content/**").hasAnyAuthority("READER", "AUTHOR", "MODERATOR", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/content").hasAuthority("AUTHOR")
                        .requestMatchers("/content/approve/**").hasAnyAuthority("MODERATOR", "ADMIN")
                        .requestMatchers("/chapter").hasAuthority("AUTHOR")
                        .requestMatchers("/chapter/approve/**").hasAnyAuthority("MODERATOR", "ADMIN")
                        .requestMatchers("/comment").hasAnyAuthority("READER", "AUTHOR")
                        .requestMatchers("/comment/approve/**").hasAnyAuthority("MODERATOR", "ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
