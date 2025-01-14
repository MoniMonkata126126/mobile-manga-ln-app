package com.example.simeon.manga_ln_app.service;

import com.example.simeon.manga_ln_app.dto.CommentDTO;
import com.example.simeon.manga_ln_app.models.Comment;
import com.example.simeon.manga_ln_app.models.CommentBeta;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    public CommentDTO convertToDTO(Comment comment){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setAuthor(comment.getAuthor().getUsername());
        commentDTO.setChapterName(comment.getChapter().getName());
        commentDTO.setText(comment.getText());
        return commentDTO;
    }

    public List<CommentDTO> convertToDTOList(List<Comment> comments){
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments){
            commentDTOS.add(convertToDTO(comment));
        }
        return commentDTOS;
    }

    public Comment convertFromBeta(CommentBeta commentBeta){
        Comment comment = new Comment();
        comment.setAuthor(commentBeta.getAuthor());
        comment.setText(commentBeta.getText());
        comment.setChapter(commentBeta.getChapter());
        return comment;
    }
}
