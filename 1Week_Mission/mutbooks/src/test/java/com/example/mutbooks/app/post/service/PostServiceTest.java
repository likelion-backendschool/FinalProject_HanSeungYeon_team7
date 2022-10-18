package com.example.mutbooks.app.post.service;

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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PostServiceTest {
    @Autowired
    private PostService postService;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    @DisplayName("글 작성")
    void t1() {
        // given
        Member author = memberRepository.findByUsername("user2").orElse(null);
        // when
        Post post = postService.write(author, new WriteForm("제목", "안녕하세요1", "<ul><li><p>안녕하세요1</p></li></ul>", null));
        // then
        assertThat(post).isNotNull();
        assertThat(post.getAuthor().getUsername()).isEqualTo("user2");
        assertThat(post.getSubject()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("안녕하세요1");
        assertThat(post.getContentHtml()).isEqualTo("<ul><li><p>안녕하세요1</p></li></ul>");
    }

    @Test
    @DisplayName("글 수정")
    void t2() {
        // given
        Member author = memberRepository.findByUsername("user2").orElse(null);
        Post post = postService.findById(1);
        // when
        postService.modify(post, new WriteForm("new 제목", "new 안녕하세요", "<ul><li><p>new 안녕하세요</p></li></ul>", null));
        // then
        assertThat(post).isNotNull();
        assertThat(post.getSubject()).isEqualTo("new 제목");
        assertThat(post.getContent()).isEqualTo("new 안녕하세요");
        assertThat(post.getContentHtml()).isEqualTo("<ul><li><p>new 안녕하세요</p></li></ul>");
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