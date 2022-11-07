package com.example.mutbooks.app.withdraw.service;

import com.example.mutbooks.app.cash.entity.CashLog;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.withdraw.entity.WithdrawApply;
import com.example.mutbooks.app.withdraw.form.WithdrawApplyForm;
import com.example.mutbooks.app.withdraw.repository.WithdrawApplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WithdrawService {
    private final MemberService memberService;
    private final WithdrawApplyRepository withdrawApplyRepository;

    @Transactional
    public void apply(String username, WithdrawApplyForm withdrawApplyForm) {
        Member applicant = memberService.findByUsername(username);
        int price = withdrawApplyForm.getPrice();   // 출금 신청 금액

        if(canWithdrawApply(applicant, price)) {
            // 캐시 차감 처리
            CashLog withdrawCashLog = memberService.addCash(applicant, -1 * price, "출금");

            WithdrawApply withdrawApply = WithdrawApply.builder()
                    .applicant(applicant)
                    .bankName(applicant.getMemberExtra().getBankName())
                    .bankAccountNo(applicant.getMemberExtra().getBankAccountNo())
                    .price(price)
                    .withdrawCashLog(withdrawCashLog)
                    .build();

            withdrawApplyRepository.save(withdrawApply);
        }
    }

    // 출금 신청 가능 여부 검증
    public boolean canWithdrawApply(Member applicant, int price) {
        // 보유 금액보다 많은 금액 출금 요청시
        if(applicant.getRestCash() < price) {
            throw new RuntimeException("출금 신청 금액이 보유 금액보다 많습니다.");
        }
        return true;
    }

    public List<WithdrawApply> findByApplicantIdOrderByIdDesc(Long applicantId) {
        return withdrawApplyRepository.findByApplicantIdOrderByIdDesc(applicantId);
    }
}
