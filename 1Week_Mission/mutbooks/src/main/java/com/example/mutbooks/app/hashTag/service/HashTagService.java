package com.example.mutbooks.app.hashTag.service;

import com.example.mutbooks.app.hashTag.entity.PostHashTag;
import com.example.mutbooks.app.hashTag.repository.HashTagRepository;
import com.example.mutbooks.app.keyword.entity.PostKeyword;
import com.example.mutbooks.app.keyword.service.KeywordService;
import com.example.mutbooks.app.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashTagService {
    private final HashTagRepository hashTagRepository;
    private final KeywordService keywordService;

    // 게시글에 해시태그 반영
    public void apply(Post post, String keywords) {
        // 해시태그의 키워드 리스트
        List<String> keywordContents = Arrays.stream(keywords.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        keywordContents.forEach(keywordContent -> {
            save(post, keywordContent);
        });
    }

    // 해시태그 저장
    public PostHashTag save(Post post, String keywordContent) {
        // 1. keyword 가져오기
        PostKeyword keyword = keywordService.save(keywordContent);

        // 2. (postId + keywordId) 가 DB에 있으면 바로 리턴
        PostHashTag hashTag = hashTagRepository.findByPostIdAndPostKeywordId(post.getId(), keyword.getId()).orElse(null);
        if(hashTag != null) {
            return hashTag;
        }
        // 3. (postId + keywordId) 로 DB에 없으면 hashTag 저장
        hashTag = PostHashTag.builder()
                .member(post.getAuthor())
                .post(post)
                .postKeyword(keyword)
                .build();
        hashTagRepository.save(hashTag);

        return hashTag;
    }

    // postId로 hashTag 조회
    public List<PostHashTag> findByPostId(long postId) {
        return hashTagRepository.findByPostId(postId);
    }
}
