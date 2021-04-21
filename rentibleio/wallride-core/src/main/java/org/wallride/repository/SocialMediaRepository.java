package org.wallride.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wallride.domain.SocialMedia;

import java.io.Serializable;

@Repository
public interface SocialMediaRepository extends JpaRepository<SocialMedia, Serializable> {

	SocialMedia findOneById(Long id);

}
