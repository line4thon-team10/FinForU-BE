package com.line4thon.fin4u.domain.specialize.web.controller;

import com.line4thon.fin4u.domain.specialize.exception.SpecializeBankNotFoundException;
import com.line4thon.fin4u.domain.specialize.web.dto.SpecializeBankRes;
import com.line4thon.fin4u.global.crawl.Entity.ForeignerStore;
import com.line4thon.fin4u.global.crawl.repository.ForeignerStoreRepository;
import com.line4thon.fin4u.global.response.SuccessResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/specialize")
@RequiredArgsConstructor
public class SpecializeBankController {
    private final ForeignerStoreRepository repository;

    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getSpecializeBankInfo() {

        List<ForeignerStore> result = repository.findAll();
        return buildAndReturnResponseEntity(result);
    }

    @GetMapping("/{bank}")
    public ResponseEntity<SuccessResponse<?>> getSpecializeBankInfo(@PathVariable @NotBlank(message = "bank pathVariable은 비어있을 수 없습니다.") String bank) {
        List<ForeignerStore> result = repository.findByBankName(bank);

        return buildAndReturnResponseEntity(result);
    }

    private ResponseEntity<SuccessResponse<?>> buildAndReturnResponseEntity(List<ForeignerStore> result) {
        if(result.isEmpty()) throw new SpecializeBankNotFoundException();

        List<SpecializeBankRes> response = result.stream()
                .map(record -> new SpecializeBankRes(
                        record.getId(),
                        record.getBankName(),
                        record.getBranchName(),
                        record.getRoadAddress(),
                        record.getZipCode(),
                        record.getWeekClose(),
                        record.getWeekendClose(),
                        record.getLongitude(),
                        record.getLatitude()
                )).toList();

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));
    }
}
