package io.github.alberes.register.manager.services;

import io.github.alberes.register.manager.domains.Address;
import io.github.alberes.register.manager.domains.UserAccount;
import io.github.alberes.register.manager.domains.UserPrincipal;
import io.github.alberes.register.manager.repositories.AddressRepository;
import io.github.alberes.register.manager.services.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService implements GenericService{

    private final AddressRepository repository;

    @Transactional
    @Modifying
    public Address save(Address address){
        address = this.repository.save(address);
        return address;
    }

    @Transactional
    public Address find(UUID id){
        Optional<Address> optional = this.repository.findById(id);
        return optional.orElseThrow(()-> new ObjectNotFoundException(
                "Object not found! Id: " + id.toString() + ", Type: " + Address.class.getName()
        ));
    }

    @Transactional
    @Modifying
    public void update(Address address){
        Address addressDB = this.find(address.getId());
        address.setUserAccount(addressDB.getUserAccount());
        address.setCreatedDate(addressDB.getCreatedDate());
        this.repository.save(address);
    }

    @Transactional
    @Modifying
    public void delete(UUID id){
        this.find(id);
        this.repository.deleteById(id);
    }

    @Transactional
    public Page<Address> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
        if(this.hasRoleAdmin(userPrincipal.getAuthorities())) {
            return this.repository.findAll(
                    PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy));
        } else{
            return this.repository.findByUserAccountId(userPrincipal.getId(), PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy));
        }
    }

}
