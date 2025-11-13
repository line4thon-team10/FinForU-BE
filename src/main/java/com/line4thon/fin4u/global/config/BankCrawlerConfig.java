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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    @Transactional
    public ItemWriter<List<ForeignerStore>> foreignerStoreListWriter() {
        return items -> {
            for (List<ForeignerStore> storeListPerBankCode : items) {
                if(storeListPerBankCode.isEmpty()) continue;

                String bankName = storeListPerBankCode.getFirst().getBankName();
                List<ForeignerStore> existingStores = repository.findByBankName(bankName);

                Map<String, ForeignerStore> existingMap = existingStores.stream()
                        .collect(
                                Collectors
                                        .toMap(
                                                ForeignerStore::getBankName,
                                                Function.identity()
                                        )
                        );

                List<ForeignerStore> toSave = new ArrayList<>();

                for(ForeignerStore record : storeListPerBankCode) {
                    ForeignerStore existing = existingMap.get(record.getBranchName());

                    if(existing != null) {
                        existing.rebuild()
                                .zipCode(existing.getZipCode())
                                .weekClose(existing.getWeekClose())
                                .weekendClose(existing.getWeekendClose())
                                .phoneNum(existing.getPhoneNum())
                                .longitude(existing.getLongitude())
                                .latitude(existing.getLatitude())
                                .build();
                        toSave.add(record);

                        existingMap.remove(record.getBranchName());
                    } else {
                        toSave.add(record);
                    }
                }

                List<ForeignerStore> toDelete = new ArrayList<>(existingMap.values());

                if(!toSave.isEmpty()) {
                    repository.saveAll(toSave);
                }
                if(!toDelete.isEmpty()) {
                    repository.deleteAll(toDelete);
                }
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