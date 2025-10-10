package com.line4thon.fin4u.domain.example.repository;

import com.line4thon.fin4u.domain.example.entity.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {
    Optional<Example> findById(Long id);
}
