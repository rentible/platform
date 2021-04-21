package org.wallride.web.controller.guest.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.wallride.domain.User;
import org.wallride.service.FacebookGraphApiService;

@Controller
public class OAuthController {

    @Autowired
    private FacebookGraphApiService facebookGraphApiService;

	@RequestMapping(value = "/oauth2/callback", method = RequestMethod.GET)
	public String init(@RequestParam("code") String code) {

		User user = facebookGraphApiService.loadUser(code);

		if (user == null) {
			return "user/login";
		}
		return "redirect:/property-list";
	}
}
