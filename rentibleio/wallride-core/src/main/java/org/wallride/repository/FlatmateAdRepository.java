package org.wallride.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wallride.domain.FlatmateAd;

import java.io.Serializable;

@Repository
public interface FlatmateAdRepository extends JpaRepository<FlatmateAd, Serializable> {

	FlatmateAd findOneById(Long id);

}
