/*
 * Copyright 2014 Tagbangers, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wallride.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.wallride.autoconfigure.twofactor.CustomAuthenticationProvider;
import org.wallride.autoconfigure.twofactor.CustomWebAuthenticationDetailsSource;
import org.wallride.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import org.wallride.service.AuthorizedUserDetailsService;
import org.wallride.service.UserService;
import org.wallride.web.support.SysKeys;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WallRideSecurityConfiguration {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private UserService userService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// @formatter:off
		auth.authenticationProvider(authenticationProvider());
		// @formatter:on
	}

	@Configuration
	@Order(1)
	public static class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private AccessDecisionManager accessDecisionManager;

		@Autowired
		private PersistentTokenRepository persistentTokenRepository;

		@Override
		public void configure(WebSecurity web) throws Exception {
			// @formatter:off
			web
				.ignoring()
					.antMatchers("/_admin/resources/**")
					.antMatchers("/_admin/webjars/**")
					.antMatchers("/_admin/setup**")
					.antMatchers("/_admin/signup**");
			// @formatter:on
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// @formatter:off
			http.antMatcher("/_admin/**")
				.authorizeRequests()
					.accessDecisionManager(accessDecisionManager)
//		            .expressionHandler(securityExpressionHandler)
					.antMatchers("/_admin/**")
                    .hasAuthority(SysKeys.ROLE_ADMIN_NAME)
					.and()
				.formLogin()
					.loginPage("/_admin/login").permitAll()
					.loginProcessingUrl("/_admin/login")
					.defaultSuccessUrl("/_admin/")
					.failureUrl("/_admin/login?failed")
					.and()
				.logout()
					.logoutRequestMatcher(new AntPathRequestMatcher("/_admin/logout", "GET"))
					.logoutSuccessUrl("/_admin/login")
					.and()
				.rememberMe()
					.tokenRepository(persistentTokenRepository)
					.and()
				.headers()
					.frameOptions().disable()
					.cacheControl().disable()
					.httpStrictTransportSecurity().disable()
					.and()
				.csrf()
					.disable()
				.exceptionHandling()
					.accessDeniedPage("/_admin/login");
			// @formatter:on
		}
	}

	@Configuration
	@Order(2)
	public static class GuestSecurityConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private AccessDecisionManager accessDecisionManager;

		@Autowired
		private PersistentTokenRepository persistentTokenRepository;

		@Autowired
		private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

		/*
		By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
		the authorization request. But, since our service is stateless, we can't save it in
		the session. We'll save the request in a Base64 encoded cookie instead.
		*/
		@Bean
		public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
			return new HttpCookieOAuth2AuthorizationRequestRepository();
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			// @formatter:off
			web
				.ignoring()
					.antMatchers("/resources/**")
					.antMatchers("/webjars/**");
			// @formatter:on
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
			SimpleUrlAuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler("/user/login?failed");
			SimpleUrlLogoutSuccessHandler logoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();

			// @formatter:off
			http
					.cors()
					.and()
				.authorizeRequests()
					.accessDecisionManager(accessDecisionManager)
					.antMatchers("/", "/login", "/user/login", "/signup", "/oauth2/callback").permitAll()
					.anyRequest().authenticated()
					.and()
				.formLogin()
					.authenticationDetailsSource(authenticationDetailsSource)
					.loginPage("/user/login").permitAll()
					.loginProcessingUrl("/login")
					.successHandler(successHandler)
					.failureHandler(failureHandler)
					.and()
				.logout()
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
					.logoutSuccessHandler(logoutSuccessHandler)
					.and()
				.rememberMe()
					.tokenRepository(persistentTokenRepository)
					.and()
				.headers()
					.frameOptions().disable()
					.cacheControl().disable()
					.httpStrictTransportSecurity().disable()
					.and()
				.csrf()
					.disable()
				.exceptionHandling()
					.accessDeniedPage("/user/login")
					.and()
					.oauth2Login()
					.authorizationEndpoint()
					.baseUri("/oauth2/authorize")
					.authorizationRequestRepository(cookieAuthorizationRequestRepository())
					.and()
					.redirectionEndpoint()
					.baseUri("/oauth2/callback/*")
					.and()
					.userInfoEndpoint()
					.and()
					.successHandler(successHandler)
					.failureHandler(failureHandler);
			// @formatter:on
		}

	}

	@Bean
	public UserDetailsService authorizedUserDetailsService() {
		return new AuthorizedUserDetailsService();
	}

	@Bean
	public AffirmativeBased accessDecisionManager() {
		List<AccessDecisionVoter<?>> accessDecisionVoters = new ArrayList<>();
		accessDecisionVoters.add(roleVoter());
		accessDecisionVoters.add(webExpressionVoter());

		AffirmativeBased accessDecisionManager = new AffirmativeBased(accessDecisionVoters);
		return accessDecisionManager;
	}

	@Bean
	public WebExpressionVoter webExpressionVoter() {
		WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
		webExpressionVoter.setExpressionHandler(webSecurityExpressionHandler());
		return webExpressionVoter;
	}

	@Bean
	public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
		DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
		defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
		return defaultWebSecurityExpressionHandler;
	}

	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
		hierarchy.setHierarchy("admin > lodger > unactivated_lodger");
		return hierarchy;
	}

	@Bean
	public RoleVoter roleVoter() {
		return new RoleHierarchyVoter(roleHierarchy());
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
		repository.setDataSource(dataSource);
		return repository;
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		final DaoAuthenticationProvider authProvider = new CustomAuthenticationProvider(userService);
		authProvider.setUserDetailsService(authorizedUserDetailsService());
		authProvider.setPasswordEncoder(new BCryptPasswordEncoder(11));
		return authProvider;
	}

}
