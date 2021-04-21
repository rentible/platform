package org.wallride.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.wallride.domain.CodeStoreItem;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class FlatmateSearchRequest implements Serializable {

	public String language;
	public String area;
	public Integer gender;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date moveInFrom;
	public Integer budget;
	public Integer currency;
	private Set<CodeStoreItem> rentPeriods = new HashSet<>();
	public List<Integer> termOfLease;
	public Integer minAge;
	public Integer maxAge;
	public String name;
	public Integer city;
	public Long authUserId;
}
