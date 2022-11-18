package com.example.mutbooks.app.post.dto.response;

import com.example.mutbooks.app.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostDetailDto {
    private Long id;
    private String subject;
    private String content;
    private String contentHtml;

    public static PostDetailDto toDto(Post post) {
        return PostDetailDto.builder()
                .id(post.getId())
                .subject(post.getSubject())
                .content(post.getContent())
                .contentHtml(post.getContentHtml())
                .build();
    }

    public static List<PostDetailDto> toDtos(List<Post> posts) {
        List<PostDetailDto> postDetailDtos = posts.stream()
                .map(post -> PostDetailDto.toDto(post))
                .collect(Collectors.toList());

        return postDetailDtos;
    }
}
