package org.wallride.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.domain.Email;

import java.io.Serializable;
import java.util.List;

@Repository
@Transactional
public interface EmailRepository extends JpaRepository<Email, Serializable> {

    @Query("SELECT e FROM Email e WHERE e.success <> true AND e.attempt < 5")
    List<Email> findAllToScheduler();
}
