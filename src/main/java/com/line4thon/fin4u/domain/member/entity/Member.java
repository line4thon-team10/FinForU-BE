package com.line4thon.fin4u.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;


@Entity
@Table(name = "members")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id",unique = true, nullable = false)
    private Long memberId;

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

    //비자 유형별로
    public enum VisaType { ACCOUNT_OPEN, CARD_AVAILABLE }
    @Enumerated(EnumType.STRING)
    @Column(name = "visa_type", nullable = false, length = 50)
    private VisaType visaType;

    @Column(nullable = false, columnDefinition = "timestamp")
    private Timestamp visa_expir;

    @Column(nullable = false, columnDefinition = "TINYINT(1) default 1")
    private boolean notify;

    //이 행이 처음 INSERT될 때, 현재 시각을 자동으로 넣음
    @Column(nullable = false, columnDefinition = "timestamp default current_timestamp")
    private Timestamp created_at;

    //처음엔 비워두지만, 나중에 UPDATE가 일어나면 현재 시각으로 자동 갱신
    @Column(columnDefinition = "timestamp null on update current_timestamp")
    private Timestamp updated_at;

    //생성 시간(created_at) 과 최종 수정 시간(updated_at) 을 DB에서 자동으로 채우거나 유지
    @PrePersist
    public void onInsert() {
        if (created_at == null) created_at = Timestamp.from(Instant.now());
        if (updated_at == null) updated_at = created_at;
    }

    //UPDATE 쿼리가 실행되기 직전에 자동으로 updated_at이 자동으로 현재 시각으로 변경
    @PreUpdate
    public void onUpdate() {
        updated_at = Timestamp.from(Instant.now());
    }
}

