package io.github.alberes.register.manager.services;

import io.github.alberes.register.manager.domains.Address;
import io.github.alberes.register.manager.domains.UserAccount;
import io.github.alberes.register.manager.repositories.AddressRepository;
import io.github.alberes.register.manager.services.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository repository;

    public Address save(Address address){
        address = this.repository.save(address);
        return address;
    }

    public Address find(UUID id){
        Optional<Address> optional = this.repository.findById(id);
        return optional.orElseThrow(()-> new ObjectNotFoundException(
                "Object not found! Id: " + id.toString() + ", Type: " + Address.class.getName()
        ));
    }

    public void update(Address address){
        this.find(address.getId());
        this.repository.save(address);
    }

    public void delete(UUID id){
        this.find(id);
        this.delete(id);
    }
}
