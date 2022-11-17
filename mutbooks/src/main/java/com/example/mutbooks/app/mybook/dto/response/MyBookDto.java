package com.example.mutbooks.app.mybook.dto.response;

import com.example.mutbooks.app.mybook.entity.MyBook;
import com.example.mutbooks.app.product.dto.response.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MyBookDto {
    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private Long ownerId;
    private ProductDto product;

    public static MyBookDto toDto(MyBook myBook) {
        ProductDto productDto = ProductDto.toDto(myBook.getProduct());

        return MyBookDto.builder()
                .id(myBook.getId())
                .createDate(myBook.getCreateDate())
                .modifyDate(myBook.getUpdateDate())
                .ownerId(myBook.getOwner().getId())
                .product(productDto)
                .build();
    }
}
