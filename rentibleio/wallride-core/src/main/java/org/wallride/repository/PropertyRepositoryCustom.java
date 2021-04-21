package org.wallride.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.wallride.domain.Property;
import org.wallride.model.PropertySearchRequest;

@Repository
public interface PropertyRepositoryCustom {
	Page<Property> search(PropertySearchRequest propertySearchRequest, Pageable pageable);
}
