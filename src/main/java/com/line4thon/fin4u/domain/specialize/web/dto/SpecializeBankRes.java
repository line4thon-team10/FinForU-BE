package com.line4thon.fin4u.domain.specialize.web.dto;

public record SpecializeBankRes(
        Long id,
        String bankName,
        String branchName,
        String roadAddress,
        String zipCode,
        java.time.LocalTime weekClose,
        java.time.LocalTime sundayClose,
        Double longitude,
        Double latitude
) {
}
