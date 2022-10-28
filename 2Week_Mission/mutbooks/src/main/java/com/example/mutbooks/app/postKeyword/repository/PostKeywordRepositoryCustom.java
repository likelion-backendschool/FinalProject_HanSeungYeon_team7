package com.example.mutbooks.app.postKeyword.repository;

import com.example.mutbooks.app.postKeyword.dto.PostKeywordDto;

import java.util.List;

public interface PostKeywordRepositoryCustom {
    List<PostKeywordDto> getQslAllByAuthorId(Long authorId);
}
