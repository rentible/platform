package org.wallride.web.controller.guest.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MessageController {

	@RequestMapping("send-message-to-flatmate")
	public String SendMessageToFlatmate() {
		return "redirect:/flatmate-view";
	}

}
