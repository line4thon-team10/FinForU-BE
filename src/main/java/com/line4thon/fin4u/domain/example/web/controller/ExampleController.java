package com.line4thon.fin4u.domain.example.web.controller;

import com.line4thon.fin4u.domain.example.service.ExampleService;
import com.line4thon.fin4u.domain.example.web.dto.ExampleResponse;
import com.line4thon.fin4u.global.response.SuccessResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
@RequiredArgsConstructor
public class ExampleController {

    private final ExampleService exampleService;

    @GetMapping
    public ResponseEntity<SuccessResponse<?>> findExample(
            @RequestBody @NotNull Long id
    ) {
        ExampleResponse response = exampleService.getExample(id);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.ok(response));
    }
}
