package io.github.alberes.register.manager.controllers.mappers;

import io.github.alberes.register.manager.controllers.dto.AddressDto;
import io.github.alberes.register.manager.controllers.dto.AddressReportDto;
import io.github.alberes.register.manager.controllers.dto.AddressViaCEPDto;
import io.github.alberes.register.manager.domains.Address;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-14T10:44:41-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public Address toEntity(AddressDto dto) {
        if ( dto == null ) {
            return null;
        }

        Address address = new Address();

        address.setPublicArea( dto.publicArea() );
        address.setNumber( dto.number() );
        address.setAdditionalAddress( dto.additionalAddress() );
        address.setNeighborhood( dto.neighborhood() );
        address.setCity( dto.city() );
        address.setState( dto.state() );
        address.setZipCode( dto.zipCode() );

        return address;
    }

    @Override
    public AddressReportDto toDto(Address address) {
        if ( address == null ) {
            return null;
        }

        String id = null;
        String publicArea = null;
        Integer number = null;
        String additionalAddress = null;
        String neighborhood = null;
        String city = null;
        String state = null;
        String zipCode = null;
        LocalDateTime createdDate = null;

        if ( address.getId() != null ) {
            id = address.getId().toString();
        }
        publicArea = address.getPublicArea();
        number = address.getNumber();
        additionalAddress = address.getAdditionalAddress();
        neighborhood = address.getNeighborhood();
        city = address.getCity();
        state = address.getState();
        zipCode = address.getZipCode();
        createdDate = address.getCreatedDate();

        AddressReportDto addressReportDto = new AddressReportDto( id, publicArea, number, additionalAddress, neighborhood, city, state, zipCode, createdDate );

        return addressReportDto;
    }

    @Override
    public AddressDto fromViaDtoToDto(AddressViaCEPDto dto) {
        if ( dto == null ) {
            return null;
        }

        String zipCode = null;
        String publicArea = null;
        String additionalAddress = null;
        String neighborhood = null;
        String city = null;
        String state = null;

        zipCode = dto.cep();
        publicArea = dto.logradouro();
        additionalAddress = dto.complemento();
        neighborhood = dto.bairro();
        city = dto.localidade();
        state = dto.uf();

        Integer number = null;

        AddressDto addressDto = new AddressDto( publicArea, number, additionalAddress, neighborhood, city, state, zipCode );

        return addressDto;
    }
}
