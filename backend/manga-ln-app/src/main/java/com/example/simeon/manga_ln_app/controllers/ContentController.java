package com.example.simeon.manga_ln_app.controllers;

import com.example.simeon.manga_ln_app.dto.ContentInputDTO;
import com.example.simeon.manga_ln_app.dto.ContentBetaDTO;
import com.example.simeon.manga_ln_app.dto.ContentDTO;
import com.example.simeon.manga_ln_app.models.Content;
import com.example.simeon.manga_ln_app.models.ContentBeta;
import com.example.simeon.manga_ln_app.service.ContentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {
    private final ContentService contentService;

    public ContentController(ContentService contentService){
        this.contentService = contentService;
    }

    @PostMapping
    public ResponseEntity<ContentBetaDTO> addContentBeta(@Valid @RequestBody ContentInputDTO contentInputDTO) {
        return new ResponseEntity<>(contentService.addContentBeta(contentInputDTO), HttpStatus.CREATED);
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<ContentDTO> approveContentBeta(@PathVariable int id) {
        return new ResponseEntity<>(contentService.approveContentBeta(id), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<ContentDTO>> searchContentByQuery(@RequestParam String q){
        return new ResponseEntity<>(contentService.searchByQuery(q), HttpStatus.OK);
    }

    //TODO: SEARCH BY CONTENT TYPE
}
