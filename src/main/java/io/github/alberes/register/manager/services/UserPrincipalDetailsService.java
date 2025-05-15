package io.github.alberes.register.manager.services;

import io.github.alberes.register.manager.constants.MessageConstants;
import io.github.alberes.register.manager.domains.UserAccount;
import io.github.alberes.register.manager.domains.UserPrincipal;
import io.github.alberes.register.manager.repositories.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserPrincipalDetailsService implements UserDetailsService {

    private final UserAccountRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = this.repository.findByEmail(username);
        if(userAccount == null){
            throw new UsernameNotFoundException(MessageConstants.OBJECT_NOT_FOUND);
        }
        userAccount.getRoles().size();
        return new UserPrincipal(userAccount);
    }
}