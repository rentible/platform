package hu.lanoga.flatshares.controller;

import hu.lanoga.flatshares.model.User;
import hu.lanoga.flatshares.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GoogleAuthController {

	private UserService userService;

	public GoogleAuthController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(path = "/pages/public/qrcode")
	public String open() {
		return "pages/public/qrcode";
	}

	public String getQrUrl(int id) {
		User user = userService.findOne(id);
		return user.getGoogleAuthQrUrl();
	}

}
