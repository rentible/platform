package org.wallride.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wallride.domain.Address;

import java.io.Serializable;

@Repository
public interface AddressRepository extends JpaRepository<Address, Serializable> {
}
