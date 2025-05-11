package io.github.alberes.register.manager.repositories;

import io.github.alberes.register.manager.domains.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
