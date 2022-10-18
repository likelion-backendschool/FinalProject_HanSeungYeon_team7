package com.example.mutbooks.app.post.service;

import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.post.entity.Post;
import com.example.mutbooks.app.post.form.WriteForm;
import com.example.mutbooks.app.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    public Post findById(long id) {
        // TODO : 예외 처리
        return postRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    @Transactional
    public Post write(Member author, WriteForm writeForm) {
        Post post = Post.builder()
                .subject(writeForm.getSubject())
                .content(writeForm.getContent())
                .author(author)
                .build();

        postRepository.save(post);

        return post;
    }

    // 수정 권한 여부 체크(글쓴이 본인인지)
    public boolean canModify(Member member, Post post) {
        return member.getId().equals(post.getAuthor().getId());
    }
}
