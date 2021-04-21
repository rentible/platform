package hu.lanoga.flatshares.config.security;

import hu.lanoga.flatshares.SysKeys;
import hu.lanoga.flatshares.config.google.authentication.twofactor.CustomAuthenticationProvider;
import hu.lanoga.flatshares.config.google.authentication.twofactor.CustomWebAuthenticationDetailsSource;
import hu.lanoga.flatshares.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailServiceImpl userDetailService;

	@Autowired
	private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private UserService userService;

	@Override
    protected void configure(final AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new CustomAuthenticationProvider(userService, userDetailService);
		authProvider.setUserDetailsService(userDetailService);
		authProvider.setPasswordEncoder(encoder());
		return authProvider;
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// require all requests to be authenticated except for the resources
        // /actuator path is reachable only with "admin" authority
        http
                .authorizeRequests().antMatchers("/actuator/**").hasAuthority(SysKeys.ROLE_ADMIN_NAME).and()
                .authorizeRequests().antMatchers("/javax.faces.resource/**", "/pages/public/**", "/public/media/**", "/api/property/**")
				.permitAll().anyRequest().authenticated();

        //Egyelőre nincs keverve a http-https
        //Minden https-n megy. Szüksées egyáltalán a security lazítása?

//		//User https over login url
//		http.requiresChannel()
//				.antMatchers("/pages/public/**").requiresInsecure().anyRequest().requiresSecure();

//		http.requiresChannel().antMatchers("/ws/**").requiresInsecure().anyRequest().requiresSecure();

        //Mixing http and https - https://www.baeldung.com/spring-channel-security-https
//		http.requiresChannel()
//				.anyRequest().requiresInsecure();


        // By default Spring creates a new session-id after a successful login.
        // When a user loads the HTTPS login page the user’s session-id cookie will be marked as secure.
        // After logging in, the context will switch to HTTP and the cookie will be lost as HTTP is insecure.
        http.sessionManagement()
                .sessionFixation()
                .none();

		// login
		http.formLogin()
				.authenticationDetailsSource(authenticationDetailsSource)
				.loginPage("/pages/public/login.xhtml").permitAll()

				.successForwardUrl("/pages/dashboard.xhtml")
				.failureHandler(authenticationFailureHandler);
		// logout
        http.logout()
                .logoutSuccessUrl("/pages/public/login.xhtml")
                .deleteCookies("JSESSIONID");


        //403 HTTP STATUS
        http.exceptionHandling().accessDeniedPage("/pages/public/error/403.html");

        // allow to use resource links like pdf
        //X-Frame-Options to allow any request from same domain. https://docs.spring.io/spring-security/site/docs/4.0.x/reference/html/headers.html#headers-frame-options
        //HTTP Strict Transport Security (HSTS) will not be addded to the response.  https://docs.spring.io/spring-security/site/docs/4.0.x/reference/html/headers.html#headers-hsts
        http.headers()
                .frameOptions().sameOrigin()
                .httpStrictTransportSecurity().disable();//includeSubDomains(true).maxAgeInSeconds(31536000);

		// not needed as JSF 2.2 is implicitly protected against CSRF
		http.csrf().disable();
	}


}