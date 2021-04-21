package hu.lanoga.flatshares.config.security;

import hu.lanoga.flatshares.exception.IPBlockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private final LoginAttemptService loginAttemptService;

	public CustomAuthenticationFailureHandler(LoginAttemptService loginAttemptService) {
		this.loginAttemptService = loginAttemptService;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
		if (exception instanceof IPBlockedException) {
			getRedirectStrategy().sendRedirect(request, response, "/pages/public/login.xhtml?blocked=true");
		} else {
			getRedirectStrategy().sendRedirect(request, response, "/pages/public/login.xhtml?error=true");
		}

        loginAttemptService.loginFailed(request.getRemoteAddr());
	}
}
