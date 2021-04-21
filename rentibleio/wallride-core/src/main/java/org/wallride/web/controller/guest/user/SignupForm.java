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
import org.hibernate.validator.constraints.Email;
import org.wallride.model.SignupRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Getter
@Setter
@SuppressWarnings("serial")
public class SignupForm implements Serializable {

	@Pattern(regexp = "^[\\w\\-]+$")
	private String username;
	@NotNull
	private String password;

	private String passwordConfirmation;

	private String firstName;

	private String lastName;

	@NotNull
	@Email
	private String email;

	private boolean googleAuth = false;

	private String secret;

	private String googleAuthQrUrl;

	public SignupRequest toSignupRequest() {
		SignupRequest request = new SignupRequest();
		request.setEmail(getEmail());
		request.setUsername(getEmail());
		request.setPassword(getPassword());
		request.setFirstName(getFirstName());
		request.setLastName(getLastName());
		request.setGoogleAuth(isGoogleAuth());
		request.setGoogleAuthQrUrl(getGoogleAuthQrUrl());
		request.setSecret(getSecret());
		return request;
	}

}
