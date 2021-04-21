package org.wallride.web.controller.guest.flatmate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.wallride.model.FlatmateSearchRequest;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FlatmateSearchForm implements Serializable {

	public String language;
	public String area;
	public Integer gender;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date moveInFrom;
	public Integer budget;
	public Integer currency;
	public List<Integer> termOfLease;
	public Integer minAge;
	public Integer maxAge;
	public String name;
	public Integer city;

	public FlatmateSearchRequest toFlatmateSearchRequest() {
		FlatmateSearchRequest request = new FlatmateSearchRequest();

		request.setGender(getGender());
		request.setArea(getArea());
		request.setMinAge(getMinAge());
		request.setMaxAge(getMaxAge());
		request.setName(getName());
		request.setCity(getCity());
		request.setTermOfLease(getTermOfLease());

		return request;
	}

}
