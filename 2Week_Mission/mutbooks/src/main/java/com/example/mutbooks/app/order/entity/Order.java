package com.example.mutbooks.app.order.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import com.example.mutbooks.app.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "product_order")
public class Order extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member buyer;               // 구매자

    private String name;                // 주문명
    private LocalDateTime payDate;      // 결제 일시
    private boolean readyStatus;        // 주문완료 여부
    private boolean isPaid;             // 결제완료 여부
    private boolean isCanceled;         // 주문취소 여부
    private boolean isRefunded;         // 환불 여부

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();     // 주문 품목 리스트

    // 해당 주문에 주문 품목 추가
    public void addOrderItem(OrderItem orderItem) {
        // 주문 품목이 속해있는 주문 지정
        orderItem.setOrder(this);
        orderItems.add(orderItem);
    }

    // 주문명 네이밍
    public void makeName() {
        String name = orderItems.get(0).getProduct().getSubject();
        // 2건 이상일 경우 1번 주문 품목 제목 외 ?건 형식으로
        if(orderItems.size() > 1) {
            name += " 외 %d개".formatted(orderItems.size() - 1);
        }
        this.name = name;
    }

    // 주문 완료 처리
    public void setOrderDone() {
        this.readyStatus = true;
    }

    // 주문 취소 처리
    public void setCancelDone() {
        this.isCanceled = true;
        // TODO: 주문완료 여부를 false 로 다시 바꾸는게 맞는지
        this.readyStatus = false;
    }

    // 총 주문(상품) 금액
    public long getPayPrice() {
        // 상품들의 실제 판매가의 총합
        long payPrice = 0;
        for(OrderItem orderItem : orderItems) {
            payPrice += orderItem.getSalePrice();
        }
        return payPrice;
    }

    // 주문 취소 가능 여부
    public boolean isCancellable() {
        if(!readyStatus) return false;
        if(isPaid) return false;

        return true;
    }

    // 결제 가능 여부
    public boolean isPayable() {
        if(!readyStatus) return false;
        if(isPaid) return false;
        if(isCanceled) return false;

        return true;
    }
}