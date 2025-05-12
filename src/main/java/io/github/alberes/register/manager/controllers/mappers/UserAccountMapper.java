package io.github.alberes.register.manager.controllers.mappers;

import io.github.alberes.register.manager.controllers.dto.UserAccountDto;
import io.github.alberes.register.manager.domains.UserAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {

    public UserAccount toEntity(UserAccountDto dto);

    public UserAccountDto toDto(UserAccount userAccount);

}
