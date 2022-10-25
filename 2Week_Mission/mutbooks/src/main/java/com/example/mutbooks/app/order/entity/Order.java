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
    private Boolean readyStatus;        // 주문완료 여부
    private Boolean isPaid;             // 결제완료 여부
    private Boolean isCanceled;         // 주문취소 여부
    private Boolean isRefunded;         // 환불 여부

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();     // 주문 품목 리스트
}
