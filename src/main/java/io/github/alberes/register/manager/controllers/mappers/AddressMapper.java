package io.github.alberes.register.manager.controllers.mappers;

import io.github.alberes.register.manager.controllers.dto.AddressDto;
import io.github.alberes.register.manager.controllers.dto.AddressReportDto;
import io.github.alberes.register.manager.controllers.dto.AddressViaCEPDto;
import io.github.alberes.register.manager.domains.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    public Address toEntity(AddressDto dto);

    public AddressReportDto toDto(Address address);

    @Mapping(source = "cep", target = "zipCode")
    @Mapping(source = "logradouro", target = "publicArea")
    @Mapping(source = "complemento", target = "additionalAddress")
    @Mapping(source = "bairro", target = "neighborhood")
    @Mapping(source = "localidade", target = "city")
    @Mapping(source = "uf", target = "state")
    public AddressDto fromViaDtoToDto(AddressViaCEPDto dto);
}
