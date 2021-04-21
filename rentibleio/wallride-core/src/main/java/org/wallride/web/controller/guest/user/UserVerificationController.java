package org.wallride.web.controller.guest.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.wallride.service.UserService;

import javax.inject.Inject;

@Controller
public class UserVerificationController {

	@Inject
	UserService userService;

	@RequestMapping(value = "/verification", method = RequestMethod.GET)
	public String init(@RequestParam("token") String token) {
		userService.verifyUser(token);
		return "user/verification";
	}

}
