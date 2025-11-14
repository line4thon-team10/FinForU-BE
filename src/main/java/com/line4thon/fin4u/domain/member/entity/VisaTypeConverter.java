package com.line4thon.fin4u.domain.member.entity;

import com.line4thon.fin4u.domain.member.entity.Member;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class VisaTypeConverter implements AttributeConverter<Member.VisaType, String> {

    @Override
    public String convertToDatabaseColumn(Member.VisaType attr) {
        if (attr == null) return "OTHERS";

        return switch (attr) {
            case ACADEMIC            -> "ACADEMIC";
            case EMPLOYMENT          -> "EMPLOYMENT";
            case RESIDENCE_FAMILY    -> "RESIDENCE_FAMILY";
            case INVESTMENT_BUSINESS -> "INVESTMENT_BUSINESS";
            case OTHERS              -> "OTHERS";
        };
    }


    @Override
    public Member.VisaType convertToEntityAttribute(String db) {
        if (db == null || db.isBlank()) return Member.VisaType.OTHERS;

        return switch (db) {
            // 레거시 값 → 새 enum
            case "ACCOUNT_OPEN"   -> Member.VisaType.OTHERS;
            case "CARD_AVAILABLE" -> Member.VisaType.INVESTMENT_BUSINESS;

            // 새 값 그대로
            case "ACADEMIC"            -> Member.VisaType.ACADEMIC;
            case "EMPLOYMENT"          -> Member.VisaType.EMPLOYMENT;
            case "RESIDENCE_FAMILY"    -> Member.VisaType.RESIDENCE_FAMILY;
            case "INVESTMENT_BUSINESS" -> Member.VisaType.INVESTMENT_BUSINESS;
            case "OTHERS"              -> Member.VisaType.OTHERS;
            default                    -> Member.VisaType.OTHERS;
        };
    }
}