package io.github.alberes.register.manager.controllers.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record AddressReportDto(
        String id,
        String publicArea,
        Integer number,
        String additionalAddress,
        String neighborhood,
        String city,
        String state,
        String zipCode,
        LocalDateTime createdDate) {
}
