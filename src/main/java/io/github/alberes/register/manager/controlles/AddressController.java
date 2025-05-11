package io.github.alberes.register.manager.controlles;

import io.github.alberes.register.manager.controlles.dto.AddressDto;
import io.github.alberes.register.manager.controlles.mappers.AddressMapper;
import io.github.alberes.register.manager.domains.Address;
import io.github.alberes.register.manager.services.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/{userId}/address")
@RequiredArgsConstructor
public class AddressController implements GenericController{

    private final AddressService service;

    private final AddressMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<Void> save(@PathVariable String userId, @RequestBody @Valid AddressDto dto){
        Address address = this.mapper.toEntity(dto);
        address = this.service.save(address);

        return ResponseEntity.created(this.createURI("/{id}", address.getId().toString()))
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<AddressDto> find(@PathVariable String userId, @PathVariable String id){
        UUID addressId = UUID.fromString(id);
        Address address = this.service.find(addressId);
        AddressDto dto = this.mapper.toDto(address);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<Void> update(@PathVariable String userId, @PathVariable String id, @RequestBody @Valid AddressDto dto){
        Address address = this.mapper.toEntity(dto);
        UUID addressId = UUID.fromString(id);
        address.setId(addressId);
        this.service.update(address);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<Void> delete(@PathVariable String userId, @PathVariable String id){
        UUID addressId = UUID.fromString(id);
        this.service.delete(addressId);
        return ResponseEntity.noContent().build();
    }

}
