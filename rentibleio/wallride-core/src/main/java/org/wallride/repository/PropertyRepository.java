package org.wallride.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.domain.Property;

import java.io.Serializable;

@Transactional
@Repository
public interface PropertyRepository extends JpaRepository<Property, Serializable> {

	@Modifying
	@Query(value = "INSERT INTO cms_hun.property_x_file_descriptor(property_id, file_descriptor_id) VALUES(:propertyId, :fileDescriptorId)", nativeQuery = true)
	int addImages(@Param("propertyId") long propertyId, @Param("fileDescriptorId") long fileDescriptorId);

    @Modifying
    @Query(value = "update property set additional_info = :additionalInfo where id = :id ", nativeQuery = true)
    int setAdditionalInfo(@Param("id") long id, @Param("additionalInfo") String additionalInfo);

	Property findOneById(Long id);

}
