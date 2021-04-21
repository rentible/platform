package hu.lanoga.flatshares.util;

import hu.lanoga.flatshares.model.User;
import hu.lanoga.flatshares.model.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

public class SecurityUtil {

    /**
     * @param request
     * @return
     */
    public static String getClientIP(HttpServletRequest request) {

        String xfHeader = request.getHeader("X-Forwarded-For");

        if (xfHeader == null) {
            return request.getRemoteAddr();
        }

        return xfHeader.split(",")[0];
    }

	/**
	 * @return
	 */
    public static Authentication getAuthentication() {

		final SecurityContext securityContext = SecurityContextHolder.getContext();

		if (securityContext != null) {
			return securityContext.getAuthentication();
		}

		return null;
	}

	/**
	 * Delete authentication from Security Context
	 */
	public static void clearAuthentication() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

    public static User getLoggedInUser() {

        final Authentication auth = getAuthentication();

        if ((auth != null) && auth.isAuthenticated() && (auth.getPrincipal() != null)) {

            final Object principal = auth.getPrincipal();

            if (principal instanceof User) {

                return (User) principal;
            }

        }

        return null;
    }

    /**
     * Set the current user in the security context and delete the previous user authentication
     * @param userPrincipal
     */
    public static void setUser(UserPrincipal userPrincipal) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal.getUser(), null, userPrincipal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

	private SecurityUtil() {

	}
}
