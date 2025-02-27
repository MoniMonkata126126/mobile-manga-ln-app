package com.example.simeon.manga_ln_app.service;

import com.example.simeon.manga_ln_app.dto.ChapterInfoDTO;
import com.example.simeon.manga_ln_app.dto.ContentInputDTO;
import com.example.simeon.manga_ln_app.dto.ContentBetaDTO;
import com.example.simeon.manga_ln_app.dto.ContentDTO;
import com.example.simeon.manga_ln_app.exceptions.DBSearchException;
import com.example.simeon.manga_ln_app.mapper.ChapterMapper;
import com.example.simeon.manga_ln_app.mapper.ContentMapper;
import com.example.simeon.manga_ln_app.models.*;
import com.example.simeon.manga_ln_app.repository.ContentBetaRepository;
import com.example.simeon.manga_ln_app.repository.ContentRepository;
import com.example.simeon.manga_ln_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;

@Slf4j
@Service
public class ContentService {
    private final ContentMapper contentMapper;
    private final ContentBetaRepository contentBetaRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    private static final Set<String> PARTICLES = Set.of(
        "of", "in", "and", "the", "to", "a", "is", "on", "from", "at"
    );

    public ContentService(ContentMapper contentMapper,
                          ContentBetaRepository contentBetaRepository,
                          ContentRepository contentRepository,
                          UserRepository userRepository){
        this.contentMapper = contentMapper;
        this.contentBetaRepository = contentBetaRepository;
        this.contentRepository = contentRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public ContentBetaDTO addContentBeta(@Valid ContentInputDTO contentInputDTO) {
        Optional<User> userOptional = userRepository.findByUsername(contentInputDTO.getAuthor());
        if(userOptional.isEmpty()) {
            throw new IllegalArgumentException("Author with username: " + contentInputDTO.getAuthor() + " does not exist!");
        }
        User user = userOptional.get();
        if(contentBetaRepository.findByName(contentInputDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Content Beta with name: " + contentInputDTO.getName() + " already exists!");
        }
        if(contentRepository.findByName(contentInputDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Content with name: " + contentInputDTO.getName() + " already exists!");
        }
        ContentBeta contentBeta = contentMapper.convertToBetaFromInputDTO(contentInputDTO, user);
        contentBetaRepository.save(contentBeta);
        ContentBeta saved = contentBetaRepository.findByName(contentBeta.getName())
                .orElseThrow(() -> new DBSearchException("Couldn't save content with name: " + contentBeta.getName()));
        return ContentMapper.convertToBetaDTOFromBeta(saved);
    }

    @Transactional
    public ContentDTO approveContentBeta(int id) {
        Optional<ContentBeta> contentBetaOptional = contentBetaRepository.findById(id);
        if(contentBetaOptional.isEmpty()) {
            throw new IllegalArgumentException("Content with ID: " + id + " does not exist!");
        }
        ContentBeta contentBeta = contentBetaOptional.get();
        Content content = contentMapper.convertToContentFromBeta(contentBeta);
        contentRepository.save(content);
        contentBetaRepository.deleteById(id);
        Content saved = contentRepository.findByName(contentBeta.getName())
                .orElseThrow(() -> new DBSearchException(
                        "Couldn't save content with name: "
                                + contentBeta.getName()));
        return contentMapper.convertToContentDTO(saved);
    }


    @Transactional
    public List<ContentDTO> searchByQuery(String q, String type) {
        List<String> keywords = Arrays.stream(q.split("[+\\s_]+"))
                .filter(keyword -> !keyword.trim().isEmpty())
                .map(String::toLowerCase)
                .filter(keyword -> !PARTICLES.contains(keyword))
                .toList();
        if (keywords.isEmpty()) {
            throw new IllegalArgumentException("Search query must not be empty!");
        }

        String keywordsString = String.join(",", keywords);
        Optional<List<Content>> contentMatchOptional = contentRepository.findByKeywords(keywordsString, keywords.size() * 8 / 10);
        List<Content> matches = contentMatchOptional.orElse(List.of());
        if (matches.isEmpty()) {
            throw new DBSearchException("No content found for such query!");
        }

        return matches
                .stream()
                .filter(content ->
                    content.getContentType().name().equals(type)
                )
                .limit(10)
                .map(contentMapper::convertToContentDTO)
                .toList();
    }

    public List<ContentDTO> searchByType(String type) {
        ContentType contentType;
        try {
            contentType = ContentType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type must be either MANGA or LN!");
        }
        Optional<List<Content>> optionalContentList = contentRepository.findByContentType(contentType);
        if (optionalContentList.isEmpty()){
            throw new DBSearchException("Content with type " + type + " not found!");
        }
        return optionalContentList.get()
                .stream()
                .map(contentMapper::convertToContentDTO)
                .toList();
    }

    @Transactional
    public List<ChapterInfoDTO> getAllChaptersForContent(int id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Content with id: " + id + " not found!"
                        )
                );
        List<Chapter> chapterList = content.getChapters();
        return chapterList.stream().map(ChapterMapper::convertChapterToInfoDTO).toList();
    }

    public List<ContentBetaDTO> getAllContentBeta() {
        List<ContentBeta> contentBetaList = contentBetaRepository.findAll();
        return contentBetaList.stream()
                .map(ContentMapper::convertToBetaDTOFromBeta)
                .toList();
    }
}
