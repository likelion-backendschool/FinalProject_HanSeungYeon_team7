package com.example.mutbooks.app.mybook.dto.response;

import com.example.mutbooks.app.mybook.entity.MyBook;
import com.example.mutbooks.app.post.entity.Post;
import com.example.mutbooks.app.product.dto.response.ProductDetailDto;
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
public class MyBookDetailDto {
    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private Long ownerId;
    private ProductDetailDto product;

    public static MyBookDetailDto toDto(MyBook myBook, List<Post> posts) {
        ProductDetailDto productDetailDto = ProductDetailDto.toDto(myBook.getProduct(), posts);

        return MyBookDetailDto.builder()
                .id(myBook.getId())
                .createDate(myBook.getCreateDate())
                .modifyDate(myBook.getUpdateDate())
                .ownerId(myBook.getOwner().getId())
                .product(productDetailDto)
                .build();
    }
}
