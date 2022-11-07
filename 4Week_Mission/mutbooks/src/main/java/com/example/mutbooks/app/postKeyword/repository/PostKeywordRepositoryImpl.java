package com.example.mutbooks.app.postKeyword.repository;

import com.example.mutbooks.app.postKeyword.dto.PostKeywordDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.mutbooks.app.postHashTag.entity.QPostHashTag.postHashTag;
import static com.example.mutbooks.app.postKeyword.entity.QPostKeyword.postKeyword;

@RequiredArgsConstructor
public class PostKeywordRepositoryImpl implements PostKeywordRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostKeywordDto> getQslAllByAuthorId(Long authorId) {
        // 생성자를 이용해 List<PostKeywordDto> 로 반환
        return jpaQueryFactory
                .select(Projections.constructor(PostKeywordDto.class,
                        postKeyword.id,
                        postKeyword.content,
                        postHashTag.count()
                ))
                .from(postKeyword)
                .innerJoin(postHashTag)
                .on(postKeyword.eq(postHashTag.postKeyword))
                .where(postHashTag.member.id.eq(authorId))
                .orderBy(postKeyword.content.asc()) // 키워드 명 오름차순 정렬
                .groupBy(postKeyword.id)            // 키워드 중복 제거
                .fetch();
    }
}
