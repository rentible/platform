package org.wallride.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "social_media", schema = "public")
@DynamicInsert
@DynamicUpdate
@Indexed
@Getter
@Setter
public class SocialMedia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private Integer socialMediaType;

	@Column(unique = true, nullable = false)
	private String socialMediaUserId;

	@Column
	protected Boolean enabled;

	@Column(nullable = false)
	private LocalDateTime createdOn = LocalDateTime.now();

	@Column
	private Long createdBy;

	@Column(nullable = false)
	private LocalDateTime modifiedOn = LocalDateTime.now();

	@Column
	private Long modifiedBy;

}
