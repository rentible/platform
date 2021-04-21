package org.wallride.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.wallride.web.support.HashMapConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "flatmate_ad", schema = "cms_hun")
public class FlatmateAd {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@Column
	private String title;

	@Column
	private String description;

	@Column
	private Integer budget;

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private String districts;

	@Transient
	@Setter(AccessLevel.NONE)
	@Convert(converter = HashMapConverter.class)
	private Map<String, Object> districtsMap = new HashMap<>();

	@Column
	private Timestamp moveInFrom;

	@Column
	private Boolean smokingAllowed;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "currency", referencedColumnName = "id")
	private CodeStoreItem currency;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "term_of_lease", referencedColumnName = "id")
	private CodeStoreItem termOfLease;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "flatmate_ad_language_x_code_store_item", schema = "cms_hun",
			joinColumns = { @JoinColumn(name = "flatmate_ad_id", referencedColumnName = "id") },
			inverseJoinColumns = { @JoinColumn(name = "code_store_item_id", referencedColumnName = "id") }
	)
	private Set<CodeStoreItem> languages = new HashSet<>();

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

	private final ObjectMapper objectMapper = new ObjectMapper();

	public void setDistrictsMap(Map<String, Object> districtsMap) {
		this.districtsMap = districtsMap;
		try {
			this.serializeDistrictsAttributes();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public void serializeDistrictsAttributes() throws JsonProcessingException {
		this.districts = objectMapper.writeValueAsString(districtsMap);
	}

	public void deserializeDistrictsAttributes() throws IOException {
		this.districtsMap = objectMapper.readValue(districts, HashMap.class);
	}
}
