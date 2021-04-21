package hu.lanoga.flatshares.config.google.authentication.twofactor;

import hu.lanoga.flatshares.exception.IPBlockedException;
import hu.lanoga.flatshares.model.User;
import hu.lanoga.flatshares.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    private final UserService userService;
    private final UserDetailsService userDetailsService;

    public CustomAuthenticationProvider(UserService userService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication auth) {

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(auth.getName());
            User user = userService.findByUsername(userDetails.getUsername());

            if ((user == null)) {
                throw new BadCredentialsException("Invalid username or password");
            } else {
                if (user.isGoogleAuth()) {
                    String verificationCode = ((CustomWebAuthenticationDetails) auth.getDetails()).getVerificationCode();
                    verify(user.getSecret(), Integer.valueOf(verificationCode));
                }

                Authentication result = super.authenticate(auth);
                return new UsernamePasswordAuthenticationToken(
                        user, result.getCredentials(), result.getAuthorities());
            }
        } catch (IPBlockedException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * @param secret
     * @param verificationCode
     */
    private void verify(String secret, Integer verificationCode) {
        try {
            TOTPAuthenticator totpAuthenticator = new TOTPAuthenticator();
            if (!totpAuthenticator.verifyCode(secret, verificationCode, 2)) {
                throw new BadCredentialsException("Invalid verification code");
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("An error has occurred during the verification: ", e);
        } catch (Exception e) {
            log.error("An error has occurred during the verification: ", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
