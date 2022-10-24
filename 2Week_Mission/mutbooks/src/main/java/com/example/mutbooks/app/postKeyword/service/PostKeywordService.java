package com.example.mutbooks.app.postKeyword.service;

import com.example.mutbooks.app.postKeyword.dto.PostKeywordDto;
import com.example.mutbooks.app.postKeyword.entity.PostKeyword;
import com.example.mutbooks.app.postKeyword.exception.PostKeywordNotFoundException;
import com.example.mutbooks.app.postKeyword.repository.PostKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostKeywordService {
    private final PostKeywordRepository postKeywordRepository;

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
        postKeywordRepository.save(keyword);

        return keyword;
    }

    // 키워드 content 로 조회
    public PostKeyword findByContent(String content) {
        return postKeywordRepository.findByContent(content).orElse(null);
    }

    // 작가가 등록한 글과 관련된 모든 해시태그 키워드 조회
    public List<PostKeywordDto> findByMemberId(Long authorId) {
        return postKeywordRepository.getQslAllByAuthorId(authorId);
    }

    public PostKeyword findById(Long id) {
        return postKeywordRepository.findById(id).orElseThrow(() -> {
            throw new PostKeywordNotFoundException("해당 키워드는 존재하지 않습니다.");
        });
    }
}
