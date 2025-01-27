package com.example.simeon.manga_ln_app.service;

import com.example.simeon.manga_ln_app.dto.CommentBetaDTO;
import com.example.simeon.manga_ln_app.dto.CommentDTO;
import com.example.simeon.manga_ln_app.mapper.CommentMapper;
import com.example.simeon.manga_ln_app.models.Chapter;
import com.example.simeon.manga_ln_app.models.CommentBeta;
import com.example.simeon.manga_ln_app.models.User;
import com.example.simeon.manga_ln_app.repository.ChapterRepository;
import com.example.simeon.manga_ln_app.repository.CommentBetaRepository;
import com.example.simeon.manga_ln_app.repository.CommentRepository;
import com.example.simeon.manga_ln_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;


@Service
public class CommentService {

    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final ChapterRepository chapterRepository;
    private final CommentRepository commentRepository;
    private final CommentBetaRepository commentBetaRepository;

    public CommentService(CommentMapper commentMapper,
                          UserRepository userRepository,
                          ChapterRepository chapterRepository,
                          CommentRepository commentRepository,
                          CommentBetaRepository commentBetaRepository) {
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
        this.chapterRepository = chapterRepository;
        this.commentRepository = commentRepository;
        this.commentBetaRepository = commentBetaRepository;
    }

    @Transactional
    public void addComment(@Valid CommentBetaDTO commentBetaDTO) {
        User user = userRepository.findByUsername(commentBetaDTO.getAuthorName())
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "User with username " + commentBetaDTO.getAuthorName() + " does not exist!"
                        )
                );
        Chapter chapter = chapterRepository.findById(commentBetaDTO.getChapterId())
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Chapter with id: " + commentBetaDTO.getChapterId() + " does not exist!"
                        )
                );
        CommentBeta commentBeta = commentMapper.convertBetaDTOToBeta(commentBetaDTO, user, chapter);
        commentBetaRepository.save(commentBeta);
    }

    @Transactional
    public CommentDTO approveComment(int id) {
        CommentBeta commentBeta = commentBetaRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Comment with " + id + " does not exist!"
                        )
                );

        commentBetaRepository.deleteById(id);
        return commentMapper.convertToDTO(commentRepository.save(commentMapper.convertBetaToComment(commentBeta)));
    }
}
