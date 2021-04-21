package hu.lanoga.flatshares.service.paginator;

import hu.lanoga.flatshares.model.User;
import hu.lanoga.flatshares.service.UserService;
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
public class UserPaginator {

    private LazyDataModel model;

    public UserPaginator(UserService userService) {
        this.model = new LazyDataModel() {
            @Override
            public List<User> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {

                List<User> users = userService.findAllWithPaginationAndFilterBy(first, pageSize, filters);
                setRowCount(userService.countByFilters(filters));

                return users;
            }
        };
    }

    public LazyDataModel getModel() {
        return this.model;
    }
}
