package com.line4thon.fin4u.global.crawl.web.controller;

import com.line4thon.fin4u.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {

    private final JobLauncher launcher;
    @Qualifier("crawlBankJob")
    private final Job crawlBankJob;

    @GetMapping
    public ResponseEntity<SuccessResponse<?>> runJob() {
        log.info("크롤링 수동 실행");
        try {
            launcher.run(crawlBankJob, new JobParameters());
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.empty());
    }
}
