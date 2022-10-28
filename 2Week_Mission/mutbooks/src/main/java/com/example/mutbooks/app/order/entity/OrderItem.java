package com.example.mutbooks.app.order.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import com.example.mutbooks.app.product.entity.Product;
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
public class OrderItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;            // 주문번호

    @ManyToOne(fetch =  FetchType.LAZY)
    private Product product;        // 상품번호

    private LocalDateTime payDate;  // 결제 일시
    private int price;              // 권장 판매가
    private int salePrice;          // 실제 판매가
    private int wholesalePrice;     // 도매가
    private int pgFee;              // 결제대행사 수수료
    private int payPrice;           // 결제 금액
    private int refundPrice;        // 환불 금액
    private boolean isPaid;         // 결제 여부

    public OrderItem(Product product) {
        // product 로 부터 가져온 값
        this.product = product;
        this.price = product.getPrice();
        this.salePrice = product.getSalePrice();
        this.wholesalePrice = product.getWholesalePrice();
    }

    // 결제 완료 처리
    public void setPaymentDone() {
        this.pgFee = 0;
        this.payPrice = getSalePrice();
        this.isPaid = true;
        this.payDate = LocalDateTime.now();
    }
}
