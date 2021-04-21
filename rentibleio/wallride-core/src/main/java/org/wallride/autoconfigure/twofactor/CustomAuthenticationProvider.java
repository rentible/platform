package org.wallride.autoconfigure.twofactor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.wallride.domain.User;
import org.wallride.exception.IPBlockedException;
import org.wallride.service.UserService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    private static Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

	private final UserService userService;

	public CustomAuthenticationProvider(UserService userService) {
		this.userService = userService;
	}

	@Override
	public Authentication authenticate(Authentication auth) {

		try {
			User user = userService.getUserByLoginId(auth.getName());

			if ((user == null)) {
				throw new BadCredentialsException("Invalid username or password");
			} else {
                if (Boolean.TRUE.equals(user.getGoogleAuth())) {
					String verificationCode = ((CustomWebAuthenticationDetails) auth.getDetails()).getVerificationCode();
					if(!StringUtils.isBlank(verificationCode)) {
						if(verify(user.getSecret(), Integer.valueOf(verificationCode))){
							Authentication result = super.authenticate(auth);
							return new UsernamePasswordAuthenticationToken(
									user, result.getCredentials(), result.getAuthorities());
						} else {
							throw new BadCredentialsException("Invalid code");
						}
					} else {
						throw new BadCredentialsException("Code can not be blank");
					}
				} else {
					Authentication result = super.authenticate(auth);
					return new UsernamePasswordAuthenticationToken(
							user, result.getCredentials(), result.getAuthorities());
				}

			}
		} catch (IPBlockedException e) {
            logger.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * @param secret
	 * @param verificationCode
	 */
	private boolean verify(String secret, Integer verificationCode) {
		Boolean verifiied = false;
		try {
			TOTPAuthenticator totpAuthenticator = new TOTPAuthenticator();
			verifiied = totpAuthenticator.verifyCode(secret, verificationCode, 2);
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
            logger.error("An error has occurred during the verification: ", e);
		} catch (Exception e) {
            logger.error("An error has occurred during the verification: ", e);
		}
		return verifiied;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
