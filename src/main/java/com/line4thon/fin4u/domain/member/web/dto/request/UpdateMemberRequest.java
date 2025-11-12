package com.line4thon.fin4u.domain.member.web.dto.request;


import com.line4thon.fin4u.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter @Setter
public class UpdateMemberRequest {
    private String nationality;
    private Member.Language language;
    private Member.VisaType visaType;
    private Timestamp visaExpir;
    private Boolean notify;
    private Member.DesiredProductType desiredProductType;
}
