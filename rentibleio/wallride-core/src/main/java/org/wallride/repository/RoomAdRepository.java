package org.wallride.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.domain.RoomAd;

import java.io.Serializable;

@Transactional
@Repository
public interface RoomAdRepository extends JpaRepository<RoomAd, Serializable> {

	@Modifying
	@Query("UPDATE RoomAd a SET a.enabled = true WHERE a.property.id = :propertyId")
	void publishAd(@Param("propertyId") long propertyId);

	@Modifying
	@Query(value = "UPDATE room_ad SET flatmate_preferences_id = :flatmatePreferencesId WHERE property_id = :propertyId", nativeQuery = true)
	void setFlatmatePreferences(@Param("flatmatePreferencesId") long flatmatePreferencesId, @Param("propertyId") long propertyId);

}
