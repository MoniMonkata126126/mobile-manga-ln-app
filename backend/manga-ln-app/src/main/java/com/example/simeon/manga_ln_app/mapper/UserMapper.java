package com.example.simeon.manga_ln_app.mapper;

import com.example.simeon.manga_ln_app.dto.UserCredentialsDTO;
import com.example.simeon.manga_ln_app.dto.UserDTO;
import com.example.simeon.manga_ln_app.models.User;
import com.example.simeon.manga_ln_app.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMapper {
    private final CommentService commentService;
    private final ContentMapper contentMapper;

    public UserMapper(CommentService commentService, ContentMapper contentMapper) {
        this.commentService = commentService;
        this.contentMapper = contentMapper;
    }

    public User convertCredentialsToUser(UserCredentialsDTO userCredentialsDTO){
        User user = new User();
        user.setUsername(userCredentialsDTO.getUsername());
        user.setPassword(userCredentialsDTO.getPassword());
        user.setRole(userCredentialsDTO.getRole());
        user.setComments(List.of());
        user.setCreatedWorks(List.of());
        user.setLikedContent(List.of());
        user.setPendingComments(List.of());
        user.setPendingWorks(List.of());
        return user;
    }

    public UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setComments(commentService.convertToDTOList(user.getComments()));
        dto.setAuthoredContent(contentMapper.convertToDTOList(user.getCreatedWorks()));
        return dto;
    }

    public UserCredentialsDTO convertUserToCredentials(User user) {
        UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO();
        userCredentialsDTO.setUsername(user.getUsername());
        userCredentialsDTO.setPassword(user.getPassword());
        userCredentialsDTO.setRole(user.getRole());
        return userCredentialsDTO;
    }
}
