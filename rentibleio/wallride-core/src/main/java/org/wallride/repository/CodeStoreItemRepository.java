package org.wallride.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wallride.domain.CodeStoreItem;

import java.io.Serializable;
import java.util.List;

@Repository
public interface CodeStoreItemRepository extends JpaRepository<CodeStoreItem, Serializable> {

	List<CodeStoreItem> findAllByCodeStoreTypeId(Integer id);

	CodeStoreItem findOneById(Integer id);
}
