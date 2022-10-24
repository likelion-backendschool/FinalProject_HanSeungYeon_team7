package com.example.mutbooks.app.postHashTag.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import com.example.mutbooks.app.keyword.entity.PostKeyword;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class PostHashTag extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;  // 회원
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;      // 글
    @ManyToOne(fetch = FetchType.LAZY)
    private PostKeyword postKeyword;    // 키워드

    // postHashTag 키워드로 게시글 검색 요청 url
    public String getSearchUrl() {
        String url = "/post/list?kwType=postHashTag&kw=%s".formatted(this.getPostKeyword().getContent());
        return url;
    }
}
