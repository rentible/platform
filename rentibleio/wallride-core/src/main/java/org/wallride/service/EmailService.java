package org.wallride.service;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.domain.Email;
import org.wallride.repository.EmailRepository;

import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class EmailService {

	private static Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Value("${spring.mail.username}")
	private String username;

	@Value("${spring.mail.password}")
	private String password;

	@Value("${spring.mail.host}")
	private String hostName;

	@Value("${spring.mail.port}")
	private int smtpPort;

	@Value("${spring.mail.ssl-on-connect}")
	private boolean sslOnConnect;

	@Autowired
	private EmailRepository emailRepository;

	public void save(Email email) {
		emailRepository.save(email);
	}

	public List<Email> findAllToScheduler() {
		return emailRepository.findAllToScheduler();
	}


	/**
	 * Email sending
	 *
	 * @param fromEmail
	 * @param toEmail
	 * @param subject
	 * @param body
	 * @param isPlainText
	 */
	@Deprecated
	public void sendMail(final String fromEmail, final String toEmail, final String subject, final String body, final boolean isPlainText) throws EmailException {
		final MultiPartEmail email = new MultiPartEmail();

		email.setHostName(hostName);
		email.setSmtpPort(smtpPort);
		email.setSSLOnConnect(sslOnConnect);
		email.setAuthenticator(new DefaultAuthenticator(username, password));

		if (fromEmail != null) {
			email.setFrom(fromEmail);
		} else {
			email.setFrom(username);
		}

		email.addTo(toEmail);
		email.setSubject(subject);

		if (StringUtils.isNotBlank(body)) {
			if (isPlainText) {
				email.addPart(body, "text/plain; charset=UTF-8");
			} else {
				email.addPart(body, "text/html; charset=UTF-8");
			}
		}

		email.send();

	}

	@Deprecated
	public void sendScheduledEmails() {

		logger.info("Starting email sender job...");
		for (Email email : findAllToScheduler()) {
			try {
				logger.info("EmailSenderScheduler sending... emailId: " + email.getId());

				Integer attempt = email.getAttempt();
				email.setAttempt(++attempt);
				sendMail(email.getFromEmail(), email.getToEmail(), email.getSubject(), email.getBody(), email.getIsPlainText());

				email.setSuccess(true);
				emailRepository.save(email);

				logger.info("EmailSenderScheduler sending success, emailId: " + email.getId());
			} catch (Exception e) {
				logger.error("Email send error!", e);

				try {
					emailRepository.save(email);
				} catch (Exception e2) {
					logger.error("EmailScheduler failed to update attempts!", e2);
				}
			}
		}
	}
}
