package com.example.mutbooks.app.postKeyword.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostKeywordDto {
    private Long id;
    private String content;
    private Long postCount;   // 해당 해시태그 키워드와 관련 게시글 수
}
