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

package org.wallride.web.controller.admin.signup;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.wallride.model.SignupRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@SuppressWarnings("serial")
@Getter
@Setter
public class SignupForm implements Serializable {

	@NotNull
	private String token;
	@NotNull
	@Pattern(regexp = "^[\\w\\-]+$")
	private String username;
	@NotNull
	private String password;
	@NotNull
	@Email
	private String email;

	public SignupRequest toSignupRequest() {
		SignupRequest request = new SignupRequest();
		request.setEmail(getEmail());
		request.setUsername(getUsername());
		request.setPassword(getPassword());
		return request;
	}
}
