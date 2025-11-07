package com.line4thon.fin4u.domain.member.web.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank @Email
    private String email;

    @NotBlank
    private String password;
}
