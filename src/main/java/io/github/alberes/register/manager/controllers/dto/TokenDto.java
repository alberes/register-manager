package io.github.alberes.register.manager.controllers.dto;

public record TokenDto(String id, String token, Long expirationDate) {
}