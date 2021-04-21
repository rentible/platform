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

package org.wallride.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.autoconfigure.WallRideCacheConfiguration;
import org.wallride.domain.User;
import org.wallride.domain.UserDetail;
import org.wallride.domain.UserInvitation;
import org.wallride.exception.DuplicateEmailException;
import org.wallride.exception.ServiceException;
import org.wallride.model.SignupRequest;
import org.wallride.repository.RoleRepository;
import org.wallride.repository.UserDetailRepository;
import org.wallride.repository.UserInvitationRepository;
import org.wallride.repository.UserRepository;
import org.wallride.support.AuthorizedUser;
import org.wallride.util.SecurityUtil;
import org.wallride.web.support.HttpForbiddenException;
import org.wallride.web.support.SysKeys;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class SignupService {

	private static Logger logger = LoggerFactory.getLogger(SignupService.class);

	@Resource
	private UserRepository userRepository;
	@Resource
	private UserInvitationRepository userInvitationRepository;
	@Resource
	private UserDetailRepository userDetailRepository;
	@Resource
	private EmailTemplateService emailTemplateService;
	@Resource
	private RoleRepository roleRepository;

    @Value("${wallride.email.verification.url}")
    private String verificationUrl;

	public UserInvitation readUserInvitation(String token) {
		return userInvitationRepository.findOneByToken(token);
	}

	public boolean validateInvitation(UserInvitation invitation) {
		if (invitation == null) {
			return false;
		}
		if (invitation.isAccepted()) {
			return false;
		}
		LocalDateTime now = LocalDateTime.now();
		if (now.isAfter(invitation.getExpiredAt())) {
			return false;
		}
		return true;
	}

	@CacheEvict(value = WallRideCacheConfiguration.USER_CACHE, allEntries = true)
	public AuthorizedUser signup(SignupRequest request) throws ServiceException {
		return signup(request, null);
	}

	@CacheEvict(value = WallRideCacheConfiguration.USER_CACHE, allEntries = true)
	public AuthorizedUser signup(SignupRequest request, String token) throws ServiceException {
		UserInvitation invitation = null;
		if (token != null) {
			invitation = userInvitationRepository.findOneForUpdateByToken(token);
			if (invitation == null) {
				throw new HttpForbiddenException();
			}
			if (!validateInvitation(invitation)) {
				throw new HttpForbiddenException();
			}
		}

		User duplicate;

		duplicate = userRepository.findOneByEmail(request.getEmail());
		if (duplicate != null) {
			throw new DuplicateEmailException(request.getEmail());
		}

		LocalDateTime now = LocalDateTime.now();
		if (invitation != null) {
			invitation.setAccepted(true);
			invitation.setAcceptedAt(now);
			userInvitationRepository.saveAndFlush(invitation);
		}

		String role = SysKeys.ROLE_UNACTIVATED_LODGER_NAME;

		User user = new User();
		user.setUsername(request.getEmail());
		user.setPassword(new BCryptPasswordEncoder(11).encode(request.getPassword()));
		user.setGoogleAuth(request.getGoogleAuth());
		user.setEmail(request.getEmail());
		user.setEnabled(true);
		user.setSchemaId(SysKeys.CSI_ID_SCHEMA_CMS_HUN);
		user.setSecret(request.getSecret());
		user.setGoogleAuthQrUrl(request.getGoogleAuthQrUrl());
		user.setActivationToken(UUID.randomUUID().toString());
		user.getRoles().add(roleRepository.getRoleByName(role));
		user.setCreatedOn(now);
		user.setModifiedOn(now);
		user = userRepository.saveAndFlush(user);
		userRepository.addRole(user.getId(), SysKeys.ROLE_UNACTIVATED_LODGER);

		UserDetail userDetail = new UserDetail();
		userDetail.setUser(user);
		userDetail.setFirstName(request.getFirstName());
		userDetail.setLastName(request.getLastName());
		userDetailRepository.saveAndFlush(userDetail);

		Map<String, Object> params = new HashMap<>();
		params.put("recipient", request.getFirstName() + " " + request.getLastName());
        params.put("appname", "Flatshares Team"); //FIXME application.properties
        params.put("url", "\"" + verificationUrl + user.getActivationToken() + "\"");
		emailTemplateService.addMail(user.getEmail(), SysKeys.EMAIL_TEMPLATE_USER_ACTIVATION, params);

		AuthorizedUser authorizedUser = new AuthorizedUser(user);
		SecurityUtil.setUser(authorizedUser);

		return authorizedUser;
	}

}
