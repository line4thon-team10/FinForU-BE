package com.line4thon.fin4u.domain.preference.entity;

import com.line4thon.fin4u.domain.product.entity.enums.Type;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductTypePreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pref_id")
    private Preference preference;

    public ProductTypePreference(Type type, Preference preference) {
        this.type = type;
        this.preference = preference;
    }


}
