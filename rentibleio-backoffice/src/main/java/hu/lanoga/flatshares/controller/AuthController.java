package hu.lanoga.flatshares.controller;

import hu.lanoga.flatshares.util.PrimeFacesUtil;
import hu.lanoga.flatshares.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthController {

	@GetMapping(path = "/pages/public/login")
	public String login() {
		return "login";
	}

	public void logOut() {
		SecurityUtil.clearAuthentication();
		PrimeFacesUtil.redirect("/pages/public/login.xhtml");
	}
}
