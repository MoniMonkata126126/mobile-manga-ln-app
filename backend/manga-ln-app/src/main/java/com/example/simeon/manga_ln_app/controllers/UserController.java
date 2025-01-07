package com.example.simeon.manga_ln_app.controllers;

import com.example.simeon.manga_ln_app.models.User;
import com.example.simeon.manga_ln_app.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id){
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserById(@PathVariable String username){
        return new ResponseEntity<>(userService.getByUsername(username), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<User> addUser(@Valid @RequestBody User user){
        userService.addUser(user);
        return new ResponseEntity<>(userService.getByUsername(user.getUsername()), HttpStatus.OK);
    }
}
