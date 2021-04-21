package org.wallride.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.wallride.domain.FlatmateAd;
import org.wallride.model.FlatmateSearchRequest;

import java.util.List;

@Repository
public interface FlatmateAdRepositoryCustom {
	Page<FlatmateAd> search(FlatmateSearchRequest flatmateSearchRequest, Pageable pageable);

	List<FlatmateAd> findAllByCustomFilter(FlatmateSearchRequest flatmateSearchRequest);
}
