package io.github.alberes.register.manager.controllers;

import io.github.alberes.register.manager.constants.MessageConstants;
import io.github.alberes.register.manager.controllers.dto.UserAccountDto;
import io.github.alberes.register.manager.controllers.dto.UserAccountProfileDto;
import io.github.alberes.register.manager.controllers.dto.UserAccountReportDto;
import io.github.alberes.register.manager.controllers.dto.UserAccountUpdateDto;
import io.github.alberes.register.manager.controllers.mappers.UserAccountMapper;
import io.github.alberes.register.manager.domains.UserAccount;
import io.github.alberes.register.manager.domains.UserPrincipal;
import io.github.alberes.register.manager.services.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    @PreAuthorize(MessageConstants.HAS_ROLE_ADMIN)
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
    @PreAuthorize(MessageConstants.HAS_ROLE_ADMIN_USER)
    public ResponseEntity<UserAccountReportDto> find(@PathVariable String id){
        UUID userId = UUID.fromString(id);
        UserAccount userAccount = this.service.find(userId);
        userAccount.setPassword(null);
        UserAccountReportDto dto = this.mapper.toDto(userAccount);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize(MessageConstants.HAS_ROLE_ADMIN_USER)
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody @Valid UserAccountUpdateDto dto){
        UUID userId = UUID.fromString(id);
        UserAccount userAccount = new UserAccount();
        userAccount.setId(userId);
        userAccount.setName(dto.name());
        this.service.update(userAccount);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(MessageConstants.HAS_ROLE_ADMIN_USER)
    public ResponseEntity<Void> delete(@PathVariable String id){
        UUID userId = UUID.fromString(id);
        this.service.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/all")
    @PreAuthorize(MessageConstants.HAS_ROLE_ADMIN_USER)
    public ResponseEntity<Page<UserAccountReportDto>> page(
            @PathVariable String id,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ){
        Page<UserAccount> pageUsers = this.service.findPage(UUID.fromString(id), page, linesPerPage, orderBy, direction);

        if(pageUsers.getTotalElements() == 0){
            return ResponseEntity.noContent().build();
        }
        Page<UserAccountReportDto> pageReport = pageUsers
                .map(u -> new UserAccountReportDto(
                        u.getId().toString(), u.getName(), u.getEmail(), u.getLastModifiedDate(), u.getCreatedDate()));
        return ResponseEntity.ok(pageReport);
    }

    @GetMapping("/authenticated")
    @PreAuthorize(MessageConstants.HAS_ROLE_ADMIN_USER)
    public ResponseEntity<UserAccountProfileDto> userAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
        UserAccountProfileDto userAccountProfileDto = this.mapper.toProfileDto(userPrincipal.getUserAccount());
        return ResponseEntity.ok(userAccountProfileDto);
    }

}
