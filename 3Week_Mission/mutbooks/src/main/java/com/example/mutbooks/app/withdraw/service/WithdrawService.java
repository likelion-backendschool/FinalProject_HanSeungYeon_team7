package com.example.mutbooks.app.withdraw.service;

import com.example.mutbooks.app.cash.entity.CashLog;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.withdraw.entity.WithdrawApply;
import com.example.mutbooks.app.withdraw.exception.WithdrawApplyNotFoundException;
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

    public List<WithdrawApply> findAll() {
        return withdrawApplyRepository.findAll();
    }

    public WithdrawApply findById(long id) {
        return withdrawApplyRepository.findById(id).orElseThrow(() -> {
            throw new WithdrawApplyNotFoundException("출금 신청 내역이 존재하지 않습니다.");
        });
    }

    @Transactional
    public void withdraw(long id) {
        WithdrawApply withdrawApply = findById(id);
        // TODO: 해당 계좌로 입금 요청 API 호출
        withdrawApply.setWithdrawDone();
    }

    // 사용자 본인 출금 취소
    @Transactional
    public void cancel(String username, long id) {
        WithdrawApply withdrawApply = findById(id);
        Member member = memberService.findByUsername(username);

        if(canCancel(member, withdrawApply)) {
            memberService.addCash(member, withdrawApply.getPrice(), "출금취소__캐시");
            withdrawApply.setCancelDone("사용자 취소");
        }
    }

    // 관리자에 의한 출금 취소
    @Transactional
    public void cancelByAdmin(String username, long id) {
        WithdrawApply withdrawApply = findById(id);
        Member member = memberService.findByUsername(username);

        memberService.addCash(member, withdrawApply.getPrice(), "출금취소__캐시");
        withdrawApply.setCancelDone("관리자 취소");
    }

    // 취소 권한 검증
    public boolean canCancel(Member member, WithdrawApply withdrawApply) {
        if(!member.getId().equals(withdrawApply.getApplicant().getId())) {
            throw new RuntimeException("해당 출금 신청 내역의 취소 권한이 없습니다.");
        }
        return true;
    }
}
