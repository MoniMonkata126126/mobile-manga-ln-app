package com.example.simeon.manga_ln_app.controllers;

import com.example.simeon.manga_ln_app.dto.CommentBetaDTO;
import com.example.simeon.manga_ln_app.dto.CommentDTO;
import com.example.simeon.manga_ln_app.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    //TODO: TO BE REFACTORED AFTER SPRING SECURITY
    @PostMapping()
    public ResponseEntity<String> addComment(@Valid @RequestBody CommentBetaDTO commentBetaDTO){
        commentService.addComment(commentBetaDTO);
        return new ResponseEntity<>("Comment awaiting approval", HttpStatus.CREATED);
    }

    //TODO: TO BE ADDED AUTHORIZATION
    @PostMapping("/approve/{id}")
    public ResponseEntity<CommentDTO> approveComment(@PathVariable int id){
        return new ResponseEntity<>(commentService.approveComment(id), HttpStatus.CREATED);
    }
}
