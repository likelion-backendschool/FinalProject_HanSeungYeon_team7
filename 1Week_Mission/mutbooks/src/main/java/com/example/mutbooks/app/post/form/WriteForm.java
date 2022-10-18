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
    private String content;     // 마크다운 원문
    private String contentHtml; // 렌더링 결과 HTML
    private String keywords;    // 해시태그
}
