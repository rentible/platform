package org.wallride.web.controller.guest.flatmate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.wallride.domain.UserDetail;
import org.wallride.model.FlatmateRequest;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FlatmateForm implements Serializable {

	@NotNull
	private String title;
	private String area;
	@NotNull
	private Integer budget;
	@NotNull
	private Integer currency;
	@NotNull
	private String description;
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date moveInFrom;
	@NotNull
	private Integer termOfLease;
	private List<Integer> languages;
	private List<Integer> districts;
	private Boolean isSmokingAllowed;

	public FlatmateRequest toFlatmateRequest() {
		FlatmateRequest request = new FlatmateRequest();

		request.setTitle(getTitle());
		request.setLanguages(getLanguages());
		request.setArea(getArea());
		request.setBudget(getBudget());
		request.setDescription(getDescription());
		request.setCurrency(getCurrency());
		request.setMoveInFrom(getMoveInFrom());
		request.setTermOfLease(getTermOfLease());
		request.setIsSmokingAllowed(getIsSmokingAllowed());
		request.setDistricts(getDistricts());
		return request;
	}

	public Model setProfilePicture(UserDetail userDetail, Model model) {
		if (userDetail.getProfileImage() != null) {
			model.addAttribute("profilePicturePath", "../media/" + userDetail.getProfileImage().getFilePath());
		} else {
			model.addAttribute("profilePicturePath", "../media/default/default-profile.png");
		}
		return model;
	}

}
