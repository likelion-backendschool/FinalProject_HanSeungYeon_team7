package com.example.mutbooks.app.rebate.service;

import com.example.mutbooks.app.order.entity.OrderItem;
import com.example.mutbooks.app.order.service.OrderService;
import com.example.mutbooks.app.rebate.entity.RebateOrderItem;
import com.example.mutbooks.app.rebate.repository.RebateOrderItemRepository;
import com.example.mutbooks.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RebateService {
    private final OrderService orderService;
    private final RebateOrderItemRepository rebateOrderItemRepository;

    // 정산 데이터 생성
    @Transactional
    public void makeData(int year, int month) {
        int endDay = Ut.date.getEndDay(year, month);

        // TODO: 정산 데이터 생성가능한 년월인지 검증

        // 1. 정산 데이터를 생성할 날짜 범위 구하기
        LocalDateTime startOfDay = Ut.date.getStartOfDay(year, month, 1);   // 해당일자의 시작일시
        LocalDateTime endOfDay = Ut.date.getEndOfDay(year, month, endDay);       // 해당일자의 종료일시

        // 2. 해당 범위의 모든 주문 품목 조회
        List<OrderItem> orderItems = orderService.findAllByPayDateBetween(startOfDay, endOfDay);

        // 3. 주문 데이터 -> 정산 데이터 변환
        List<RebateOrderItem> rebateOrderItems = orderItems.stream()
                .map(this::toRebateOrderItem)
                .collect(Collectors.toList());

        // 4. 정산 데이터 생성
        rebateOrderItems.forEach(this::makeRebateOrderItem);
    }

    // RebateOrderItem 생성
    private void makeRebateOrderItem(RebateOrderItem rebateOrderItem) {
        RebateOrderItem oldRebateOrderItem = rebateOrderItemRepository.findByOrderItemId(rebateOrderItem.getOrderItem().getId())
                .orElse(null);
        // TODO: 이미 생성된 정산 데이터가 있으면 삭제하고 다시 만들기
        if(oldRebateOrderItem != null) {
            rebateOrderItemRepository.delete(oldRebateOrderItem);
        }
        rebateOrderItemRepository.save(rebateOrderItem);
    }

    // OrderItem -> RebateOrderItem 변환
    private RebateOrderItem toRebateOrderItem(OrderItem orderItem) {
        return new RebateOrderItem(orderItem);
    }
}
