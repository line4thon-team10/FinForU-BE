package com.line4thon.fin4u.domain.product.entity;

import com.line4thon.fin4u.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Bank extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_id")
    private Long id;

    @Column(name = "bank_name", nullable = false, unique = true)
    private String bankName;
}
