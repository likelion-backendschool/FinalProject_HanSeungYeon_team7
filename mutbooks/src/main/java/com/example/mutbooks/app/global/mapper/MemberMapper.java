package com.example.mutbooks.app.global.mapper;

import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.form.JoinForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * Member <-> DTO 변환
 * componentModel = "spring" // 해당 Mapper 를 스프링 빈으로 등록
 * unmappedTargetPolicy = ReportingPolicy.IGNORE // 일치하지 않는 필드 무시하기
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "restCash", constant = "0")
    Member JoinFormToEntity(JoinForm joinForm);
}
