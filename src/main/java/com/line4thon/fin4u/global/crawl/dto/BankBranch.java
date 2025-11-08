package com.line4thon.fin4u.global.crawl.dto;

public record BankBranch(
        String type,            // 점포 유형
        String bankName,        // 은행사
        String branchName,      // 점포 이름
        String address,         // 주소
        String operatingDay,    // 운영일
        String operatingTime,   // 운영시간
        String info             // 안내
) {
}
