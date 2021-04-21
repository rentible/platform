package org.wallride.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.SortableField;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user", schema = "public")
@DynamicInsert
@DynamicUpdate
@Indexed
@Getter
@Setter
@SuppressWarnings("serial")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Field(name = "sortId", analyze = Analyze.NO, index = org.hibernate.search.annotations.Index.NO)
	@SortableField(forField = "sortId")
	private Long id;

	@Column(length = 100, unique = true)
	@Field(analyze = Analyze.NO)
	private String username;

	@Column(length = 200, nullable = false, unique = true)
	private String email;

	@Column(length = 500, nullable = false)
    protected String password;

	@Column
	private Integer schemaId;

	@Column
	private Boolean googleAuth;

	@Column
	private String googleAuthQrUrl;

	@Column
    private Long socialMediaId;//FIXME ne az id, hanem az entitás legyen bejoinolva

	@Column
	private boolean enabled;

	@Column
	private String activationToken;

	@Column
	private LocalDateTime lastLoggedIn;

	@Column
	private Integer numberOfLogin;

	@Column
	private Integer language;

	@Column
	private Boolean online;

	@Column
	private String secret;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL,
			fetch = FetchType.LAZY, optional = false)
	private UserDetail userDetail;

	@Column(nullable = false)
	private LocalDateTime createdOn = LocalDateTime.now();

	@Column
	private Long createdBy;

	@Column(nullable = false)
	private LocalDateTime modifiedOn = LocalDateTime.now();

	@Column
	private Long modifiedBy;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "user_x_role", schema = "public",
			joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
			inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") }
	)
	private Set<Role> roles = new HashSet<>();
}
