package io.github.alberes.register.manager.controllers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UserAccountProfileDto(
        String id,
        String name,
        String email,
        Set<String> profiles) {
}
