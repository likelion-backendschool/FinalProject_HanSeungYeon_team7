package com.example.mutbooks.app.withdraw.repository;

import com.example.mutbooks.app.withdraw.entity.WithdrawApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawApplyRepository extends JpaRepository<WithdrawApply, Long> {
    List<WithdrawApply> findByApplicantIdOrderByIdDesc(Long applicantId);
}
