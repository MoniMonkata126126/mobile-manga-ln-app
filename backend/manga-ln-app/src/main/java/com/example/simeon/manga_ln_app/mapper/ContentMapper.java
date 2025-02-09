package com.example.simeon.manga_ln_app.mapper;

import com.example.simeon.manga_ln_app.dto.ContentInputDTO;
import com.example.simeon.manga_ln_app.dto.ContentBetaDTO;
import com.example.simeon.manga_ln_app.dto.ContentDTO;
import com.example.simeon.manga_ln_app.models.Content;
import com.example.simeon.manga_ln_app.models.ContentBeta;
import com.example.simeon.manga_ln_app.models.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentMapper {

    //NEEDS FIXING
    public ContentInputDTO convertToDTO(Content content){
        ContentInputDTO contentInputDTO = new ContentInputDTO();
        contentInputDTO.setName(content.getName());
        contentInputDTO.setAuthor(content.getAuthor().getUsername());
        contentInputDTO.setContentType(content.getContentType());
        return contentInputDTO;
    }

    public List<ContentInputDTO> convertToDTOList(List<Content> contents){
        List<ContentInputDTO> contentInfoDTOS = new ArrayList<>();
        for( Content content : contents ){
            contentInfoDTOS.add(convertToDTO(content));
        }
        return contentInfoDTOS;
    }

    public Content convertToContentFromBeta(ContentBeta contentBeta){
        Content content = new Content();
        content.setContentType(contentBeta.getContentType());
        content.setName(contentBeta.getName());
        content.setAuthor(contentBeta.getAuthor());
        content.setChapters(new ArrayList<>());
        content.setChaptersBeta(new ArrayList<>());
        return content;
    }

    public ContentBeta convertToBetaFromInputDTO(@Valid ContentInputDTO contentInputDTO, User user){
        ContentBeta contentBeta = new ContentBeta();
        contentBeta.setContentType(contentInputDTO.getContentType());
        contentBeta.setName(contentInputDTO.getName());
        contentBeta.setAuthor(user);
        return contentBeta;
    }

    public static ContentBetaDTO convertToBetaDTOFromBeta(ContentBeta contentBeta) {
        ContentBetaDTO dto = new ContentBetaDTO();
        dto.setId(contentBeta.getId());
        dto.setContentType(contentBeta.getContentType());
        dto.setName(contentBeta.getName());
        dto.setAuthorUsername(contentBeta.getAuthor().getUsername());
        return dto;
    }

    public ContentDTO convertToContentDTO(Content content) {
        ContentDTO dto = new ContentDTO();
        dto.setId(content.getId());
        dto.setContentType(content.getContentType());
        dto.setName(content.getName());
        dto.setAuthorUsername(content.getAuthor().getUsername());
        return dto;
    }
}
