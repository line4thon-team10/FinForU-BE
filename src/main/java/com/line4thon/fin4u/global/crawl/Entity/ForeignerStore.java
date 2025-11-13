package com.line4thon.fin4u.global.crawl.Entity;

import com.line4thon.fin4u.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForeignerStore extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bankName;
    private String branchName;
    private String roadAddress;
    private String zipCode;
    private LocalTime weekClose;
    private LocalTime weekendClose;
    private String phoneNum;
    private Double longitude;
    private Double latitude;

    @Builder(builderMethodName = "rebuild")
    public void build(
            String roadAddress,
            String zipCode,
            LocalTime weekClose,
            LocalTime weekendClose,
            String phoneNum,
            Double longitude,
            Double latitude) {
        this.roadAddress = roadAddress;
        this.zipCode = zipCode;
        this.weekClose = weekClose;
        this.weekendClose = weekendClose;
        this.phoneNum = phoneNum;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
