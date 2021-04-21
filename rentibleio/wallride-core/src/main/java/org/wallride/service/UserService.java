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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.wallride.autoconfigure.WallRideCacheConfiguration;
import org.wallride.domain.*;
import org.wallride.exception.DuplicateEmailException;
import org.wallride.exception.EmailNotFoundException;
import org.wallride.exception.ServiceException;
import org.wallride.model.*;
import org.wallride.repository.*;
import org.wallride.support.AuthorizedUser;
import org.wallride.util.SecurityUtil;
import org.wallride.web.support.SysKeys;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.ValidationException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

	private static final String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Inject
    private BlogService blogService;

    @Inject
    private FileStoreService fileStoreService;

    @Inject
    private FileDescriptorService fileDescriptorService;

    @Inject
    private MessageCodesResolver messageCodesResolver;

    @Inject
    private PlatformTransactionManager transactionManager;

    @Inject
    private JavaMailSender mailSender;

    @Inject
    @Qualifier("emailTemplateEngine")
    private TemplateEngine templateEngine;

    @Inject
    private MessageSourceAccessor messageSourceAccessor;

    @Inject
    private MailProperties mailProperties;

    @Resource
    private UserRepository userRepository;

    @Resource
    private UserInvitationRepository userInvitationRepository;

    @Resource
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Resource
    private UserDetailRepository userDetailRepository;

    @Resource
    private EmailTemplateService emailTemplateService;

    @Resource
    private CodeStoreItemRepository codeStoreItemRepository;

    public PasswordResetToken createPasswordResetToken(PasswordResetTokenCreateRequest request) {
        User user = userRepository.findOneByEmail(request.getEmail());
        if (user == null) {
            throw new EmailNotFoundException();
        }

        LocalDateTime now = LocalDateTime.now();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(user);
        passwordResetToken.setEmail(user.getEmail());
        passwordResetToken.setExpiredAt(now.plusHours(24));
        passwordResetToken.setCreatedAt(now);
        passwordResetToken.setCreatedBy(user.toString());
        passwordResetToken.setUpdatedAt(now);
        passwordResetToken.setUpdatedBy(user.toString());
        passwordResetToken = passwordResetTokenRepository.saveAndFlush(passwordResetToken);

        try {
            Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
            String blogTitle = blog.getTitle(LocaleContextHolder.getLocale().getLanguage());

            ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath();
            if (blog.isMultiLanguage()) {
                builder.path("/{language}");
            }
            builder.path("/password-reset");
            builder.path("/{token}");

            Map<String, Object> urlVariables = new LinkedHashMap<>();
            urlVariables.put("language", request.getLanguage());
            urlVariables.put("token", passwordResetToken.getToken());
            String resetLink = builder.buildAndExpand(urlVariables).toString();

            Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("passwordResetToken", passwordResetToken);
            ctx.setVariable("resetLink", resetLink);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
            message.setSubject(MessageFormat.format(
                    messageSourceAccessor.getMessage("PasswordResetSubject", LocaleContextHolder.getLocale()),
                    blogTitle));
            message.setFrom(mailProperties.getProperties().get("mail.from"));
            message.setTo(passwordResetToken.getEmail());

            String htmlContent = templateEngine.process("password-reset", ctx);
            message.setText(htmlContent, true); // true = isHtml

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new ServiceException(e);
        }

        return passwordResetToken;
    }

    @CacheEvict(value = WallRideCacheConfiguration.USER_CACHE, allEntries = true)
    public User updateUser(UserUpdateRequest form, Errors errors, AuthorizedUser authorizedUser) throws ValidationException {
        User user = userRepository.findOneForUpdateById(form.getId());
        //		user.setName(form.getName());
        //		user.setNickname(form.getNickname());
        user.setEmail(form.getEmail());
        //		user.setDescription(form.getDescription());

        user = userRepository.saveAndFlush(user);
        return user;
    }

    @CacheEvict(value = WallRideCacheConfiguration.USER_CACHE, allEntries = true)
    public User updateProfile(ProfileUpdateRequest request, AuthorizedUser updatedBy) {
        User user = userRepository.findOneForUpdateById(updatedBy.getId());
        UserDetail userDetail = user.getUserDetail();
        if (user == null) {
            throw new IllegalArgumentException("The user does not exist");
        }

        User duplicate;
        if (!ObjectUtils.nullSafeEquals(request.getEmail(), user.getEmail())) {
            duplicate = userRepository.findOneByEmail(request.getEmail());
            if (duplicate != null) {
                throw new DuplicateEmailException(request.getEmail());
            }
        }

        user.setEmail(request.getEmail());
		user.setGoogleAuth(request.getGoogleAuth());
		user.setGoogleAuthQrUrl(request.getGoogleAuthQrUrl());
		user.setSecret(request.getSecret());
		user.setModifiedOn(LocalDateTime.now());
        user.setModifiedBy(updatedBy.getId());

        CodeStoreItem gender = codeStoreItemRepository.findOneById(request.getGender());
        CodeStoreItem occupation = codeStoreItemRepository.findOneById(request.getOccupation());

        if (request.getLanguages() != null) {
            userDetail.getLanguagesMap().put("languages", request.getLanguages());
            userDetail.setLanguagesMap(userDetail.getLanguagesMap());
        }

        if (request.getHobbies() != null) {
            userDetail.getHobbiesMap().put("hobbies", request.getHobbies());
            userDetail.setHobbiesMap(userDetail.getHobbiesMap());
        }

        userDetail.setFirstName(request.getFirstName());
        userDetail.setLastName(request.getLastName());
        userDetail.setPhoneNumber(request.getPhoneNumber());
        userDetail.setGender(gender);
        userDetail.setOccupation(occupation);
        userDetail.setDateOfBirth(new java.sql.Timestamp(request.getDateOfBirth().getTime()).toLocalDateTime());
        userDetailRepository.saveAndFlush(userDetail);

		SecurityUtil.clearAuthentication();
		SecurityUtil.setUser(new AuthorizedUser(user));

        return userRepository.saveAndFlush(user);
    }

    @CacheEvict(value = WallRideCacheConfiguration.USER_CACHE, allEntries = true)
    public User updatePassword(PasswordUpdateRequest request, PasswordResetToken passwordResetToken) {
        User user = userRepository.findOneForUpdateById(request.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("The user does not exist");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setModifiedOn(LocalDateTime.now());
        user.setModifiedBy(passwordResetToken.getUser().getId());
        user = userRepository.saveAndFlush(user);

        passwordResetTokenRepository.delete(passwordResetToken);

        try {
            Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
            String blogTitle = blog.getTitle(LocaleContextHolder.getLocale().getLanguage());

            ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath();
            if (blog.isMultiLanguage()) {
                builder.path("/{language}");
            }
            builder.path("/login");

            Map<String, Object> urlVariables = new LinkedHashMap<>();
            urlVariables.put("language", request.getLanguage());
            urlVariables.put("token", passwordResetToken.getToken());
            String loginLink = builder.buildAndExpand(urlVariables).toString();

            Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("passwordResetToken", passwordResetToken);
            ctx.setVariable("resetLink", loginLink);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
            message.setSubject(MessageFormat.format(
                    messageSourceAccessor.getMessage("PasswordChangedSubject", LocaleContextHolder.getLocale()),
                    blogTitle));
            message.setFrom(mailProperties.getProperties().get("mail.from"));
            message.setTo(passwordResetToken.getEmail());

            String htmlContent = templateEngine.process("password-changed", ctx);
            message.setText(htmlContent, true); // true = isHtml

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new ServiceException(e);
        }

        return user;
    }

    @CacheEvict(value = WallRideCacheConfiguration.USER_CACHE, allEntries = true)
    public User updatePassword(PasswordUpdateRequest request, AuthorizedUser updatedBy) {
        User user = userRepository.findOneForUpdateById(request.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("The user does not exist");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setModifiedOn(LocalDateTime.now());
        user.setModifiedBy(updatedBy.getId());
        return userRepository.saveAndFlush(user);
    }

    @CacheEvict(value = WallRideCacheConfiguration.USER_CACHE, allEntries = true)
    public User deleteUser(UserDeleteRequest form, BindingResult result) throws BindException {
        User user = userRepository.findOneForUpdateById(form.getId());
        userRepository.delete(user);
        return user;
    }

    /**
     * Soft delete a user
     *
     * @param user
     * @return
     */
    @CacheEvict(value = WallRideCacheConfiguration.USER_CACHE, allEntries = true)
    public void deleteUser(User user) {
        userRepository.disable(user.getId());

        Map<String, Object> params = new HashMap<>();
        params.put("recipient", user.getUserDetail().getFirstName());
        params.put("appname", "Flatshares Team");//FIXME application.properties

        emailTemplateService.addMail(user.getEmail(), SysKeys.EMAIL_TEMPLATE_USER_DELETION, params);
        SecurityUtil.clearAuthentication();
    }

    @CacheEvict(value = WallRideCacheConfiguration.USER_CACHE, allEntries = true)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<User> bulkDeleteUser(UserBulkDeleteRequest bulkDeleteForm, BindingResult result) {
        List<User> users = new ArrayList<>();
        for (long id : bulkDeleteForm.getIds()) {
            final UserDeleteRequest deleteRequest = new UserDeleteRequest.Builder()
                    .id(id)
                    .build();

            final BeanPropertyBindingResult r = new BeanPropertyBindingResult(deleteRequest, "request");
            r.setMessageCodesResolver(messageCodesResolver);

            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            transactionTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
            User user = null;
            try {
                user = transactionTemplate.execute(status -> {
                    try {
                        return deleteUser(deleteRequest, r);
                    } catch (BindException e) {
                        throw new RuntimeException(e);
                    }
                });
                users.add(user);
            } catch (Exception e) {
                logger.debug("Errors: {}", r);
                result.addAllErrors(r);
            }
        }
        return users;
    }

    @CacheEvict(value = WallRideCacheConfiguration.USER_CACHE, allEntries = true)
    public List<UserInvitation> inviteUsers(UserInvitationCreateRequest form, BindingResult result, AuthorizedUser authorizedUser) throws MessagingException {
        String[] recipients = StringUtils.commaDelimitedListToStringArray(form.getInvitees());

        LocalDateTime now = LocalDateTime.now();

        List<UserInvitation> invitations = new ArrayList<>();
        for (String recipient : recipients) {
            UserInvitation invitation = new UserInvitation();
            invitation.setEmail(recipient);
            invitation.setMessage(form.getMessage());
            invitation.setExpiredAt(now.plusHours(72));
            invitation.setCreatedAt(now);
            invitation.setCreatedBy(authorizedUser.toString());
            invitation.setUpdatedAt(now);
            invitation.setUpdatedBy(authorizedUser.toString());
            invitation = userInvitationRepository.saveAndFlush(invitation);
            invitations.add(invitation);
        }

        Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
        for (UserInvitation invitation : invitations) {
            String websiteTitle = blog.getTitle(LocaleContextHolder.getLocale().getLanguage());
            String signupLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/_admin/signup")
                    .queryParam("token", invitation.getToken())
                    .buildAndExpand().toString();

            final Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("websiteTitle", websiteTitle);
            ctx.setVariable("authorizedUser", authorizedUser);
            ctx.setVariable("signupLink", signupLink);
            ctx.setVariable("invitation", invitation);

            final MimeMessage mimeMessage = mailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
            message.setSubject(MessageFormat.format(
                    messageSourceAccessor.getMessage("InvitationMessageTitle", LocaleContextHolder.getLocale()),
                    authorizedUser.toString(),
                    websiteTitle));
            message.setFrom(authorizedUser.getEmail());
            message.setTo(invitation.getEmail());

            final String htmlContent = templateEngine.process("user-invite", ctx);
            message.setText(htmlContent, true); // true = isHtml

            mailSender.send(mimeMessage);
        }

        return invitations;
    }

    @CacheEvict(value = WallRideCacheConfiguration.USER_CACHE, allEntries = true)
    public UserInvitation inviteAgain(UserInvitationResendRequest form, BindingResult result, AuthorizedUser authorizedUser) throws MessagingException {
        LocalDateTime now = LocalDateTime.now();

        UserInvitation invitation = userInvitationRepository.findOneForUpdateByToken(form.getToken());
        invitation.setExpiredAt(now.plusHours(72));
        invitation.setUpdatedAt(now);
        invitation.setUpdatedBy(authorizedUser.toString());
        invitation = userInvitationRepository.saveAndFlush(invitation);

        Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
        String websiteTitle = blog.getTitle(LocaleContextHolder.getLocale().getLanguage());
        String signupLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/_admin/signup")
                .queryParam("token", invitation.getToken())
                .buildAndExpand().toString();

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("websiteTitle", websiteTitle);
        ctx.setVariable("authorizedUser", authorizedUser);
        ctx.setVariable("signupLink", signupLink);
        ctx.setVariable("invitation", invitation);

        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setSubject(MessageFormat.format(
                messageSourceAccessor.getMessage("InvitationMessageTitle", LocaleContextHolder.getLocale()),
                authorizedUser.toString(),
                websiteTitle));
        message.setFrom(authorizedUser.getEmail());
        message.setTo(invitation.getEmail());

        final String htmlContent = templateEngine.process("user-invite", ctx);
        message.setText(htmlContent, true); // true = isHtml

        mailSender.send(mimeMessage);

        return invitation;
    }

    @CacheEvict(value = WallRideCacheConfiguration.USER_CACHE, allEntries = true)
    public UserInvitation deleteUserInvitation(UserInvitationDeleteRequest request) {
        UserInvitation invitation = userInvitationRepository.findOneForUpdateByToken(request.getToken());
        userInvitationRepository.delete(invitation);
        return invitation;
    }

    public List<Long> getUserIds(UserSearchRequest request) {
        return userRepository.searchForId(request);
    }

    public Page<User> getUsers(UserSearchRequest request) {
        Pageable pageable = new PageRequest(0, 10);
        return getUsers(request, pageable);
    }

    public Page<User> getUsers(UserSearchRequest request, Pageable pageable) {
        return userRepository.search(request, pageable);
    }

    private List<User> getUsers(Collection<Long> ids) {
        Set<User> results = new LinkedHashSet<User>(userRepository.findAllByIdIn(ids));
        List<User> users = new ArrayList<>();
        for (long id : ids) {
            for (User user : results) {
                if (id == user.getId()) {
                    users.add(user);
                    break;
                }
            }
        }
        return users;
    }

    //	@Cacheable(value="users", key="'id.'+#id")
    public User getUserById(long id) {
        return userRepository.findOneById(id);
    }

    public User getUserByLoginId(String loginId) {
        return userRepository.findOneByUsername(loginId);
    }

    //	@Cacheable(value="users", key="'invitations.list'")
    public List<UserInvitation> getUserInvitations() {
        return userInvitationRepository.findAll(new Sort(Sort.Direction.DESC, "createdAt"));
    }

    public PasswordResetToken getPasswordResetToken(String token) {
        return passwordResetTokenRepository.findOneByToken(token);
    }

    public void verifyUser(String token) {
        User user = userRepository.getUserByActivationToken(token);
        if (user != null) {
            userRepository.removeActivationToken(user.getEmail());
            userRepository.updateRole(user.getId(), SysKeys.ROLE_LODGER);
        }
    }

	public String generateQRUrl(String email, String secret) {
		try {

			return QR_PREFIX + URLEncoder.encode(String.format(
					"otpauth://totp/%s:%s?secret=%s&issuer=%s",
					"FlatShares", email, secret, "FlatShares"),
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("An error has occurred during QR URL generation: ", e);
		}

		return "";
	}

    /**
     * Save the uploaded image as profile image into a user record and delete the existing one in db and on disk too
     *
     * @param multipartFile Uploaded profile image
     * @param user
     */
    public void saveProfileImage(MultipartFile multipartFile, User user) {
        UserDetail userDetail = userRepository.findOneById(user.getId()).getUserDetail();

        if (userDetail.getProfileImage() != null) {
            fileStoreService.deleteOnDisk(userDetail.getProfileImage());
            fileDescriptorService.delete(userDetail.getProfileImage());
        }

        FileDescriptor profileImage = fileStoreService.store(multipartFile, user);
        userDetail.setProfileImage(profileImage);

        userDetail = userDetailRepository.save(userDetail);
        user.setUserDetail(userDetail);

        SecurityUtil.clearAuthentication();
        SecurityUtil.setUser(new AuthorizedUser(user));
    }

}
