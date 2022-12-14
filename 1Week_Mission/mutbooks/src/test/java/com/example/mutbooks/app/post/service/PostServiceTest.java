package com.example.mutbooks.app.post.service;

import com.example.mutbooks.app.postHashTag.entity.PostHashTag;
import com.example.mutbooks.app.postHashTag.service.PostHashTagService;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.repository.MemberRepository;
import com.example.mutbooks.app.post.entity.Post;
import com.example.mutbooks.app.post.form.WriteForm;
import org.junit.jupiter.api.Assertions;
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
class PostServiceTest {
    @Autowired
    private PostService postService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostHashTagService postHashTagService;

    @Test
    @DisplayName("글 작성")
    void t1() {
        // given
        Member author = memberRepository.findByUsername("user2").orElse(null);
        String keywords = "#로맨스 #판타지 #판타지";
        // when
        Post post = postService.write(author, new WriteForm("제목", "안녕하세요1", "<ul><li><p>안녕하세요1</p></li></ul>", keywords));
        // then
        assertThat(post).isNotNull();
        assertThat(post.getAuthor().getUsername()).isEqualTo("user2");
        assertThat(post.getSubject()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("안녕하세요1");
        assertThat(post.getContentHtml()).isEqualTo("<ul><li><p>안녕하세요1</p></li></ul>");
        // 해시태그
        List<PostHashTag> hashTags = postHashTagService.findByPostId(3);
        assertThat(hashTags.size()).isEqualTo(2);
        assertThat(hashTags.get(0).getPostKeyword().getContent()).isEqualTo("로맨스");
        assertThat(hashTags.get(1).getPostKeyword().getContent()).isEqualTo("판타지");
    }

    @Test
    @DisplayName("글 수정")
    void t2() {
        // given
        Post post = postService.findById(1);
        String keywords = "#로맨스 #판타지 #소설";
        // when
        postService.modify(post, new WriteForm("new 제목", "new 안녕하세요", "<ul><li><p>new 안녕하세요</p></li></ul>", keywords));
        // then
        assertThat(post).isNotNull();
        assertThat(post.getSubject()).isEqualTo("new 제목");
        assertThat(post.getContent()).isEqualTo("new 안녕하세요");
        assertThat(post.getContentHtml()).isEqualTo("<ul><li><p>new 안녕하세요</p></li></ul>");
        // 해시태그
        List<PostHashTag> hashTags = postHashTagService.findByPostId(1);
        assertThat(hashTags.size()).isEqualTo(3);
        assertThat(hashTags.get(0).getPostKeyword().getContent()).isEqualTo("판타지");
        assertThat(hashTags.get(1).getPostKeyword().getContent()).isEqualTo("소설");
        assertThat(hashTags.get(2).getPostKeyword().getContent()).isEqualTo("로맨스");
    }

    @Test
    @DisplayName("글 삭제")
    void t3() {
        // given
        Post post = postService.findById(1);
        // when
        postService.delete(post);
        // then
        Assertions.assertThrows(RuntimeException.class, () -> {
            postService.findById(1);
        });
    }
}