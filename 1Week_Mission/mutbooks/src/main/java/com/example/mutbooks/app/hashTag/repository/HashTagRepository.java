package com.example.mutbooks.app.hashTag.repository;

import com.example.mutbooks.app.hashTag.entity.PostHashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HashTagRepository extends JpaRepository<PostHashTag, Long> {
    Optional<PostHashTag> findByPostIdAndPostKeywordId(Long postId, Long postKeywordId);

    List<PostHashTag> findByPostId(Long postId);
}
