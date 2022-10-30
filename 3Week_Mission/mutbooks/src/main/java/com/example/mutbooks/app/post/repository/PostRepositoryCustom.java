package com.example.mutbooks.app.post.repository;

import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.post.entity.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> searchQsl(Member author, String kwType, String kw);
}
