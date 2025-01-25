package com.example.simeon.manga_ln_app.service;

import com.example.simeon.manga_ln_app.exceptions.DBSearchException;
import com.example.simeon.manga_ln_app.exceptions.SendRequestException;
import com.example.simeon.manga_ln_app.models.ChapterBeta;
import com.example.simeon.manga_ln_app.models.ChapterContent;
import com.example.simeon.manga_ln_app.models.Content;
import com.example.simeon.manga_ln_app.repository.ChapterBetaRepository;
import com.example.simeon.manga_ln_app.repository.ChapterContentRepository;
import com.example.simeon.manga_ln_app.repository.ContentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ChapterService {
    @Value("${storage.bucket.url}")
    private String baseStorageURL;
    @Value("${storage.api.key}")
    private String apiKey;
    private final ContentRepository contentRepository;
    private final ChapterBetaRepository chapterBetaRepository;
    private final ChapterContentRepository chapterContentRepository;

    public ChapterService(ContentRepository contentRepository, ChapterBetaRepository chapterBetaRepository, ChapterContentRepository chapterContentRepository) {
        this.contentRepository = contentRepository;
        this.chapterBetaRepository = chapterBetaRepository;
        this.chapterContentRepository = chapterContentRepository;
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

        ChapterBeta chapterBeta = new ChapterBeta();
        chapterBeta.setContent(content);
        chapterBeta.setName(chapterName);
        chapterBeta.setChapterContentList(List.of());
        this.chapterBetaRepository.save(chapterBeta);

        int fileOrderCount = 1;

        for (MultipartFile file : multipartFiles) {
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
            chapterContent.setChapterBeta(
                    this.chapterBetaRepository.findByName(chapterName)
                            .orElseThrow(
                                    () -> new DBSearchException(
                                            "Failed to find chapter with name: " + chapterName
                                    )
                            )
            );
            chapterContent.setChapter(null);
            chapterContent.setContentURL(uploadURL);
            chapterContent.setDisplayOrder(fileOrderCount);
            this.chapterContentRepository.save(chapterContent);
            fileOrderCount++;
        }

        return ResponseEntity.ok(responseMessage.toString());
    }
}
