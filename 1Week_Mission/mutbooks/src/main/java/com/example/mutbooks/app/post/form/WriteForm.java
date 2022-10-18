package com.example.mutbooks.app.post.form;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class WriteForm {
    @NotEmpty
    private String subject;     // 제목
    @NotEmpty
    private String content;     // 내용
    private String keywords;    // 해시태그
}
