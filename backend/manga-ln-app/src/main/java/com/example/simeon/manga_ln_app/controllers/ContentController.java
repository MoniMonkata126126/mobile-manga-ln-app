package com.example.simeon.manga_ln_app.controllers;

import com.example.simeon.manga_ln_app.dto.ChapterInfoDTO;
import com.example.simeon.manga_ln_app.dto.ContentInputDTO;
import com.example.simeon.manga_ln_app.dto.ContentBetaDTO;
import com.example.simeon.manga_ln_app.dto.ContentDTO;
import com.example.simeon.manga_ln_app.service.ContentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public ResponseEntity<List<ContentDTO>> searchContentByQuery(@RequestParam String q, @RequestParam String type){
        return new ResponseEntity<>(contentService.searchByQuery(q, type), HttpStatus.OK);
    }

    @GetMapping("/type")
    public ResponseEntity<List<ContentDTO>> searchByType(@RequestParam String type){
        return new ResponseEntity<>(contentService.searchByType(type), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ChapterInfoDTO>> getChaptersPerContent(@PathVariable int id){
        return new ResponseEntity<>(contentService.getAllChaptersForContent(id), HttpStatus.OK);
    }
}
