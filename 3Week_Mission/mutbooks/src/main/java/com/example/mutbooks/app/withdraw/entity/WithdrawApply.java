package com.example.mutbooks.app.withdraw.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import com.example.mutbooks.app.cash.entity.CashLog;
import com.example.mutbooks.app.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class WithdrawApply extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member applicant;           // 신청자

    @ManyToOne(fetch = FetchType.LAZY)
    private CashLog withdrawCashLog;    // 출금 관련 캐시 내역

    private String bankName;            // 출금 신청 은행명
    private String bankAccountNo;       // 출금 신청 계좌번호
    private int price;                  // 출금 신청 금액
    private LocalDateTime withdrawDate; // 출금 일시
    private LocalDateTime cancelDate;   // 출금 취소 일시
    private boolean isWithdrawn;       // 출금 여부
    private boolean isCancelled;       // 출금 신청 취소 여부

    // 신청 완료 여부
    public boolean isAppliedStatus() {
        if(!isWithdrawn && !isCancelled) {
            return true;
        }
        return false;
    }

    // 출금 완료
    public boolean isWithdrawnStatus() {
        if(isWithdrawn) {
            return true;
        }
        return false;
    }

    // 취소 완료
    public boolean isCancelledStatus() {
        if(isCancelled) {
            return true;
        }
        return false;
    }

    // 출금 완료 처리
    public void setWithdrawDone() {
        isWithdrawn = true;
        withdrawDate = LocalDateTime.now();
    }
}
