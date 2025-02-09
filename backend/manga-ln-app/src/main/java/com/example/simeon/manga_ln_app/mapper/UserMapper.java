package com.example.simeon.manga_ln_app.mapper;

import com.example.simeon.manga_ln_app.dto.UserCredentialsDTO;
import com.example.simeon.manga_ln_app.dto.UserDTO;
import com.example.simeon.manga_ln_app.models.Role;
import com.example.simeon.manga_ln_app.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMapper {
    private final CommentMapper commentMapper;
    private final ContentMapper contentMapper;

    public UserMapper(CommentMapper commentMapper, ContentMapper contentMapper) {
        this.commentMapper = commentMapper;
        this.contentMapper = contentMapper;
    }

    public User convertCredentialsToUser(UserCredentialsDTO userCredentialsDTO){
        User user = new User();
        user.setUsername(userCredentialsDTO.getUsername());
        user.setPassword(userCredentialsDTO.getPassword());
        user.setRole(Role.READER);
        user.setComments(List.of());
        user.setCreatedWorks(List.of());
        user.setPendingComments(List.of());
        user.setPendingWorks(List.of());
        return user;
    }

    public UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setComments(commentMapper.convertToDTOList(user.getComments()));
        dto.setAuthoredContent(contentMapper.convertToDTOList(user.getCreatedWorks()));
        return dto;
    }
}
