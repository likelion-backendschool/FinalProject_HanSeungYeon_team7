package com.example.mutbooks.scheduler;

import com.example.mutbooks.job.makeRebateOrderItem.MakeRebateOrderItemJobConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobScheduler {
    private final JobLauncher jobLauncher;  // Job 실행 객체

    private final MakeRebateOrderItemJobConfig makeRebateOrderItemJobConfig;
    private final Job makeRebateOrderItemJob;   // 빈으로 등록한 Job 을 주입받아 사용


    // 매달 15일 오전 4시에 Job 실행해야하지만, 오류를 고려하여 매일 오전 4시에 실행
//    @Scheduled(cron = "0 0 4 * * *")    // TODO: 운영시 주석 해제
    @Scheduled(cron = "30 * * * * *")    // 개발용(매분 30초마다 실행)
    public void runJob() {
        log.info("scheduler 실행 " + String.valueOf(LocalDateTime.now()));

        // 현재 일시 LocalDateTime -> String 변환한 값을 Job Parameter 에 담기
        Map<String, JobParameter> confMap = new HashMap<>();
        LocalDateTime rebateDate = getMakeRebateDataDateTime();
        confMap.put("year", new JobParameter((long) rebateDate.getYear()));
        confMap.put("month", new JobParameter((long) rebateDate.getMonthValue()));
        JobParameters jobParameters = new JobParameters(confMap);

        try {
            jobLauncher.run(makeRebateOrderItemJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            throw new RuntimeException(e);
        } catch (JobRestartException e) {
            throw new RuntimeException(e);
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new RuntimeException(e);
        } catch (JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }

    // jobParameter 값 생성
    public LocalDateTime getMakeRebateDataDateTime() {
        // 정산 데이터 생성 날짜 범위(15일 이후 = 1달 전, 15일 이전 = 2달 전)
        // TODO : 운영시 주석 해제
//        return LocalDateTime.now().getDayOfMonth() >= 15 ?
//                LocalDateTime.now().minusMonths(1) : LocalDateTime.now().minusMonths(2);
        return LocalDateTime.now(); // 개발용
    }
}
