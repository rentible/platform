package org.wallride.web.controller.guest.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GoogleAuthController {

	private static final String QR_URL = "qrUrl";

	@RequestMapping(value = "/googleauth", method = RequestMethod.GET)
	public String init(Model model, @RequestParam("qrUrl") String qrUrl) {
		model.addAttribute(QR_URL, qrUrl);
		return "user/googleauth";
	}

}
