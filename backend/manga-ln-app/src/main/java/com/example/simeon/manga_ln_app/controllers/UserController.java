package com.example.simeon.manga_ln_app.controllers;

import com.example.simeon.manga_ln_app.dto.CommentDTO;
import com.example.simeon.manga_ln_app.dto.UserCredentialsDTO;
import com.example.simeon.manga_ln_app.dto.UserDTO;
import com.example.simeon.manga_ln_app.models.CommentBeta;
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

    @PostMapping()
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserCredentialsDTO userCredentialsDTO){
        userService.addUser(userCredentialsDTO);
        return new ResponseEntity<>(userService.getByUsername(userCredentialsDTO.getUsername()), HttpStatus.OK);
    }

    //TODO: TO BE REFACTORED AFTER SPRING SECURITY
    @PostMapping("/comments")
    public ResponseEntity<String> addComment(@Valid @RequestBody CommentBeta commentBeta){
        userService.addComment(commentBeta);
        return new ResponseEntity<>("Comment awaiting approval", HttpStatus.OK);
    }

    //TODO: TO BE ADDED AUTHORIZATION
    @PostMapping("/comments/approve/{id}")
    public ResponseEntity<CommentDTO> approveComment(@PathVariable int id){
        return new ResponseEntity<>(userService.approveComment(id), HttpStatus.OK);
    }
}
