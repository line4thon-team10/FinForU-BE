package com.line4thon.fin4u.domain.member.web.dto.request;

import com.line4thon.fin4u.domain.member.entity.Member;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class SignupRequest {

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 8, max = 64)
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String nationality;

    @NotNull
    private Member.Language language;

    @NotNull
    private Member.VisaType visaType;

    @NotNull
    private Timestamp visaExpir; // yyyy-MM-ddTHH:mm:ss 형태로 매핑됨

    private Boolean notify = true;
}
