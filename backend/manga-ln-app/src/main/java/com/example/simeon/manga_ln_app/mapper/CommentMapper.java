package com.example.simeon.manga_ln_app.mapper;

import com.example.simeon.manga_ln_app.dto.CommentDTO;
import com.example.simeon.manga_ln_app.models.Comment;

public class CommentMapper {
    public static CommentDTO convertCommentToDTO(Comment comment){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setAuthor(comment.getAuthor().getUsername());
        commentDTO.setChapterName(comment.getChapter().getName());
        commentDTO.setText(comment.getText());
        return commentDTO;
    }
}
