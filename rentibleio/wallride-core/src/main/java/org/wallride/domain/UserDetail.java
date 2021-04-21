package org.wallride.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.wallride.web.support.HashMapConverter;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "user_detail")
@Getter
@Setter
public class UserDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;

	@Column
	private String firstName;

	@Column
	private String lastName;

	@Column
	private LocalDateTime dateOfBirth;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "profile_image")
    private FileDescriptor profileImage;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gender", referencedColumnName = "id")
	private CodeStoreItem gender;

	@Column
	private String phoneNumber;

	@Column
	private Integer normalCredit;

	@Column
	private Integer fbCredit;

	@Column
	private LocalDateTime showLogoExpire;

	@Column
	private LocalDateTime showUrlExpire;

	@Column
	private String fbId;

	@Column
	private Integer mailSmart;

	@Column
	private Integer mailMessage;

	@Column
	private Integer mailInterest;

	@Column
	private Integer mailNews;

	@Column
	private LocalDateTime emailApprovedOn;

	@Column
	private String avatar;

	@Column
	private Boolean adminVerified;

	@Column
	private String banNote;

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private String hobbies;

	@Transient
	@Setter(AccessLevel.NONE)
	@Convert(converter = HashMapConverter.class)
	private Map<String, Object> hobbiesMap = new HashMap<>();

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private String languages;

	@Transient
	@Setter(AccessLevel.NONE)
	@Convert(converter = HashMapConverter.class)
	private Map<String, Object> languagesMap = new HashMap<>();

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "occupation_id", referencedColumnName = "id")
	private CodeStoreItem occupation;

//	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinTable(name = "user_detail_x_language", schema = "cms_hun",
//			joinColumns = { @JoinColumn(name = "user_detail_id", referencedColumnName = "id") },
//			inverseJoinColumns = { @JoinColumn(name = "code_store_item_id", referencedColumnName = "id") }
//	)
//	private Set<CodeStoreItem> languages = new HashSet<>();

	private final ObjectMapper objectMapper = new ObjectMapper();

	public void setHobbiesMap(Map<String, Object> hobbiesMap) {
		this.hobbiesMap = hobbiesMap;
		try {
			this.serializeHobbiesAttributes();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public void serializeHobbiesAttributes() throws JsonProcessingException {
		this.hobbies = objectMapper.writeValueAsString(hobbiesMap);
	}

	public void deserializeHobbiesAttributes() throws IOException {
		this.hobbiesMap = objectMapper.readValue(hobbies, HashMap.class);
	}

	public void setLanguagesMap(Map<String, Object> languagesMap) {
		this.languagesMap = languagesMap;
		try {
			this.serializeLanguagesAttributes();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public void serializeLanguagesAttributes() throws JsonProcessingException {
		this.languages = objectMapper.writeValueAsString(languagesMap);
	}

	public void deserializeLanguagesAttributes() throws IOException {
		this.languagesMap = objectMapper.readValue(languages, HashMap.class);
	}

}
