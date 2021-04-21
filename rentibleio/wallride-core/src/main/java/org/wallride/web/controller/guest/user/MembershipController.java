package org.wallride.web.controller.guest.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wallride.domain.BlogLanguage;

@Controller
public class MembershipController {

	private static final String PROFILE_MENU_ACTIVE = "profileMenuActive";

	@RequestMapping(value = "/settings/profile/membership")
	public String init(Model model, BlogLanguage blogLanguage) {
		model.addAttribute(PROFILE_MENU_ACTIVE, true);
		return "user/membership";
	}

}
