package hu.lanoga.flatshares.service;

import hu.lanoga.flatshares.model.Email;
import hu.lanoga.flatshares.model.EmailTemplate;
import hu.lanoga.flatshares.repository.EmailMapper;
import hu.lanoga.flatshares.repository.EmailTemplateMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.SqlTable;
import org.mybatis.dynamic.sql.render.RenderingStrategy;
import org.mybatis.dynamic.sql.where.render.WhereClauseProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.sql.JDBCType;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmailTemplateService {

	private EmailTemplateMapper emailTemplateMapper;
	private EmailMapper emailMapper;
	@Value("${flatshares.mail.sender.username}")
	private String fromEmail;

	public EmailTemplateService() {
	}

	@Autowired
	public EmailTemplateService(EmailTemplateMapper emailTemplateMapper, EmailMapper emailMapper) {
		this.emailTemplateMapper = emailTemplateMapper;
		this.emailMapper = emailMapper;
	}

    /**
     * Find an emailtemplate by a templateCode, modify the placeholders in the template and insert into the email table.
     *
     * @param from
     * @param templCode
     * @param params
     */
    @Transactional
    public void addMail(String from, Integer templCode, Map<String,Object> params){
        EmailTemplate emailTemplate = emailTemplateMapper.findByTemplCode(templCode);

        final VelocityContext velocityCtx = new VelocityContext();

        for (final Map.Entry<String, Object> entry : params.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            velocityCtx.put(key, value);
        }

        String subject="";
        String body="";

        try (StringWriter swOut = new StringWriter()) {

            Velocity.evaluate(velocityCtx, swOut, "", emailTemplate.getSubject());
            subject = swOut.toString();

        } catch (final Exception e) {
            log.error("{}",e);
        }
        try (StringWriter swOut = new StringWriter()) {

            Velocity.evaluate(velocityCtx, swOut, "", emailTemplate.getContent());
            body = swOut.toString();

        } catch (final Exception e) {
            log.error("{}",e);
        }

        final Email email = new Email();
        email.setFromEmail(fromEmail);
        email.setToEmail(from);
        email.setSubject(subject);
        email.setBody(body);
        email.setAttempt(0);
        email.setIsPlainText(false);
        email.setSuccess(false);

        emailMapper.save(email);
    }

    public List<EmailTemplate> findAllWithPaginationAndFilterBy(int offset, int limit, Map filters) {


        WhereClauseProvider whereClause = buildWhereClauseProvider(filters);

        return emailTemplateMapper.findAllWithPaginationAndFilterBy(offset, limit, whereClause);
    }

    public int count() {
        return emailTemplateMapper.count();
    }

    public int countByFilters(Map filters) {
        WhereClauseProvider whereClause = buildWhereClauseProvider(filters);

        return emailTemplateMapper.countBy(whereClause);
    }

    /**
     * Build where clause provider based on filters
     *
     * @param filters
     * @return
     */
    private WhereClauseProvider buildWhereClauseProvider(Map filters) {
        Integer id = null;
        Integer templCode = null;

        if (filters != null && filters.size() > 0) {

            if (filters.get("id") != null) {
                id = Integer.parseInt(String.valueOf(filters.get("id")));
            }

            if (filters.get("templ_code") != null) {
                templCode = Integer.parseInt(String.valueOf(filters.get("templ_code")));
            }
        }


        final SqlTable emailTemplateTable = SqlTable.of("public.email_template");

        return SqlBuilder
                .where(emailTemplateTable.column("id", JDBCType.INTEGER), SqlBuilder.isEqualToWhenPresent(id))
                .and(emailTemplateTable.column("templ_code", JDBCType.INTEGER), SqlBuilder.isEqualToWhenPresent(templCode))
                .build()
                .render(RenderingStrategy.MYBATIS3, "whereClauseProvider");
    }
}
