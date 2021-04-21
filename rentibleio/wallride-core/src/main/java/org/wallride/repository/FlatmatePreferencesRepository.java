package org.wallride.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.domain.FlatmatePreferences;

import java.io.Serializable;

@Transactional
@Repository
public interface FlatmatePreferencesRepository extends JpaRepository<FlatmatePreferences, Serializable> {

}
