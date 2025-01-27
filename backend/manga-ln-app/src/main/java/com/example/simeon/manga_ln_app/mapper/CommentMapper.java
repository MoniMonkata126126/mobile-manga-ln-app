package com.example.simeon.manga_ln_app.mapper;

import com.example.simeon.manga_ln_app.dto.CommentBetaDTO;
import com.example.simeon.manga_ln_app.dto.CommentDTO;
import com.example.simeon.manga_ln_app.models.Chapter;
import com.example.simeon.manga_ln_app.models.Comment;
import com.example.simeon.manga_ln_app.models.CommentBeta;
import com.example.simeon.manga_ln_app.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentMapper {
    public static CommentDTO convertCommentToDTO(Comment comment){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setAuthor(comment.getAuthor().getUsername());
        commentDTO.setChapterName(comment.getChapter().getName());
        commentDTO.setText(comment.getText());
        return commentDTO;
    }

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

    public Comment convertBetaToComment(CommentBeta commentBeta){
        Comment comment = new Comment();
        comment.setAuthor(commentBeta.getAuthor());
        comment.setText(commentBeta.getText());
        comment.setChapter(commentBeta.getChapter());
        return comment;
    }

    public CommentBeta convertBetaDTOToBeta(CommentBetaDTO commentBetaDTO, User user, Chapter chapter) {
        CommentBeta commentBeta = new CommentBeta();
        commentBeta.setAuthor(user);
        commentBeta.setChapter(chapter);
        commentBeta.setText(commentBetaDTO.getText());
        return commentBeta;
    }
}
