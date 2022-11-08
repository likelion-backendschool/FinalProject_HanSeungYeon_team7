package com.example.mutbooks.app.postHashTag.repository;

import com.example.mutbooks.app.postHashTag.entity.PostHashTag;
import com.example.mutbooks.app.postKeyword.entity.PostKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {
    Optional<PostHashTag> findByPostIdAndPostKeywordId(Long postId, Long postKeywordId);

    List<PostHashTag> findByPostId(Long postId);

    List<PostHashTag> findByPostKeyword(PostKeyword postKeyword);
}
