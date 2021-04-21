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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wallride.domain.BlogLanguage;
import org.wallride.domain.User;
import org.wallride.domain.UserDetail;
import org.wallride.exception.DuplicateEmailException;
import org.wallride.exception.DuplicateLoginIdException;
import org.wallride.model.ProfileUpdateRequest;
import org.wallride.service.UserService;
import org.wallride.support.AuthorizedUser;
import org.wallride.support.CommonCodeStoreItem;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;

@Controller
@RequestMapping("/settings/profile")
public class ProfileUpdateController {

	public static final String FORM_MODEL_KEY = "form";
	public static final String ERRORS_MODEL_KEY = BindingResult.MODEL_KEY_PREFIX + FORM_MODEL_KEY;
	public static final String TERM_OF_LEASE = "termOfLease";
	public static final String CURRENCIES = "currencies";
	public static final String LANGUAGES = "languages";
	public static final String GENDERS = "genders";
	public static final String OCCUPATIONS = "occupations";
	public static final String HOBBIES = "hobbies";
	public static final String PROFILE_MENU_ACTIVE = "profileMenuActive";

	@Inject
	private UserService userService;

	@ModelAttribute(FORM_MODEL_KEY)
	public ProfileUpdateForm setupProfileUpdateForm() {
		return new ProfileUpdateForm();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String init(
			Principal principal,
			Model model,
			BlogLanguage blogLanguage) {
		AuthorizedUser authorizedUser = new AuthorizedUser((User) ((Authentication) principal).getPrincipal());
		UserDetail userDetail = authorizedUser.getUserDetail();
		CommonCodeStoreItem.CommonCodeStoreItemBuilder builder = new CommonCodeStoreItem.CommonCodeStoreItemBuilder();
		ProfileUpdateForm form = new ProfileUpdateForm().build(authorizedUser, userDetail);
		model = form.setProfilePicture(userDetail, model);

		builder.withCsiGenders(blogLanguage)
				.withCsiLanguages(blogLanguage)
				.withCsiOccupations(blogLanguage)
				.withCsiHobbies(blogLanguage)
				.build();

		model.addAttribute(TERM_OF_LEASE, builder.getCsiTermOfLease());
		model.addAttribute(CURRENCIES, builder.getCsiCurrencies());
		model.addAttribute(GENDERS, builder.getCsiGenders());
		model.addAttribute(LANGUAGES, builder.getCsiLanguages());
		model.addAttribute(OCCUPATIONS, builder.getCsiOccupations());
		model.addAttribute(HOBBIES, builder.getCsiHobbies());
		model.addAttribute(PROFILE_MENU_ACTIVE, true);
		model.addAttribute(FORM_MODEL_KEY, form);
		return edit(model, blogLanguage, principal);
	}

	@RequestMapping(method = RequestMethod.GET, params = "step.edit")
	public String edit(Model model, BlogLanguage blogLanguage, Principal principal) {
		AuthorizedUser authorizedUser = new AuthorizedUser((User) ((Authentication) principal).getPrincipal());
		UserDetail userDetail = authorizedUser.getUserDetail();

		CommonCodeStoreItem.CommonCodeStoreItemBuilder builder = new CommonCodeStoreItem.CommonCodeStoreItemBuilder();

		builder.withCsiGenders(blogLanguage)
				.withCsiLanguages(blogLanguage)
				.withCsiOccupations(blogLanguage)
				.build();

		model = new ProfileUpdateForm().setProfilePicture(userDetail, model);
		model.addAttribute(GENDERS, builder.getCsiGenders());
		model.addAttribute(LANGUAGES, builder.getCsiLanguages());
		model.addAttribute(OCCUPATIONS, builder.getCsiOccupations());
		model.addAttribute(PROFILE_MENU_ACTIVE, true);
		return "user/profile-update";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(
			@Validated @ModelAttribute(FORM_MODEL_KEY) ProfileUpdateForm form,
			BindingResult errors,
			Principal principal,
			Model model,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(FORM_MODEL_KEY, form);
		redirectAttributes.addFlashAttribute(ERRORS_MODEL_KEY, errors);
		AuthorizedUser authorizedUser = new AuthorizedUser((User) ((Authentication) principal).getPrincipal());

		if (errors.hasErrors()) {
			return "redirect:/settings/profile?step.edit";
		}

		ProfileUpdateRequest request;

		User updatedUser;
		try {
			if (Boolean.TRUE.equals(form.isGoogleAuth()) && !Boolean.TRUE.equals(authorizedUser.getGoogleAuth())) {
				form.setSecret(Base32.random());
				form.setGoogleAuthQrUrl(userService.generateQRUrl(form.getEmail(), form.getSecret()));
			}
			request = form.toProfileUpdateRequest();
			updatedUser = userService.updateProfile(request, authorizedUser);
		} catch (DuplicateLoginIdException e) {
			errors.rejectValue("loginId", "NotDuplicate");
			return "redirect:/settings/profile?step.edit";
		} catch (DuplicateEmailException e) {
			errors.rejectValue("email", "NotDuplicate");
			return "redirect:/settings/profile?step.edit";
		}

		redirectAttributes.getFlashAttributes().clear();
		redirectAttributes.addFlashAttribute("updatedUser", updatedUser);
		if (form.isGoogleAuth() && !authorizedUser.getGoogleAuth()) {
			try {
				return "redirect:/googleauth?qrUrl=" + URLEncoder.encode(form.getGoogleAuthQrUrl(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return "";
			}
		}
		return "redirect:/settings/profile";
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public String delete(Principal principal, Model model) {
		userService.deleteUser((User) ((Authentication) principal).getPrincipal());


		return "redirect:/user/login";
	}

	@RequestMapping(value = "/image", method = RequestMethod.POST)
	public String uploadProfileImage(@RequestParam("file") MultipartFile multipartFile, Principal principal) {
		AuthorizedUser authorizedUser = new AuthorizedUser((User) ((Authentication) principal).getPrincipal());
		userService.saveProfileImage(multipartFile, authorizedUser);
		return "redirect:/settings/profile";
	}
}
