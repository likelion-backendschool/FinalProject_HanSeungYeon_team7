package com.example.mutbooks.app.post.repository;

import com.example.mutbooks.app.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    List<Post> findAllByAuthorIdOrderByIdDesc(Long authorId);
}
