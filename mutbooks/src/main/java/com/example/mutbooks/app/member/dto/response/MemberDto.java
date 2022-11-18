package com.example.mutbooks.app.member.dto.response;

import com.example.mutbooks.app.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private String username;
    private String email;
    private boolean emailVerified;
    private String nickname;

    public static MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .createDate(member.getCreateDate())
                .modifyDate(member.getUpdateDate())
                .username(member.getUsername())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }
}
