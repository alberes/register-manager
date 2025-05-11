package io.github.alberes.register.manager.controlles.mappers;

import io.github.alberes.register.manager.controlles.dto.AddressDto;
import io.github.alberes.register.manager.domains.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    public Address toEntity(AddressDto dto);

    public AddressDto toDto(Address address);
}
