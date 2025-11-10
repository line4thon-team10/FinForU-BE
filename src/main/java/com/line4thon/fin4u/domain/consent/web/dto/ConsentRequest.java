package com.line4thon.fin4u.domain.consent.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsentRequest {

    // [Required] Agree to FinForU
    @NotNull private Boolean agreeFinforu;

    // [Required] Terms of use for users aged 14+
    @NotNull private Boolean agreeTerms14;

    // [Required] Collection and use of personal info
    @NotNull private Boolean agreePrivacy;

    // [Optional] Promotional notifications
    private Boolean agreePromotion;

    // [Optional] Marketing notifications
    private Boolean agreeMarketing;
}