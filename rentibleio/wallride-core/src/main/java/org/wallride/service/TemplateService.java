package org.wallride.service;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.domain.Template;
import org.wallride.repository.TemplateRepository;

import javax.annotation.Resource;
import java.io.StringWriter;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class TemplateService {

    private static Logger logger = LoggerFactory.getLogger(TemplateService.class);

    @Resource
    TemplateRepository templateRepository;

    /**
     * Find a template by its unique name
     * @param name
     * @return
     */
    public Template findOneByName(String name) {
        return templateRepository.findOneByName(name);
    }

    /**
     * Fill placeholders in a template with given values
     * @param templateName
     * @param values
     * @return Formatted and filled template
     */
    public String fillTemplateWithValuesAndGet(String templateName, Map<String, Object> values) {
        String result = "";
        final VelocityContext velocityContext = new VelocityContext();

        if(values != null) {
            values.forEach((k, v) -> velocityContext.put(k, v));
        }

        try (StringWriter sw = new StringWriter()) {
            Template template = findOneByName(templateName);
            if (template != null) {
                Velocity.evaluate(velocityContext, sw, "",template.getContent());
                result = sw.toString();
            }
        } catch (final Exception e) {
            logger.error("An error has occurred during resolve a template!", e);
        }

        return result;
    }
}
