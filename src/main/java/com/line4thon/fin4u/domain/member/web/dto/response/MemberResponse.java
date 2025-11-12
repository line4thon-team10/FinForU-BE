package com.line4thon.fin4u.domain.member.web.dto.response;

import com.line4thon.fin4u.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class MemberResponse {

    private String email;
    private String name;
    private String nationality;
    private Member.Language language;
    private Member.VisaType visaType;
    private Member.DesiredProductType desiredProductType;
    private Timestamp visaExpir;
    private boolean notify;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static MemberResponse from(Member m) {
        return MemberResponse.builder()
                .email(m.getEmail())
                .name(m.getName())
                .nationality(m.getNationality())
                .language(m.getLanguage())
                .visaType(m.getVisaType())
                .desiredProductType(m.getDesiredProductType())
                .visaExpir(m.getVisa_expir())
                .notify(m.isNotify())
                .createdAt(m.getCreated_at())
                .updatedAt(m.getUpdated_at())
                .build();
    }
}
