package com.example.simeon.manga_ln_app.service;

import com.example.simeon.manga_ln_app.dto.*;
import com.example.simeon.manga_ln_app.exceptions.SendRequestException;
import com.example.simeon.manga_ln_app.mapper.ChapterMapper;
import com.example.simeon.manga_ln_app.models.Chapter;
import com.example.simeon.manga_ln_app.models.ChapterBeta;
import com.example.simeon.manga_ln_app.models.ChapterContent;
import com.example.simeon.manga_ln_app.models.Content;
import com.example.simeon.manga_ln_app.repository.ChapterBetaRepository;
import com.example.simeon.manga_ln_app.repository.ChapterRepository;
import com.example.simeon.manga_ln_app.repository.ContentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ChapterService {
    @Value("${storage.bucket.url}")
    private String baseStorageURL;
    @Value("${storage.api.key}")
    private String apiKey;
    private final ContentRepository contentRepository;
    private final ChapterBetaRepository chapterBetaRepository;
    private final ChapterRepository chapterRepository;
    private final ChapterMapper chapterMapper;

    public ChapterService(ContentRepository contentRepository,
                          ChapterBetaRepository chapterBetaRepository,
                          ChapterRepository chapterRepository,
                          ChapterMapper chapterMapper) {
        this.contentRepository = contentRepository;
        this.chapterBetaRepository = chapterBetaRepository;
        this.chapterRepository = chapterRepository;
        this.chapterMapper = chapterMapper;
    }


    private ResponseEntity<String> sendPutRequest(MultipartFile file, String uploadUrl, HttpHeaders headers){
        try {

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.exchange(uploadUrl, HttpMethod.PUT, requestEntity, String.class);
        }
        catch (Exception e){
            throw new SendRequestException(e.getMessage());
        }
    }

    private ResponseEntity<MultipartFile> sendGetRequest(String uploadUrl, HttpHeaders headers){
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(uploadUrl, HttpMethod.GET, requestEntity, MultipartFile.class);
    }

    @Transactional
    public ResponseEntity<String> addChapterBeta(String contentName, String chapterName, List<MultipartFile> multipartFiles){
        StringBuilder responseMessage = new StringBuilder();

        Content content = this.contentRepository.findByName(contentName)
                .orElseThrow(() -> new IllegalArgumentException("Content with name " + contentName + " not found!"));

        Optional<List<ChapterBeta>> checkChapterBetaList = this.chapterBetaRepository.findByName(chapterName);
        if(checkChapterBetaList.isPresent()){
            for( ChapterBeta chapterBeta : checkChapterBetaList.get()){
                if(chapterBeta.getContent().getName().equals(contentName))
                    throw new IllegalArgumentException("Chapter with name: "
                            + chapterName + " from content with name: "
                            + contentName + " already exists!");
            }
        }

        Optional<List<Chapter>> checkChapterList = this.chapterRepository.findByName(chapterName);
        if(checkChapterList.isPresent()){
            for( Chapter chapter : checkChapterList.get()){
                if(chapter.getContent().getName().equals(contentName))
                    throw new IllegalArgumentException("Chapter with name: "
                            + chapterName + " from content with name: "
                            + contentName + " already exists!");
            }
        }

        ChapterBeta chapterBeta = new ChapterBeta();
        chapterBeta.setContent(content);
        chapterBeta.setName(chapterName);
        chapterBeta.setChapterContentList(new ArrayList<>());

        int fileOrderCount = 0;

        for (MultipartFile file : multipartFiles) {
            fileOrderCount++;
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + this.apiKey);
            headers.add("Content-Type", file.getContentType());

            String uploadURL =
                    baseStorageURL+"/"+content.getContentType().toString()
                    +"/"+content.getName()+"/"+chapterName
                    +"/"+file.getOriginalFilename();

            responseMessage.append("File ").append(fileOrderCount).append(" response:");
            responseMessage.append(
                    sendPutRequest(file, uploadURL, headers)
            );

            ChapterContent chapterContent = new ChapterContent();
            chapterContent.setChapterBeta(chapterBeta);
            chapterContent.setChapter(null);
            chapterContent.setContentURL(uploadURL);
            chapterContent.setDisplayOrder(fileOrderCount);
            chapterBeta.getChapterContentList().add(chapterContent);
        }

        chapterBeta.setChapterContentsCount(fileOrderCount);
        this.chapterBetaRepository.save(chapterBeta);

        return ResponseEntity.ok(responseMessage.toString());
    }

    @Transactional
    public ChapterDTO approveChapterBeta(int id) {
        ChapterBeta chapterBeta = chapterBetaRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Chapter with id: " + id + " not found!"
                        )
                );

        List<Chapter> chapterList = chapterRepository.findByName(chapterBeta.getName())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Chapters with name: " + chapterBeta.getName() + " not found!"
                ));

        for(Chapter chapter : chapterList){
            if(chapter.getContent().getName().equals(chapterBeta.getContent().getName()))
                throw new IllegalArgumentException(
                        "Chapter with name: " + chapterBeta.getName() + " already exists and approved!"
                );
        }

        Chapter chapter = chapterMapper.convertBetaToChapter(chapterBeta);
        chapterRepository.save(chapter);
        chapterBetaRepository.deleteChapterBetaByNameAndId(chapterBeta.getId(), chapterBeta.getName());
        return chapterMapper.convertChapterToDTO(chapter);
    }

    @Transactional
    public ChapterDetailsDTO getChapterDetailsById(int id) {
        log.info("In request get chapter details");
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Chapter with id: " + id + " not found!"
                        )
                );
        ChapterDetailsDTO chapterDetailsDTO = new ChapterDetailsDTO();
        chapterDetailsDTO.setId(id);
        chapterDetailsDTO.setName(chapter.getName());
        List<String> urls = chapter.getChapterContentList().stream()
                .map(chapterContent -> chapterContent.getContentURL().replace(
                        "/object/content-library/",
                        "/object/public/content-library/"
                ))
                .toList();

        chapterDetailsDTO.setImageURLs(urls);

        List<CommentDetailsDTO> commentDetails = chapter.getComments().stream().map(
                comment -> {
                    CommentDetailsDTO commentDetailsDTO = new CommentDetailsDTO();
                    commentDetailsDTO.setId(comment.getId());
                    commentDetailsDTO.setAuthor(comment.getAuthor().getUsername());
                    commentDetailsDTO.setText(comment.getText());
                    return commentDetailsDTO;
                }
        ).toList();

        log.info(chapterDetailsDTO.toString());

        chapterDetailsDTO.setCommentDetailsList(commentDetails);
        return chapterDetailsDTO;
    }

    @Transactional
    public List<ChapterBetaDetailsDTO> getAllChapBetaDetails() {
        List<ChapterBeta> chapterBetaList = chapterBetaRepository.findAll();
        return chapterBetaList
                .stream()
                .map(ChapterMapper::chapBetaToChapBetaDetails)
                .toList();
    }
}
