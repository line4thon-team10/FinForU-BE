package com.line4thon.fin4u.global.crawl.repository;

import com.line4thon.fin4u.global.crawl.Entity.ForeignerStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForeignerStoreRepository extends JpaRepository<ForeignerStore, Long> {
    List<ForeignerStore> findByBankName(String bankName);
}
