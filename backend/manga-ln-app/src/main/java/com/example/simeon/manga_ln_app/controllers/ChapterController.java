package com.example.simeon.manga_ln_app.controllers;

import com.example.simeon.manga_ln_app.dto.ChapterDTO;
import com.example.simeon.manga_ln_app.service.ChapterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/chapter")
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

    @PostMapping("/approve/{id}")
    public ResponseEntity<ChapterDTO> approveChapterBeta(@PathVariable int id){
        return new ResponseEntity<>(chapterService.approveChapterBeta(id), HttpStatus.CREATED);
    }

    //TODO: ADD GET CHAPTER ( FIGURE OUT AND WRITE LOGIC )

}
