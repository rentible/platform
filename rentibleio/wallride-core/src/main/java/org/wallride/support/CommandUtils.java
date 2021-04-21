package org.wallride.support;

import org.wallride.domain.Property;
import org.wallride.service.PropertyService;
import org.wallride.service.TemplateService;

import java.util.HashMap;
import java.util.Map;

public class CommandUtils {

    public static final String COMMAND_BEGIN = "<pre>#cmd_begin#";
    public static final String COMMAND_END = "#cmd_end#</pre>";

    public static final String GET_PROPERTIES = "get_properties";

    //All part of property template
    private static class PropertyTemplatePart {
        public static String HEADER = "property_header";
        public static String CONTENT = "property_content";
        public static String FOOTER = "property_footer";
    }

    public static String getHtml(String command, Object service, TemplateService templateService) {

        switch (command) {
            case GET_PROPERTIES:

                StringBuilder sbBody = new StringBuilder();

                sbBody.append(templateService.findOneByName(PropertyTemplatePart.HEADER).getContent());

                //TODO rest endpoint-ról jöjjön vagy service-ből?
                for (Property p : ((PropertyService) service).findAll()) {
                    Map<String, Object> values = new HashMap<>();
                    values.put("title", p.getTitle());
                    values.put("description", p.getDescription());
                    values.put("rental", p.getRentPrice());

                    sbBody.append(templateService.fillTemplateWithValuesAndGet(PropertyTemplatePart.CONTENT, values));
                }

                sbBody.append(templateService.findOneByName(PropertyTemplatePart.FOOTER).getContent());

                return sbBody.toString();
            default:
                return "";
        }
    }

    private CommandUtils() {
    }
}
