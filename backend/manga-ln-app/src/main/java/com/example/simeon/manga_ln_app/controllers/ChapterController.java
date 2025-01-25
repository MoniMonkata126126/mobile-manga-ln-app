package com.example.simeon.manga_ln_app.controllers;

import com.example.simeon.manga_ln_app.service.ChapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/chapters")
public class ChapterController {

    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @PostMapping
    public ResponseEntity<String> uploadFiles(@RequestParam String contentName,
                                              @RequestParam String chapterName,
                                              @RequestPart("data") List<MultipartFile> files) {
        return chapterService.addChapterBeta(contentName, chapterName, files);
    }

    //TODO: ADD CHAPTER BETA APPROVE

    //TODO: ADD GET CHAPTER ( FIGURE OUT AND WRITE LOGIC )

}
