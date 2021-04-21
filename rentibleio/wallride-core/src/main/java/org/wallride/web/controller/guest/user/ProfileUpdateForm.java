/*
 * Copyright 2014 Tagbangers, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wallride.web.controller.guest.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.wallride.domain.UserDetail;
import org.wallride.model.ProfileUpdateRequest;
import org.wallride.service.UserService;
import org.wallride.support.AuthorizedUser;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProfileUpdateForm implements Serializable {

	@Autowired
	private UserService userService;

	private Long userId;

	@Email
	private String email;

	private String username;

	private boolean googleAuth;

	private String googleAuthQrUrl;

	private String secret;
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@NotNull
	private String phoneNumber;
	@NotNull
	private Integer gender;

    private List<Integer> hobbies;

	private List<Integer> languages;

	private Integer occupation;
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date dateOfBirth;

	public ProfileUpdateRequest toProfileUpdateRequest() {
		ProfileUpdateRequest request = new ProfileUpdateRequest();
		request.setUserId(getUserId());
		request.setEmail(getEmail());
		request.setUsername(getUsername());
		request.setGoogleAuth(isGoogleAuth());
		request.setGoogleAuthQrUrl(getGoogleAuthQrUrl());
		request.setSecret(getSecret());
		request.setFirstName(getFirstName());
		request.setLastName(getLastName());
		request.setDateOfBirth(getDateOfBirth());
		request.setPhoneNumber(getPhoneNumber());
		request.setGender(getGender());
		request.setHobbies(getHobbies());
		request.setLanguages(getLanguages());
		request.setOccupation(getOccupation());
		return request;
	}

	public ProfileUpdateForm build(AuthorizedUser authorizedUser, UserDetail userDetail) {

		ProfileUpdateForm form = new ProfileUpdateForm();
		form.setUserId(authorizedUser.getId());
		form.setEmail(authorizedUser.getEmail());
		form.setUsername(authorizedUser.getUsername());
		form.setFirstName(userDetail.getFirstName());
		if (authorizedUser.getGoogleAuth() != null) {
			form.setGoogleAuth(authorizedUser.getGoogleAuth());
		} else {
			form.setGoogleAuth(false);
		}
		form.setLastName(userDetail.getLastName());
		if (userDetail.getDateOfBirth() != null) {
			form.setDateOfBirth(java.sql.Timestamp.valueOf(userDetail.getDateOfBirth()));
		}
		if (userDetail.getPhoneNumber() != null) {
			form.setPhoneNumber(userDetail.getPhoneNumber());
		}
		if (userDetail.getGender() != null) {
			form.setGender(userDetail.getGender().getId());
		}
		if (userDetail.getOccupation() != null) {
			form.setOccupation(userDetail.getOccupation().getId());
		}
		if (userDetail.getLanguages() != null) {
			try {
				userDetail.deserializeLanguagesAttributes();
			} catch (IOException e) {
				e.printStackTrace();
			}
			form.setLanguages((List<Integer>) userDetail.getLanguagesMap().get("languages"));
		}
		if (userDetail.getHobbies() != null) {
			try {
				userDetail.deserializeHobbiesAttributes();
			} catch (IOException e) {
				e.printStackTrace();
			}
			form.setHobbies((List<Integer>) userDetail.getHobbiesMap().get("hobbies"));
		}

		return form;
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
