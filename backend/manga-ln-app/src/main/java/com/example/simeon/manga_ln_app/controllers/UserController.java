package com.example.simeon.manga_ln_app.controllers;

import com.example.simeon.manga_ln_app.dto.UserCredentialsDTO;
import com.example.simeon.manga_ln_app.dto.UserDTO;
import com.example.simeon.manga_ln_app.models.Role;
import com.example.simeon.manga_ln_app.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id){
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username){
        return new ResponseEntity<>(userService.getByUsername(username), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> addUser(@Valid @RequestBody UserCredentialsDTO userCredentialsDTO){
        return new ResponseEntity<>(userService.addUser(userCredentialsDTO), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserCredentialsDTO userCredentialsDTO) throws CredentialNotFoundException {
        return new ResponseEntity<>(userService.verify(userCredentialsDTO), HttpStatus.OK);
    }

    @PutMapping("/role/{username}")
    public ResponseEntity<UserDTO> changeRole(@PathVariable String username, @RequestParam Role role){
        return new ResponseEntity<>(userService.changeRole(username, role), HttpStatus.OK);
    }
}
