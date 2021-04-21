package hu.lanoga.flatshares.service.paginator;

import hu.lanoga.flatshares.model.CodeStoreItem;
import hu.lanoga.flatshares.service.CodeStoreItemService;
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
public class CodeStoreItemPaginator {

    private LazyDataModel model;


    public CodeStoreItemPaginator(CodeStoreItemService codeStoreItemService) {
        this.model = new LazyDataModel() {
            @Override
            public List<CodeStoreItem> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {

                List<CodeStoreItem> codeStoreItems = codeStoreItemService.findAllWithPaginationAndFilterBy(first, pageSize, filters);
                setRowCount(codeStoreItemService.countByFilters(filters));

                return codeStoreItems;
            }
        };

    }

    public LazyDataModel getModel() {
        return this.model;
    }
}
