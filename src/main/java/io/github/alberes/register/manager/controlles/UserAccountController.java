package io.github.alberes.register.manager.controlles;

import io.github.alberes.register.manager.controlles.dto.UserAccountDto;
import io.github.alberes.register.manager.controlles.dto.UserAccountUpdateDto;
import io.github.alberes.register.manager.controlles.mappers.UserAccountMapper;
import io.github.alberes.register.manager.domains.UserAccount;
import io.github.alberes.register.manager.domains.UserPrincipal;
import io.github.alberes.register.manager.services.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserAccountController implements GenericController{

    private final UserAccountService service;

    private final UserAccountMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> save(@RequestBody @Valid UserAccountDto dto){
        UserAccount userAccount = this.mapper.toEntity(dto);
        userAccount.setRoles(new HashSet<String>());
        userAccount.getRoles().add(dto.role().toUpperCase());
        userAccount = this.service.save(userAccount);
        return ResponseEntity
                .created(this.createURI("/{id}", userAccount.getId().toString()))
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<UserAccountDto> find(@PathVariable String id){
        UUID userId = UUID.fromString(id);
        UserAccount userAccount = this.service.find(userId);
        userAccount.setPassword(null);
        UserAccountDto dto = this.mapper.toDto(userAccount);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody @Valid UserAccountUpdateDto dto){
        UUID userId = UUID.fromString(id);
        UserAccount userAccount = new UserAccount();
        userAccount.setId(userId);
        userAccount.setName(dto.name());
        this.service.update(userAccount);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<Void> delete(@PathVariable String id){
        UUID userId = UUID.fromString(id);
        this.service.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
