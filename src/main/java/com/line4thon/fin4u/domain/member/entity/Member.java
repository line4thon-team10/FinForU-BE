package com.line4thon.fin4u.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "members")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id",unique = true, nullable = false)
    private Long memberId;

    //언어 설정은 로그인이 안되도 되야 해서 쿠키설정 필요
    public enum Language { ENGLISH, CHINESE, VIETNAMESE }
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Language language;


    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String nationality;

    //비자 유형별(비자 유형 enum 내용 변경됨)
    public enum VisaType { ACADEMIC, EMPLOYMENT, RESIDENCE_FAMILY, INVESTMENT_BUSINESS, OTHERS }
    @Convert(converter = VisaTypeConverter.class)
    @Column(name = "visa_type", nullable = false, length = 50)
    private VisaType visaType;

    @Column(nullable = false, columnDefinition = "timestamp")
    private Timestamp visa_expir;

    //DesiredProductType (새로 추가됨): 여러 개 선택
    public enum DesiredProductType { CARD, DEPOSIT, INSTALLMENT_SAVINGS }
    @ElementCollection(fetch = FetchType.EAGER) // 필요시 LAZY
    @CollectionTable(
            name = "member_desired_products",
            joinColumns = @JoinColumn(name = "member_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false, length = 50)
    private Set<DesiredProductType> desiredProductTypes = new HashSet<>();

    @Column(nullable = false, columnDefinition = "TINYINT(1) default 1")
    private boolean notify;

    //이 행이 처음 INSERT될 때, 현재 시각을 자동으로 넣음
    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "timestamp default current_timestamp")
    private Timestamp created_at;

    //처음엔 비워두지만, 나중에 UPDATE가 일어나면 현재 시각으로 자동 갱신
    @UpdateTimestamp
    @Column(columnDefinition = "timestamp null on update current_timestamp")
    private Timestamp updated_at;

    //생성 시간(created_at) 과 최종 수정 시간(updated_at) 을 DB에서 자동으로 채우거나 유지
    @PrePersist
    public void onInsert() {
        if (desiredProductTypes == null || desiredProductTypes.isEmpty()) {
            desiredProductTypes = new HashSet<>(Set.of(DesiredProductType.CARD));
        }
        // 기존 created_at/updated_at 처리 그대로
    }
}

