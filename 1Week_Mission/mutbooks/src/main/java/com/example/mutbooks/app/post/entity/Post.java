package com.example.mutbooks.app.post.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import com.example.mutbooks.app.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {
    private String subject; // 제목
    private String content; // 내용(마크다운 원문)
    private String contentHtml; // HTML 내용(토스트에디터의 렌더링 결과)

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;  // 글쓴이
}
