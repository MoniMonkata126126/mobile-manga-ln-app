package com.example.simeon.manga_ln_app.mapper;

import com.example.simeon.manga_ln_app.dto.ChapterBetaDetailsDTO;
import com.example.simeon.manga_ln_app.dto.ChapterDTO;
import com.example.simeon.manga_ln_app.dto.ChapterInfoDTO;
import com.example.simeon.manga_ln_app.models.Chapter;
import com.example.simeon.manga_ln_app.models.ChapterBeta;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterMapper {
    public ChapterDTO convertChapterToDTO(Chapter chapter){
        ChapterDTO chapterDTO = new ChapterDTO();
        chapterDTO.setName(chapter.getName());
        chapterDTO.setContentName(chapter.getContent().getName());
        chapterDTO.setComments(chapter.getComments().stream().map(CommentMapper::convertCommentToDTO).toList());
        return chapterDTO;
    }

    public static ChapterInfoDTO convertChapterToInfoDTO(Chapter chapter){
        ChapterInfoDTO chapterInfoDTO = new ChapterInfoDTO();
        chapterInfoDTO.setId(chapter.getId());
        chapterInfoDTO.setName(chapter.getName());
        return chapterInfoDTO;
    }


    public Chapter convertBetaToChapter(ChapterBeta chapterBeta) {
        Chapter chapter = new Chapter();
        chapter.setName(chapterBeta.getName());

        chapterBeta.getChapterContentList().forEach(chapterContent -> {
            chapterContent.setChapterBeta(null);
            chapterContent.setChapter(chapter);
        });
        chapter.setChapterContentList(chapterBeta.getChapterContentList());
        chapterBeta.setChapterContentList(List.of());

        chapter.setContent(chapterBeta.getContent());
        chapter.setComments(List.of());
        chapter.setChapterContentsCount(chapterBeta.getChapterContentsCount());
        return chapter;
    }

    public static ChapterBetaDetailsDTO chapBetaToChapBetaDetails(ChapterBeta chapterBeta){
        ChapterBetaDetailsDTO chapterBetaDetailsDTO = new ChapterBetaDetailsDTO();
        chapterBetaDetailsDTO.setId(chapterBeta.getId());
        chapterBetaDetailsDTO.setName(chapterBeta.getName());
        chapterBetaDetailsDTO.setContentName(chapterBeta.getContent().getName());
        return chapterBetaDetailsDTO;
    }
}
