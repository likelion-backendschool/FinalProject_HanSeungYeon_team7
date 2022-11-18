package com.example.mutbooks.app.product.dto.response;

import com.example.mutbooks.app.post.dto.response.PostDetailDto;
import com.example.mutbooks.app.post.entity.Post;
import com.example.mutbooks.app.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductDetailDto {
    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private Long authorId;
    private String authorName;
    private String subject;
    private List<PostDetailDto> bookChapters;

    public static ProductDetailDto toDto(Product product, List<Post> posts) {
        List<PostDetailDto> postDetailDtos = PostDetailDto.toDtos(posts);

        return ProductDetailDto.builder()
                .id(product.getId())
                .createDate(product.getCreateDate())
                .modifyDate(product.getUpdateDate())
                .authorId(product.getAuthor().getId())
                .authorName(product.getAuthor().getNickname())
                .subject(product.getSubject())
                .bookChapters(postDetailDtos)
                .build();
    }
}
