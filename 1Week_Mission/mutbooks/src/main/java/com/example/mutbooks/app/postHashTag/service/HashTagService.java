package com.example.mutbooks.app.postHashTag.service;

import com.example.mutbooks.app.postHashTag.entity.PostHashTag;
import com.example.mutbooks.app.postHashTag.repository.PostHashTagRepository;
import com.example.mutbooks.app.keyword.entity.PostKeyword;
import com.example.mutbooks.app.keyword.service.KeywordService;
import com.example.mutbooks.app.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashTagService {
    private final PostHashTagRepository postHashTagRepository;
    private final KeywordService keywordService;

    // 게시글에 해시태그 반영
    public void apply(Post post, String keywords) {
        // 1. 기존 해시태그 가져오기
        List<PostHashTag> oldHashTags = findByPostId(post.getId());

        // 2. 새로운 해시태그 키워드 리스트
        List<String> keywordContents = Arrays.stream(keywords.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        // 3. 삭제할 해시태그 구하기(기존 해시태그 리스트에서 새로운 해시태그 리스트에 없는 것)
        List<PostHashTag> deleteHashTags = new ArrayList<>();
        for(PostHashTag oldHashTag : oldHashTags) {
            // 기존에 등록된 해시태그가 새롭게 등록된 해시태그에 포함되었는지 여부
            boolean contains = keywordContents.stream().anyMatch(s -> s.equals(oldHashTag.getPostKeyword().getContent()));

            if(!contains) {
                deleteHashTags.add(oldHashTag);
            }
        }

        // 4. 3번에서 구한 해시태그 삭제
        deleteHashTags.forEach(hashTag -> {
            postHashTagRepository.delete(hashTag);
        });

        // 5. 나머지 해시태그는 저장
        keywordContents.forEach(keywordContent -> {
            save(post, keywordContent);
        });
    }

    // 해시태그 저장
    public PostHashTag save(Post post, String keywordContent) {
        // 1. keyword 가져오기
        PostKeyword keyword = keywordService.save(keywordContent);

        // 2. (postId + keywordId) 가 DB에 있으면 바로 리턴
        PostHashTag hashTag = postHashTagRepository.findByPostIdAndPostKeywordId(post.getId(), keyword.getId()).orElse(null);
        if(hashTag != null) {
            return hashTag;
        }
        // 3. (postId + keywordId) 로 DB에 없으면 postHashTag 저장
        hashTag = PostHashTag.builder()
                .member(post.getAuthor())
                .post(post)
                .postKeyword(keyword)
                .build();
        postHashTagRepository.save(hashTag);

        return hashTag;
    }

    // postId로 postHashTag 조회
    public List<PostHashTag> findByPostId(long postId) {
        return postHashTagRepository.findByPostId(postId);
    }
}
