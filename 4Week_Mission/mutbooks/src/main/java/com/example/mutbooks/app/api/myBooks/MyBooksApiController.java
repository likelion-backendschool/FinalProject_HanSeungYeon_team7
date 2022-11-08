package com.example.mutbooks.app.api.myBooks;

import com.example.mutbooks.app.base.dto.RsData;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.mybook.dto.response.MyBookDetailDto;
import com.example.mutbooks.app.mybook.dto.response.MyBookDto;
import com.example.mutbooks.app.mybook.service.MyBookService;
import com.example.mutbooks.app.security.dto.MemberContext;
import com.example.mutbooks.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/myBooks")
@RequiredArgsConstructor
public class MyBooksApiController {
    private final MyBookService myBookService;

    // 내 도서 리스트
    @GetMapping("")
    public ResponseEntity<RsData> list(@AuthenticationPrincipal MemberContext memberContext) {
        Member member = memberContext.getMember();
        List<MyBookDto> myBookDtos = myBookService.findAllByOwner(member);

        return Ut.spring.responseEntityOf(
                RsData.successOf(Ut.mapOf("myBooks", myBookDtos))
        );
    }

    // 도서 상세 조회
    @GetMapping("/{myBookId}")
    public ResponseEntity<RsData> detail(@PathVariable long myBookId, @AuthenticationPrincipal MemberContext memberContext) {
        MyBookDetailDto myBookDetailDto = myBookService.findByIdForDetail(myBookId);

        return Ut.spring.responseEntityOf(
                RsData.successOf(Ut.mapOf("myBook", myBookDetailDto))
        );
    }
}
