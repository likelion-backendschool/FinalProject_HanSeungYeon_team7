package com.example.mutbooks.app.postHashTag.service;

import com.example.mutbooks.app.postHashTag.entity.PostHashTag;
import com.example.mutbooks.app.postKeyword.service.PostKeywordService;
import com.example.mutbooks.app.post.entity.Post;
import com.example.mutbooks.app.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PostHashTagServiceTest {
    @Autowired
    private PostService postService;
    @Autowired
    private PostHashTagService postHashTagService;
    @Autowired
    private PostKeywordService postKeywordService;

    @Test
    @DisplayName("2번 게시글에 해시태그 키워드 2개 등록하기")
    void t1() {
        // given
        Post post = postService.findById(2);
        String keywordContent1 = "판타지";
        String keywordContent2 = "판타지";
        String keywordContent3 = "소설";
        // when
        postHashTagService.save(post, keywordContent1);
        postHashTagService.save(post, keywordContent2);
        postHashTagService.save(post, keywordContent3);
        // then
        List<PostHashTag> hashTags = postHashTagService.findByPostId(2);

        assertThat(hashTags.size()).isEqualTo(2);
        assertThat(hashTags.get(0).getPostKeyword().getContent()).isEqualTo("판타지");
        assertThat(hashTags.get(1).getPostKeyword().getContent()).isEqualTo("소설");
    }

    @Test
    @DisplayName("입력된 문자열에서 해시태그 추출해서 1번 게시글에 등록하기")
    void t2() {
        // given
        Post post = postService.findById(1);
        String keywords = "#판타지  #소설 # SF # 소설";
        // when
        postHashTagService.apply(post, keywords);
        // then
        List<PostHashTag> hashTags = postHashTagService.findByPostId(1);

        assertThat(hashTags.size()).isEqualTo(3);
        assertThat(hashTags.get(0).getPostKeyword().getContent()).isEqualTo("판타지");
        assertThat(hashTags.get(1).getPostKeyword().getContent()).isEqualTo("소설");
        assertThat(hashTags.get(2).getPostKeyword().getContent()).isEqualTo("SF");
    }
}