package com.example.mutbooks.app.rebate.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import com.example.mutbooks.app.cash.entity.CashLog;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.order.entity.Order;
import com.example.mutbooks.app.order.entity.OrderItem;
import com.example.mutbooks.app.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class RebateOrderItem extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private OrderItem orderItem;                // 주문품목 번호
    private LocalDateTime orderItemCreateDate;  // 주문품목 생성일시

    /**
     * orderItem
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Order order;            // 주문번호

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product product;        // 상품번호
    private String productSubject;  // 상품명

    private LocalDateTime payDate;      // 결제 일시
    private LocalDateTime refundDate;   // 환불 일시

    private int price;              // 권장 판매가
    private int salePrice;          // 실제 판매가
    private int wholesalePrice;     // 도매가
    private int pgFee;              // 결제대행사 수수료
    private int payPrice;           // 결제 금액
    private int refundPrice;        // 환불 금액

    private boolean isPaid;         // 결제 여부
    private boolean isRefunded;     // 환불 여부

    /**
     * member
     */
    // 구매자 회원
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member buyer;           // 구매자
    private String buyerName;       // 구매자명

    // 판매자 회원
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member seller;           // 판매자
    private String sellerName;       // 판매자명

    /**
     * CashLog
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private CashLog rebateCashLog;      // 정산금액 지급 내역
    private LocalDateTime rebateDate;   // 정산금액 지급 일시
    private boolean isRebated;          // 정산 여부

    public RebateOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;

        order = orderItem.getOrder();
        product = orderItem.getProduct();
        price = orderItem.getPrice();
        salePrice = orderItem.getSalePrice();
        wholesalePrice = orderItem.getWholesalePrice();
        pgFee = orderItem.getPgFee();
        payPrice = orderItem.getPayPrice();
        refundPrice = orderItem.getRefundPrice();
        payDate = orderItem.getPayDate();
        refundDate = orderItem.getRefundDate();
        isPaid = orderItem.isPaid();
        isRefunded = orderItem.isRefunded();

        // 상품 추가데이터
        productSubject = orderItem.getProduct().getSubject();

        // 주문품목 추가데이터
        orderItemCreateDate = orderItem.getCreateDate();

        // 구매자 추가데이터
        buyer = orderItem.getOrder().getBuyer();
        buyerName = orderItem.getOrder().getBuyer().getUsername();

        // 판매자 추가데이터
        seller = orderItem.getProduct().getAuthor();
        sellerName = orderItem.getProduct().getAuthor().getUsername();
    }

    // 예상 정산 금액 계산
    public int calculateRebatePrice() {
        if(isRefunded) {
            return 0;
        }
        return payPrice - pgFee - wholesalePrice;
    }

    // 정산 가능 여부
    public boolean isRebateAvailable() {
        // 전액 환불건 or 정산 완료건은 정산 불가
        if(isRefunded || isRebated) {
            return false;
        }
        return true;
    }

    // 정산 완료 처리
    public void setRebateDone(CashLog cashLog) {
        rebateDate = LocalDateTime.now();
        rebateCashLog = cashLog;
        isRebated = true;
    }
}
