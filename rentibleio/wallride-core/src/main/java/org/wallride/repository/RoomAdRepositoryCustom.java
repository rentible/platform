package org.wallride.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.wallride.domain.Property;
import org.wallride.domain.RoomAd;
import org.wallride.model.PropertyRequest;
import org.wallride.model.PropertySearchRequest;

import java.util.List;

@Repository
public interface RoomAdRepositoryCustom {
	Page<Property> search(PropertyRequest propertyRequest, Pageable pageable);

	List<RoomAd> findAllByCustomFilter(PropertySearchRequest propertySearchRequest);
}
