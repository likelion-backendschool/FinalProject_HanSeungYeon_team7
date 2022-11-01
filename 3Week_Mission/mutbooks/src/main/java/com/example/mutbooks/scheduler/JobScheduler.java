package com.example.mutbooks.scheduler;

import com.example.mutbooks.job.makeRebateOrderItem.MakeRebateOrderItemJobConfig;
import com.example.mutbooks.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
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
    private final Step makeRebateOrderItemStep1;

    //매 분 00초마다 실행
//    @Scheduled(cron = "0 * * * * *")

    // 매달 15일 오전 4시 0분 0초마다 Job 실행
    @Scheduled(cron = "0 0 4 15 * *")
    public void runJob() {
        log.info("scheduler 실행 " + String.valueOf(LocalDateTime.now()));

        // 현재 일시 LocalDateTime -> String 변환한 값을 Job Parameter 에 담기
        Map<String, JobParameter> confMap = new HashMap<>();
        String createDateStr = Ut.date.format(LocalDateTime.now());
        confMap.put("createDate", new JobParameter(createDateStr));
        JobParameters jobParameters = new JobParameters(confMap);

        try {
            jobLauncher.run(makeRebateOrderItemJobConfig.makeRebateOrderItemJob(makeRebateOrderItemStep1), jobParameters);
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
}
