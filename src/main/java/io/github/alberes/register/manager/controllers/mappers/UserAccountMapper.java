package io.github.alberes.register.manager.controllers.mappers;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.alberes.register.manager.controllers.dto.UserAccountDto;
import io.github.alberes.register.manager.controllers.dto.UserAccountProfileDto;
import io.github.alberes.register.manager.controllers.dto.UserAccountReportDto;
import io.github.alberes.register.manager.domains.UserAccount;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    public UserAccount toEntity(UserAccountDto dto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    public UserAccountReportDto toDto(UserAccount userAccount);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "roles", target = "profiles", qualifiedByName = "mapRoles")
    public UserAccountProfileDto toProfileDto(UserAccount userAccount);

    @Named("mapRoles")
    default Set<String> mapRoles(Set<String> roles) {
        return roles;
    }

}
