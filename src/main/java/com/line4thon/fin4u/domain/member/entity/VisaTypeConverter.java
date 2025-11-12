package com.line4thon.fin4u.domain.member.entity;

import com.line4thon.fin4u.domain.member.entity.Member;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class VisaTypeConverter implements AttributeConverter<Member.VisaType, String> {

    @Override
    public String convertToDatabaseColumn(Member.VisaType attr) {
        if (attr == null) return "ACCOUNT_OPEN"; // DB가 허용하는 기본값

        // 새 enum → DB 옛 문자열 (★ 반드시 DB ENUM 멤버 중 하나만 반환)
        return switch (attr) {
            case ACADEMIC,
                 EMPLOYMENT,
                 RESIDENCE_FAMILY,
                 OTHERS -> "ACCOUNT_OPEN";
            case INVESTMENT_BUSINESS -> "CARD_AVAILABLE";
        };
    }

    @Override
    public Member.VisaType convertToEntityAttribute(String db) {
        if (db == null) return Member.VisaType.OTHERS;

        // DB 옛 문자열 → 새 enum
        return switch (db) {
            case "CARD_AVAILABLE" -> Member.VisaType.INVESTMENT_BUSINESS;
            case "ACCOUNT_OPEN", "" -> Member.VisaType.OTHERS; // 안전 맵핑
            default -> Member.VisaType.OTHERS; // 혹시 모르는 값
        };
    }
}