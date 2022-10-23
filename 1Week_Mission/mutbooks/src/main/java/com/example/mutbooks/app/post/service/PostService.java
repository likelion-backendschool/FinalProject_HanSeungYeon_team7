package com.example.mutbooks.app.post.service;

import com.example.mutbooks.app.postHashTag.service.HashTagService;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.post.exception.PostNotFoundException;
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
    private final HashTagService hashTagService;

    public Post findById(long id) {
        return postRepository.findById(id).orElseThrow(() -> {
            throw new PostNotFoundException();
        });
    }

    // 내 글 전체조회
    public List<Post> findAllByAuthorIdOrderByIdDesc(Long authorId) {
        return postRepository.findAllByAuthorIdOrderByIdDesc(authorId);
    }

    // 내 글 전체/키워드 기반 겁색 조회
    public List<Post> search(Member author, String kwType, String kw) {
        return postRepository.searchQsl(author, kwType, kw);
    }

    @Transactional
    public Post write(Member author, WriteForm writeForm) {
        Post post = Post.builder()
                .subject(writeForm.getSubject())
                .content(writeForm.getContent())
                .contentHtml(writeForm.getContentHtml())
                .author(author)
                .build();

        postRepository.save(post);

        // 해시태그 적용
        String keywords = writeForm.getKeywords();
        if(keywords != null) {
            hashTagService.apply(post, keywords);
        }

        return post;
    }

    @Transactional
    public void modify(Post post, WriteForm writeForm) {
        post.setSubject(writeForm.getSubject());
        post.setContent(writeForm.getContent());
        post.setContentHtml(writeForm.getContentHtml());

        // 해시태그 적용
        String keywords = writeForm.getKeywords();
        if(keywords != null) {
            hashTagService.apply(post, keywords);
        }

        postRepository.save(post);
    }

    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }

    // 글 수정 권한 여부 체크(수정 권한: 글쓴이 본인)
    public boolean canModify(Member member, Post post) {
        return member.getId().equals(post.getAuthor().getId());
    }

    // 글 삭제 권한 여부 체크(삭제 권한: 글쓴이 본인)
    public boolean canDelete(Member member, Post post) {
        return canModify(member, post);
    }

    // 글 조회 권한 여부 체크(권한: 글쓴이 본인)
    public boolean canSelect(Member member, Post post) {
        return canModify(member, post);
    }
}
