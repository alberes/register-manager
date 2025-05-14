package io.github.alberes.register.manager.controllers.mappers;

import io.github.alberes.register.manager.controllers.dto.UserAccountDto;
import io.github.alberes.register.manager.controllers.dto.UserAccountReportDto;
import io.github.alberes.register.manager.domains.UserAccount;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-14T10:44:41-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class UserAccountMapperImpl implements UserAccountMapper {

    @Override
    public UserAccount toEntity(UserAccountDto dto) {
        if ( dto == null ) {
            return null;
        }

        UserAccount userAccount = new UserAccount();

        userAccount.setName( dto.name() );
        userAccount.setEmail( dto.email() );
        userAccount.setPassword( dto.password() );

        return userAccount;
    }

    @Override
    public UserAccountReportDto toDto(UserAccount userAccount) {
        if ( userAccount == null ) {
            return null;
        }

        String id = null;
        String name = null;
        String email = null;
        LocalDateTime lastModifiedDate = null;
        LocalDateTime createdDate = null;

        if ( userAccount.getId() != null ) {
            id = userAccount.getId().toString();
        }
        name = userAccount.getName();
        email = userAccount.getEmail();
        lastModifiedDate = userAccount.getLastModifiedDate();
        createdDate = userAccount.getCreatedDate();

        UserAccountReportDto userAccountReportDto = new UserAccountReportDto( id, name, email, lastModifiedDate, createdDate );

        return userAccountReportDto;
    }
}
