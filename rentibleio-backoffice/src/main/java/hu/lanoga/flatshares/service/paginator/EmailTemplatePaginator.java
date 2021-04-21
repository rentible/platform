package hu.lanoga.flatshares.service.paginator;

import hu.lanoga.flatshares.model.EmailTemplate;
import hu.lanoga.flatshares.service.EmailTemplateService;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
//If you use with request scope, then command link won't work in datatable! https://stackoverflow.com/questions/25409316/commandlink-not-working-on-a-lazy-loaded-primefaces-datascroller/37690739#37690739
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EmailTemplatePaginator {

    private LazyDataModel model;


    public EmailTemplatePaginator(EmailTemplateService emailTemplateService) {
        this.model = new LazyDataModel() {
            @Override
            public List<EmailTemplate> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {

                List<EmailTemplate> emailTemplates = emailTemplateService.findAllWithPaginationAndFilterBy(first, pageSize, filters);
                setRowCount(emailTemplateService.countByFilters(filters));

                return emailTemplates;
            }
        };
    }

    public LazyDataModel getModel() {
        return this.model;
    }
}
