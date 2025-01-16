package com.example.simeon.manga_ln_app.service;

import com.example.simeon.manga_ln_app.dto.ContentInputDTO;
import com.example.simeon.manga_ln_app.dto.ContentBetaDTO;
import com.example.simeon.manga_ln_app.dto.ContentDTO;
import com.example.simeon.manga_ln_app.exceptions.DBSearchException;
import com.example.simeon.manga_ln_app.mapper.ContentMapper;
import com.example.simeon.manga_ln_app.models.Content;
import com.example.simeon.manga_ln_app.models.ContentBeta;
import com.example.simeon.manga_ln_app.models.User;
import com.example.simeon.manga_ln_app.repository.ContentBetaRepository;
import com.example.simeon.manga_ln_app.repository.ContentRepository;
import com.example.simeon.manga_ln_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Set;

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
            throw new IllegalArgumentException("Content with name: " + contentInputDTO.getName() + " already exists!");
        }
        ContentBeta contentBeta = contentMapper.convertToBetaFromInputDTO(contentInputDTO, user);
        contentBetaRepository.save(contentBeta);
        ContentBeta saved = contentBetaRepository.findByName(contentBeta.getName())
                .orElseThrow(() -> new DBSearchException("Couldn't save content with name: " + contentBeta.getName()));
        return contentMapper.convertToBetaDTOFromBeta(saved);
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
    public List<ContentDTO> searchByQuery(String q) {
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
                .limit(10)
                .map(contentMapper::convertToContentDTO)
                .toList();
    }
}
