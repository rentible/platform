package org.wallride.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wallride.domain.CodeStoreType;

import java.io.Serializable;

@Repository
public interface CodeStoreTypeRepository extends JpaRepository<CodeStoreType, Serializable> {
}
