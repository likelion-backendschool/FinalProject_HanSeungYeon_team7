package com.example.mutbooks.app.hashTag.service;

import com.example.mutbooks.app.hashTag.entity.PostHashTag;
import com.example.mutbooks.app.keyword.service.KeywordService;
import com.example.mutbooks.app.post.entity.Post;
import com.example.mutbooks.app.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class HashTagServiceTest {
    @Autowired
    private PostService postService;
    @Autowired
    private HashTagService hashTagService;
    @Autowired
    private KeywordService keywordService;

    @Test
    @DisplayName("1번 게시글에 해시태그 키워드 1개 등록하기")
    void t1() {
        // given
        Post post = postService.findById(1);
        String keywordContent1 = "판타지";
        String keywordContent2 = "판타지";
        String keywordContent3 = "소설";
        // when
        hashTagService.save(post, keywordContent1);
        hashTagService.save(post, keywordContent2);
        hashTagService.save(post, keywordContent3);
        // then
        List<PostHashTag> hashTags = hashTagService.findByPostId(1);

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
        hashTagService.apply(post, keywords);
        // then
        List<PostHashTag> hashTags = hashTagService.findByPostId(1);

        assertThat(hashTags.size()).isEqualTo(3);
        assertThat(hashTags.get(0).getPostKeyword().getContent()).isEqualTo("판타지");
        assertThat(hashTags.get(1).getPostKeyword().getContent()).isEqualTo("소설");
        assertThat(hashTags.get(2).getPostKeyword().getContent()).isEqualTo("SF");
    }
}