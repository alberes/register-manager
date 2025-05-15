package io.github.alberes.register.manager.services;

import io.github.alberes.register.manager.constants.MessageConstants;
import io.github.alberes.register.manager.domains.UserAccount;
import io.github.alberes.register.manager.domains.UserPrincipal;
import io.github.alberes.register.manager.repositories.UserAccountRepository;
import io.github.alberes.register.manager.services.exceptions.DuplicateRecordException;
import io.github.alberes.register.manager.services.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAccountService implements GenericService{

    private final UserAccountRepository repository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Transactional
    @Modifying
    public UserAccount save(UserAccount userAccount){
        UserAccount userAccountDB = this.repository.findByEmail(userAccount.getEmail());
        if(userAccountDB != null){
            throw new DuplicateRecordException(MessageConstants.REGISTRATION_WITH_E_MAIL + userAccount.getEmail() + MessageConstants.HAS_ALREADY_BEEN_REGISTERED);
        }
        String password = this.encoder.encode(userAccount.getPassword());
        userAccount.setPassword(password);
        return this.repository.save(userAccount);
    }

    @Transactional
    public UserAccount find(UUID id){
        Optional<UserAccount> optional = this.repository.findById(id);
        return optional.orElseThrow(() -> new ObjectNotFoundException(
                MessageConstants.OBJECT_NOT_FOUND_ID + id.toString() + MessageConstants.TYPE + UserAccount.class.getName()));
    }

    @Transactional
    @Modifying
    public void update(UserAccount userAccount){
        UserAccount userAccountDB = this.find(userAccount.getId());
        userAccountDB.setName(userAccount.getName());
        this.repository.save(userAccountDB);
    }

    public void delete(UUID id){
        this.find(id);
        this.repository.deleteById(id);
    }

    @Transactional
    public Page<UserAccount> findPage(UUID id, Integer page, Integer linesPerPage, String orderBy, String direction) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
        if(this.hasRoleAdmin(userPrincipal.getAuthorities())) {
            return this.repository.findAll(
                    PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy));
        }else{
            return this.repository.findById(id, PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy));
        }
    }

    @Transactional
    public boolean notExistsEmail(String email){
        UserAccount userAccount = this.repository.findByEmail(email);
        return userAccount == null;
    }

}
