package com.example.mutbooks.app.keyword.service;

import com.example.mutbooks.app.keyword.entity.PostKeyword;
import com.example.mutbooks.app.keyword.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordService {
    private final KeywordRepository keywordRepository;

    // 키워드 저장
    @Transactional
    public PostKeyword save(String content) {
        PostKeyword keyword = findByContent(content);

        // 1. 해당 키워드(content)가 DB에 있으면 바로 리턴
        if(keyword != null) {
            return keyword;
        }
        // 2. 해당 키워드(content)가 DB에 없으면 저장
        keyword = PostKeyword.builder()
                .content(content)
                .build();
        keywordRepository.save(keyword);

        return keyword;
    }

    // 키워드 content 로 조회
    public PostKeyword findByContent(String content) {
        return keywordRepository.findByContent(content).orElse(null);
    }
}
