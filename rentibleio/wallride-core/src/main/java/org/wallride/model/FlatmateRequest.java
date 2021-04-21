package org.wallride.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FlatmateRequest implements Serializable {

	private String title;
	private List<Integer> languages;
	private String area;
	private Integer budget;
	private Integer currency;
	private String description;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date moveInFrom;
	private Integer termOfLease;
	private Boolean isSmokingAllowed;
	private List<Integer> districts;

}
