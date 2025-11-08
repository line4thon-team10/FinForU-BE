package com.line4thon.fin4u.global.crawl.dto;

public record BankBranch(
        String type,
        String bankName,
        String branchName,
        String address,
        String operatingDay,
        String operatingTime,
        String info
) {
}
