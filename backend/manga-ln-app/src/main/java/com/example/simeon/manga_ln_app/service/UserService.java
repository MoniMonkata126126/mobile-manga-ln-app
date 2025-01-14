package com.example.simeon.manga_ln_app.service;

import com.example.simeon.manga_ln_app.dto.CommentDTO;
import com.example.simeon.manga_ln_app.exceptions.DBSearchException;
import com.example.simeon.manga_ln_app.mapper.ContentMapper;
import com.example.simeon.manga_ln_app.models.CommentBeta;
import com.example.simeon.manga_ln_app.models.User;
import com.example.simeon.manga_ln_app.repository.CommentBetaRepository;
import com.example.simeon.manga_ln_app.repository.CommentRepository;
import com.example.simeon.manga_ln_app.repository.UserRepository;
import com.example.simeon.manga_ln_app.dto.UserDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CommentBetaRepository commentBetaRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final ContentMapper contentMapper;

    public UserService(UserRepository userRepository,
                       CommentService commentService,
                       ContentMapper contentMapper,
                       CommentBetaRepository commentBetaRepository,
                       CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.commentService = commentService;
        this.contentMapper = contentMapper;
        this.commentBetaRepository = commentBetaRepository;
        this.commentRepository = commentRepository;
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setComments(commentService.convertToDTOList(user.getComments()));
        dto.setAuthoredContent(contentMapper.convertToDTOList(user.getCreatedWorks()));
        return dto;
    }

    @Transactional
    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public UserDTO getById(@NotEmpty int id) {
        return convertToDTO(userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " does not exist!")));
    }

    @Transactional
    public void addUser(@Valid User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User with username " + user.getUsername() + " already exists!");
        }
        userRepository.save(user);
    }

    @Transactional
    public UserDTO getByUsername(@NotBlank String username) {
        return convertToDTO(userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " does not exist!")));
    }

    @Transactional
    public void addComment(@Valid CommentBeta commentBeta) {
        if (!userRepository.existsByUsername(commentBeta.getAuthor().getUsername()).orElse(false)) {
            throw new IllegalArgumentException("User with username "
                    + commentBeta.getAuthor().getUsername() +
                    " does not exist!");
        }
        commentBetaRepository.save(commentBeta);
    }


    @Transactional
    public CommentDTO approveComment(int id) {
        CommentBeta commentBeta = commentBetaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Comment with " + id + " does not exist!"));
        commentRepository.save(commentService.convertFromBeta(commentBeta));
        commentBetaRepository.deleteById(id);
        return commentRepository.findByText(commentBeta.getText()).orElseThrow(() -> new DBSearchException("Comment not found!"));
    }
}