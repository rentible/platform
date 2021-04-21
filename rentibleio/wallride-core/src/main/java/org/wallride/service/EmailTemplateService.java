package org.wallride.service;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.domain.Email;
import org.wallride.domain.EmailTemplate;
import org.wallride.repository.EmailRepository;
import org.wallride.repository.EmailTemplateRepository;
import org.wallride.support.DateAndTimeUtil;
import org.wallride.web.support.SysKeys;

import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class EmailTemplateService {

    private static Logger logger = LoggerFactory.getLogger(EmailTemplateService.class);

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void addMail(String to, Integer templCode) {
        addMail(to, templCode, Collections.emptyMap());
    }

    public void addMail(String to, Integer templCode, Map<String, Object> params) {
        EmailTemplate emailTemplate = emailTemplateRepository.findByTemplCode(templCode);

        final VelocityContext velocityCtx = new VelocityContext();

        for (final Map.Entry<String, Object> entry : params.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            velocityCtx.put(key, value);
        }

        String subject = "";
        String body = "";

        try (StringWriter swOut = new StringWriter()) {

            Velocity.evaluate(velocityCtx, swOut, "", emailTemplate.getSubject());
            subject = swOut.toString();

        } catch (final Exception e) {
            logger.error("{}", e);
        }
        try (StringWriter swOut = new StringWriter()) {

            Velocity.evaluate(velocityCtx, swOut, "", emailTemplate.getContent());
            body = swOut.toString();

        } catch (final Exception e) {
            logger.error("{}", e);
        }

        final Email email = new Email();
        email.setFromEmail(fromEmail);
        email.setToEmail(to);
        email.setSubject(subject);
        email.setBody(body);
        email.setAttempt(0);
        email.setIsPlainText(false);
        email.setSuccess(false);
        email.setCreatedOn(DateAndTimeUtil.now());
        email.setModifiedOn(DateAndTimeUtil.now());
        email.setCreatedBy(SysKeys.SYS_ADMIN_USER_ID);
        email.setModifiedBy(SysKeys.SYS_ADMIN_USER_ID);

        emailRepository.save(email);
    }
}
