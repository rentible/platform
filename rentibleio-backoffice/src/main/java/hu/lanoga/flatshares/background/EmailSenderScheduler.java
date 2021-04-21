package hu.lanoga.flatshares.background;

import hu.lanoga.flatshares.model.Email;
import hu.lanoga.flatshares.repository.EmailMapper;
import hu.lanoga.flatshares.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public final class EmailSenderScheduler {

	private EmailMapper emailMapper;
	private EmailService emailService;
	private ExecutorService executor;

	@Value("${flatshares.mail.sender.scheduler.sub-threads}")
	private int nThreads;

	@PostConstruct
	private void init() {
		executor = Executors.newFixedThreadPool(nThreads);
		log.info("EmailSenderScheduler initilaized.");
	}

	@Autowired
	public EmailSenderScheduler(EmailMapper emailMapper, EmailService emailService) {
		this.emailMapper = emailMapper;
		this.emailService = emailService;
	}

	private class EmailSenderTask implements Runnable {

		private final Email email;

		public EmailSenderTask(Email email) {
			this.email = email;
		}

		@Override public void run() {
			try {
                log.info("EmailSenderScheduler (subthread) sending... emailId: " + email.getId());

				Integer attempt = email.getAttempt();
				email.setAttempt(++attempt);
				emailService.sendMail(email.getFromEmail(), email.getToEmail(), email.getSubject(), email.getBody(), email.getIsPlainText());
				email.setSuccess(true);
				emailMapper.update(email);

                log.info("EmailSenderScheduler (subthread) sending success, emailId: " + email.getId());
			} catch (Exception e) {
                log.error("Email send error!", e);

				try {
					emailMapper.update(email);
				} catch (Exception e2) {
                    log.error("EmailScheduler failed to update attempts!", e2);
				}
			}
		}
	}

	@Scheduled(cron = "${flatshares.mail.sender.scheduler.cronExpression:*/20 * * * * *}")
	private void scheduledEmailSending() {

		final List<Email> emails = emailMapper.findAllToScheduler();

		for (final Email email : emails) {
			executor.execute(new EmailSenderTask(email));
		}

	}
}
