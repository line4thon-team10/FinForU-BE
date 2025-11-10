package com.line4thon.fin4u.global.crawl.web.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankBranch {
    private String bankName;        // 은행사
    private String branchName;      // 점포 이름
    private String address;         // 주소
    private String operatingDay;    // 운영일
    private String operatingTime;   // 운영시간
    private String branchId;
    @Setter
    private String phoneNum;
}
