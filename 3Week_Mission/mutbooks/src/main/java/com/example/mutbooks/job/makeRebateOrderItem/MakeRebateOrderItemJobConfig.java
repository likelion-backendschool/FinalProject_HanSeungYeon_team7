package com.example.mutbooks.job.makeRebateOrderItem;

import com.example.mutbooks.app.order.entity.OrderItem;
import com.example.mutbooks.app.order.repository.OrderItemRepository;
import com.example.mutbooks.app.rebate.entity.RebateOrderItem;
import com.example.mutbooks.app.rebate.repository.RebateOrderItemRepository;
import com.example.mutbooks.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MakeRebateOrderItemJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final OrderItemRepository orderItemRepository;              // 읽기 대상
    private final RebateOrderItemRepository rebateOrderItemRepository;  // 쓰기 대상

    // 매월 15일 오전 4시 0분 0초에 이전 달(1일~말일) 정산 데이터 생성
    @Bean
    public Job makeRebateOrderItemJob(Step makeRebateOrderItemStep1) {
        log.info("makeRebateOrderItemJob 실행");
        return jobBuilderFactory.get("makeRebateOrderItemJob")
                .start(makeRebateOrderItemStep1)
                .build();
    }

    @Bean
    @JobScope
    public Step makeRebateOrderItemStep1(
            ItemReader orderItemReader,
            ItemProcessor orderItemToRebateOrderItemProcessor,
            ItemWriter RebateOrderItemWriter
    ) {
        log.info("makeRebateOrderItemStep1 실행");
        return stepBuilderFactory.get("makeRebateOrderItemStep1")
                .<OrderItem, RebateOrderItem>chunk(100) // 100개씩 처리
                .reader(orderItemReader)
                .processor(orderItemToRebateOrderItemProcessor)
                .writer(RebateOrderItemWriter)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<OrderItem> orderItemReader(
            @Value("#{jobParameters[createDate]}") String createDateStr
    ) {
        // 1. 정산 데이터를 생성할 날짜 범위 구하기
        // 이번 달 15일에 생성해야하는 정산 데이터 날짜 범위 = 저번 달 1일 ~ 말일
        LocalDateTime createDate = Ut.date.parse(createDateStr);
        LocalDateTime targetDate = createDate.minusMonths(1);

        log.info(String.valueOf(createDate));
        log.info(String.valueOf(targetDate));

        int year = targetDate.getYear();
        int month = targetDate.getMonthValue();
        int endDay = Ut.date.getEndDay(year, month);
        LocalDateTime startOfDay = Ut.date.getStartOfDay(year, month, 1);   // 해당일자의 시작일시
        LocalDateTime endOfDay = Ut.date.getEndOfDay(year, month, endDay);       // 해당일자의 종료일시
        // 2. 해당 범위의 모든 주문 품목 조회
        return new RepositoryItemReaderBuilder<OrderItem>()
                .name("orderItemReader")
                .repository(orderItemRepository)
                .methodName("findAllByPayDateBetween")
                .pageSize(100)
                .arguments(Arrays.asList(startOfDay, endOfDay))   // 메서드 인자
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<OrderItem, RebateOrderItem> orderItemToRebateOrderItemProcessor() {
        // 3. 주문 데이터 -> 정산 데이터 변환
        return orderItem -> new RebateOrderItem(orderItem);
    }

    @StepScope
    @Bean
    public ItemWriter<RebateOrderItem> RebateOrderItemWriter() {
        // 4. 정산 데이터 생성
        return items -> items.forEach(item -> {
            RebateOrderItem oldRebateOrderItem = rebateOrderItemRepository.findByOrderItemId(item.getOrderItem().getId())
                    .orElse(null);
            // 이미 생성된 정산 데이터가 있으면 삭제하고 다시 만들기
            if(oldRebateOrderItem != null) {
                rebateOrderItemRepository.delete(oldRebateOrderItem);
            }
            rebateOrderItemRepository.save(item);
        });
    }
}
