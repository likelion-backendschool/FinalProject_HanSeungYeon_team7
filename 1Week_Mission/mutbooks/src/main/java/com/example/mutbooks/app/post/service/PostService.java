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
}
