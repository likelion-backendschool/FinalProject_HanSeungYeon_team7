package com.example.mutbooks.app.global.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * Member <-> DTO 변환
 * componentModel = "spring" // 해당 Mapper 를 스프링 빈으로 등록
 * unmappedTargetPolicy = ReportingPolicy.IGNORE // 일치하지 않는 필드 무시하기
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OpenBankingAccessTokenMapper {
    OpenBankingAccessTokenMapper INSTANCE = Mappers.getMapper(OpenBankingAccessTokenMapper.class);
}
