package com.example.mutbooks.app.post.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import com.example.mutbooks.app.postHashTag.entity.PostHashTag;
import com.example.mutbooks.app.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL})
    private List<PostHashTag> hashTags = new ArrayList<>();

    // 해당 게시글의 해시태그들을 한 문장으로 반환
    public String getHashTagString() {
        if(hashTags.isEmpty()) {
            return "";
        }

        return "#" + hashTags
                .stream()
                .map(hashTag -> hashTag.getPostKeyword().getContent())
                .sorted()
                .collect(Collectors.joining(" #"))
                .trim();
    }
}
