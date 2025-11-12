package com.line4thon.fin4u.domain.product.entity;

import com.line4thon.fin4u.domain.member.entity.Member;
import com.line4thon.fin4u.domain.product.entity.enums.Type;
import com.line4thon.fin4u.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name="saving_product")
public class Comparison extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="type", nullable = false)
    private Type type;

    @Column(name="product_id", nullable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String guestToken;

    public static Comparison of(Member member, Type type, Long id){
        return Comparison.builder()
                .member(member)
                .type(type)
                .productId(id)
                .build();
    }

    public static Comparison ofGuest(String guestToken, Type type, Long id){
        return Comparison.builder()
                .guestToken(guestToken)
                .type(type)
                .productId(id)
                .build();
    }
}
