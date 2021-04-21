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

import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wallride.exception.DuplicateEmailException;
import org.wallride.service.SignupService;
import org.wallride.service.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/signup")
public class SignupController {

	public static final String FORM_MODEL_KEY = "form";
	public static final String ERRORS_MODEL_KEY = BindingResult.MODEL_KEY_PREFIX + FORM_MODEL_KEY;
	public static final String REDIRECT_TO_EDIT = "redirect:/signup?step.edit";

	@Inject
	private SignupService signupService;
	@Inject
	private UserService userService;

	@ModelAttribute(FORM_MODEL_KEY)
	public SignupForm setupSignupForm() {
		return new SignupForm();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String init(Model model) {
		SignupForm form = new SignupForm();
		model.addAttribute(FORM_MODEL_KEY, form);
		return edit(model);
	}

	@RequestMapping(method = RequestMethod.GET, params = "step.edit")
	public String edit(Model model) {
		return "user/signup";
	}

	@RequestMapping(method=RequestMethod.POST)
	public String signup(
			@Valid @ModelAttribute("form") SignupForm form,
			BindingResult errors,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(FORM_MODEL_KEY, form);
		redirectAttributes.addFlashAttribute(ERRORS_MODEL_KEY, errors);

		if (errors.hasErrors()) {
			return REDIRECT_TO_EDIT;
		} else if (!form.getPassword().equals(form.getPasswordConfirmation())) {
			errors.rejectValue("passwordConfirmation", "MatchCurrentPassword");
			return REDIRECT_TO_EDIT;
		}

		try {
			if (form.isGoogleAuth()) {
				form.setSecret(Base32.random());
				form.setGoogleAuthQrUrl(userService.generateQRUrl(form.getEmail(), form.getSecret()));
			}
			signupService.signup(form.toSignupRequest());
		} catch (DuplicateEmailException e) {
			errors.rejectValue("email", "EmailNotDuplicate");
			return REDIRECT_TO_EDIT;
		}

		redirectAttributes.getFlashAttributes().clear();
		if (form.isGoogleAuth()) {
			try {
				return "redirect:/googleauth?qrUrl=" + URLEncoder.encode(form.getGoogleAuthQrUrl(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return "";
			}
		}
		return "redirect:/property-list";
	}
}
