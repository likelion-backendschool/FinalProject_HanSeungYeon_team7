package com.example.mutbooks.app.post.service;

import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.post.entity.Post;
import com.example.mutbooks.app.post.form.WriteForm;
import com.example.mutbooks.app.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    // 글 전체조회
    public List<Post> findAllByOrderByIdDesc() {
        return postRepository.findAllByOrderByIdDesc();
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

    @Transactional
    public void modify(Post post, WriteForm writeForm) {
        post.setSubject(writeForm.getSubject());
        post.setContent(writeForm.getContent());

        postRepository.save(post);
    }

    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }

    // 수정 권한 여부 체크(수정 권한: 글쓴이 본인)
    public boolean canModify(Member member, Post post) {
        return member.getId().equals(post.getAuthor().getId());
    }

    // 삭제 권한 여부 체크(삭제 권한: 글쓴이 본인)
    public boolean canDelete(Member member, Post post) {
        return canModify(member, post);
    }
}
