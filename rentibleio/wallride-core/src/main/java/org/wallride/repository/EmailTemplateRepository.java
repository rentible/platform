package org.wallride.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.domain.EmailTemplate;

import java.io.Serializable;

@Repository
@Transactional
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Serializable> {

    EmailTemplate findByTemplCode(int templCode);
}
