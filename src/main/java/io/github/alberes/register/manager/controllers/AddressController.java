package io.github.alberes.register.manager.controllers;

import io.github.alberes.register.manager.constants.MessageConstants;
import io.github.alberes.register.manager.controllers.dto.AddressDto;
import io.github.alberes.register.manager.controllers.dto.AddressReportDto;
import io.github.alberes.register.manager.controllers.dto.AddressViaCEPDto;
import io.github.alberes.register.manager.controllers.mappers.AddressMapper;
import io.github.alberes.register.manager.domains.Address;
import io.github.alberes.register.manager.domains.UserAccount;
import io.github.alberes.register.manager.services.AddressService;
import io.github.alberes.register.manager.services.ViaCEPService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/{userId}/addresses")
@RequiredArgsConstructor
public class AddressController implements GenericController{

    private final AddressService service;

    private final ViaCEPService viaCEPService;

    private final AddressMapper mapper;

    @PostMapping
    @PreAuthorize(MessageConstants.HAS_ROLE_ADMIN_USER)
    public ResponseEntity<Void> save(@PathVariable String userId, @RequestBody @Valid AddressDto dto){
        Address address = this.mapper.toEntity(dto);
        address.setUserAccount(new UserAccount());
        address.getUserAccount().setId(UUID.fromString(userId));
        address = this.service.save(address);
        return ResponseEntity.created(this.createURI("/{id}", address.getId().toString()))
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize(MessageConstants.HAS_ROLE_ADMIN_USER)
    public ResponseEntity<AddressReportDto> find(@PathVariable String userId, @PathVariable String id){
        UUID addressId = UUID.fromString(id);
        Address address = this.service.find(UUID.fromString(userId), addressId);
        AddressReportDto dto = this.mapper.toDto(address);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize(MessageConstants.HAS_ROLE_ADMIN_USER)
    public ResponseEntity<Void> update(@PathVariable String userId, @PathVariable String id, @RequestBody @Valid AddressDto dto){
        Address address = this.mapper.toEntity(dto);
        UUID addressId = UUID.fromString(id);
        address.setId(addressId);
        address.setUserAccount(new UserAccount());
        address.getUserAccount().setId(UUID.fromString(userId));
        this.service.update(address);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(MessageConstants.HAS_ROLE_ADMIN_USER)
    public ResponseEntity<Void> delete(@PathVariable String userId, @PathVariable String id){
        UUID addressId = UUID.fromString(id);
        this.service.delete(UUID.fromString(userId), addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/zipcode/{zipcode}")
    @PreAuthorize(MessageConstants.HAS_ROLE_ADMIN_USER)
    public ResponseEntity<AddressDto> getAddress(@PathVariable String userId, @PathVariable String zipcode){
        AddressViaCEPDto dto = this.viaCEPService.getZipCodeViaCEP(zipcode);
        AddressDto addressDto = this.mapper.fromViaDtoToDto(dto);
        return ResponseEntity.ok(addressDto);
    }

    @GetMapping
    @PreAuthorize(MessageConstants.HAS_ROLE_ADMIN_USER)
    public ResponseEntity<Page<AddressReportDto>> page(
            @PathVariable String userId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "publicArea") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ){
        Page<Address> pageAddress = this.service.findPage(UUID.fromString(userId), page, linesPerPage, orderBy, direction);

        if(pageAddress.getTotalElements() == 0){
            return ResponseEntity.noContent().build();
        }
        Page<AddressReportDto> pageReport = pageAddress
                .map(this.mapper::toDto);
        return ResponseEntity.ok(pageReport);
    }

}
