package org.wallride.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "room_ad", schema = "cms_hun")
public class RoomAd {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "property_id", referencedColumnName = "id")
	private Property property;

	@OneToOne
	@JoinColumn(name = "flatmate_preferences_id", referencedColumnName = "id")
	private FlatmatePreferences flatmatePreferences;

	@Column
	private Boolean enabled;

	@Column
	@NotNull
	private Timestamp createdOn;

	@Column
	@NotNull
	private Long createdBy;

	@Column
	@NotNull
	private Timestamp modifiedOn;

	@Column
	@NotNull
	private Long modifiedBy;

}
