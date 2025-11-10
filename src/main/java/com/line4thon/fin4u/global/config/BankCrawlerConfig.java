package com.line4thon.fin4u.global.config;

import com.line4thon.fin4u.global.crawl.BankCode;
import com.line4thon.fin4u.global.crawl.component.CrawlerItemProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import com.line4thon.fin4u.global.crawl.Entity.ForeignerStore;
import com.line4thon.fin4u.global.crawl.repository.ForeignerStoreRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BankCrawlerConfig {

    private final CrawlerItemProcessor processor;
    private final ForeignerStoreRepository repository;
    /**
     * ItemReader(R): Processor에게 "무엇을" 크롤링할지 (은행 코드) 전달합니다.
     * 여기서는 간단히 하드코딩된 리스트를 사용합니다.
     */
    @Bean
    public ItemReader<String> bankCodeReader() {
        List<String> bankNamesToCrawl = List.of(
                BankCode.KOOKMIN.getCode(),
                BankCode.WOORI.getCode(),
                BankCode.HANA.getCode(),
                BankCode.SHINHAN.getCode()
        );
        return new ListItemReader<>(bankNamesToCrawl);
    }
    @Bean
    public ItemWriter<List<ForeignerStore>> foreignerStoreListWriter() {
        return items -> {
            for (List<ForeignerStore> storeListPerBankCode : items) {
                repository.saveAll(storeListPerBankCode);
            }
        };
    }
    @Bean
    public Step processBankStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager) {

        return new StepBuilder("processBankStep", jobRepository)
                .<String, List<ForeignerStore>>chunk(1, transactionManager)
                .reader(bankCodeReader())
                .processor(processor)
                .writer(foreignerStoreListWriter())
                .build();
    }

    @Bean
    public Job crawlBankJob(Step processBankStep,
                            JobRepository jobRepository) {
        return new JobBuilder("crawlBankJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(processBankStep)
                .end()
                .build();
    }
}