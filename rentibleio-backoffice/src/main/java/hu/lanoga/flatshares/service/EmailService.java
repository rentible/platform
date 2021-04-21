package hu.lanoga.flatshares.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class EmailService {

    @Value("${flatshares.mail.sender.username}")
    private String username;

    @Value("${flatshares.mail.sender.password}")
    private String password;

    @Value("${flatshares.mail.sender.host}")
    private String hostName;

    @Value("${flatshares.mail.sender.port}")
    private int smtpPort;

    @Value("${flatshares.mail.sender.ssl-on-connect}")
    private boolean sslOnConnect;

    /**
     * Email sending
     *
     * @param fromEmail
     * @param toEmail
     * @param subject
     * @param body
     * @param isPlainText
     */
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

}
