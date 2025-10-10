package com.line4thon.fin4u.domain.example.service;

import com.line4thon.fin4u.domain.example.entity.Example;
import com.line4thon.fin4u.domain.example.exception.ExampleNotFoundException;
import com.line4thon.fin4u.domain.example.repository.ExampleRepository;
import com.line4thon.fin4u.domain.example.web.dto.ExampleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExampleServiceImpl implements ExampleService {

    private final ExampleRepository exampleRepository;

    @Override
    public ExampleResponse getExample(Long id) {
        Example example = exampleRepository.findById(id).orElseThrow(ExampleNotFoundException::new);
        return new ExampleResponse(
                example.getId()
        );
    }
}
