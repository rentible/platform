package hu.lanoga.flatshares.model;

import hu.lanoga.flatshares.service.CodeStoreItemService;
import hu.lanoga.flatshares.service.UserDetailService;
import hu.lanoga.flatshares.util.ContextProvider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class User {

    private Integer id;
    private String username;
    @Email(message = "Email should be valid")
    private String email;
    @NotNull(message = "Password cannot be null")
    private String password;
    private Integer schemaId;
    @Getter(AccessLevel.NONE)
    private String schema;
    private boolean googleAuth; //TODO it does not work!
    private String googleAuthQrUrl;
    private String secret;
    private boolean enabled;
    private String activationToken;
    private Integer numberOfLogin;
    private Timestamp lastLoggedIn;
    private Integer language;
    private Boolean online;
    @NotNull
    private Timestamp createdOn;

    @NotNull
    private Integer createdBy;

    @NotNull
    private Timestamp modifiedOn;

    @NotNull
    private Integer modifiedBy;
    private List<Role> roles;
    private UserDetail userDetail;

    public String getSchema() {
        if (StringUtils.isBlank(this.schema)) {
            return ContextProvider.getBean(CodeStoreItemService.class).findOne(this.getSchemaId()).getCaption();
        } else {
            return this.schema;
        }
    }

    public UserDetail getUserDetail() {
        if (this.userDetail == null) {
            return ContextProvider.getBean(UserDetailService.class).findOne(this.getSchema(), this.getId());
        } else {
            return this.userDetail;
        }
    }
}
