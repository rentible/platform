package org.wallride.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.domain.Template;

@Repository
@Transactional
public interface TemplateRepository extends JpaRepository<Template, Long> {

    Template findOneByName(String name);
}
