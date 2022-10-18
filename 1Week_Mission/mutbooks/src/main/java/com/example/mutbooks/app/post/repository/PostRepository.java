package com.example.mutbooks.app.post.repository;

import com.example.mutbooks.app.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
