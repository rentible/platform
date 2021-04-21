package hu.lanoga.flatshares.service;

import hu.lanoga.flatshares.SysKeys;
import hu.lanoga.flatshares.exception.SecurityException;
import hu.lanoga.flatshares.model.User;
import hu.lanoga.flatshares.model.UserPrincipal;
import hu.lanoga.flatshares.repository.RoleMapper;
import hu.lanoga.flatshares.repository.UserMapper;
import hu.lanoga.flatshares.util.DateAndTimeUtil;
import hu.lanoga.flatshares.util.ObjectUtil;
import hu.lanoga.flatshares.util.PrimeFacesUtil;
import hu.lanoga.flatshares.util.SecurityUtil;
import hu.lanoga.flatshares.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.SqlTable;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategy;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.where.render.WhereClauseProvider;
import org.owasp.encoder.Encode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.JDBCType;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

    private static final String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final EmailTemplateService emailTemplateService;
    private final UserDetailService userDetailService;

    public UserService(UserMapper userMapper, RoleMapper roleMapper, EmailTemplateService emailTemplateService, UserDetailService userDetailService) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.emailTemplateService = emailTemplateService;
        this.userDetailService = userDetailService;
    }

    public User findOne(int id) {
        User user = userMapper.findOne(id);
        user.setUserDetail(userDetailService.findOne(user.getSchema(), user.getId()));

        return user;
    }

    /**
     * Save a user and a user detail into the target schema.
     *
     * @param schema target schema.
     * @param user
     * @return number of rows affected. It has to be zero or one.
     */
    public int saveWithUserDetail(String schema, User user) {
        try {

            int result = this.save(user);
            userDetailService.save(schema, user.getUserDetail());

            return result;
        } catch (Exception e) {
            log.error("An error has occurred during user saveWithUserDetail: ", e);
            return 0;
        }
    }

    public int save(User user) {
        final SqlTable userTable = SqlTable.of("public.user");

        InsertStatementProvider<User> insertStatementProvider = org.mybatis.dynamic.sql.SqlBuilder.insert(user)
                .into(userTable)
                .map(userTable.column("username", JDBCType.VARCHAR)).toPropertyWhenPresent("username", user::getUsername)
                .map(userTable.column("email", JDBCType.VARCHAR)).toPropertyWhenPresent("email", user::getEmail)
                .map(userTable.column("password", JDBCType.VARCHAR)).toPropertyWhenPresent("password", user::getPassword)
                .map(userTable.column("schema_id", JDBCType.INTEGER)).toPropertyWhenPresent("schemaId", user::getSchemaId)
                .map(userTable.column("google_auth", JDBCType.BOOLEAN)).toPropertyWhenPresent("googleAuth", user::isGoogleAuth)
                .map(userTable.column("google_auth_qr_url", JDBCType.VARCHAR)).toPropertyWhenPresent("googleAuthQrUrl", user::getGoogleAuthQrUrl)
                .map(userTable.column("enabled", JDBCType.BOOLEAN)).toPropertyWhenPresent("enabled", user::isEnabled)
                .map(userTable.column("activation_token", JDBCType.VARCHAR)).toPropertyWhenPresent("activationToken", user::getActivationToken)
                .map(userTable.column("last_logged_in", JDBCType.TIMESTAMP)).toPropertyWhenPresent("lastLoggedIn", user::getLastLoggedIn)
                .map(userTable.column("number_of_login", JDBCType.INTEGER)).toPropertyWhenPresent("numberOfLogin", user::getNumberOfLogin)
                .map(userTable.column("online", JDBCType.BOOLEAN)).toPropertyWhenPresent("online", user::getOnline)
                .map(userTable.column("secret", JDBCType.VARCHAR)).toPropertyWhenPresent("secret", user::getSecret)
                .map(userTable.column("created_on", JDBCType.TIMESTAMP)).toPropertyWhenPresent("createdOn", user::getCreatedOn)
                .map(userTable.column("created_by", JDBCType.INTEGER)).toPropertyWhenPresent("createdBy", user::getCreatedBy)
                .map(userTable.column("modified_on", JDBCType.TIMESTAMP)).toPropertyWhenPresent("modifiedOn", user::getModifiedOn)
                .map(userTable.column("modified_by", JDBCType.INTEGER)).toPropertyWhenPresent("modifiedBy", user::getModifiedBy)
                .build()
                .render(RenderingStrategy.MYBATIS3);

        return userMapper.save(insertStatementProvider);
    }

    /**
     * Update a user record, if user properties are present
     *
     * @param user
     * @return
     */
    public int update(User user) {
        final SqlTable userTable = SqlTable.of("public.user");

        UpdateStatementProvider updateStatementProvider = org.mybatis.dynamic.sql.SqlBuilder.update(userTable)
                .set(userTable.column("username", JDBCType.VARCHAR)).equalToWhenPresent(user.getUsername())
                .set(userTable.column("email", JDBCType.VARCHAR)).equalToWhenPresent(user.getEmail())
                .set(userTable.column("number_of_login", JDBCType.INTEGER)).equalToWhenPresent(user.getNumberOfLogin())
                .set(userTable.column("online", JDBCType.BOOLEAN)).equalToWhenPresent(user.getOnline())
                .set(userTable.column("language", JDBCType.INTEGER)).equalToWhenPresent(user.getLanguage())
//                .set(userTable.column("password", JDBCType.VARCHAR)).equalToWhenPresent(user.getPassword())
                .set(userTable.column("google_auth", JDBCType.BOOLEAN)).equalToWhenPresent(user.isGoogleAuth())
                .set(userTable.column("google_auth_qr_url", JDBCType.VARCHAR)).equalToWhenPresent(user.getGoogleAuthQrUrl())
                .set(userTable.column("secret", JDBCType.VARCHAR)).equalToWhenPresent(user.getSecret())
                .set(userTable.column("enabled", JDBCType.BOOLEAN)).equalToWhenPresent(user.isEnabled())
                .set(userTable.column("activation_token", JDBCType.VARCHAR)).equalToWhenPresent(user.getActivationToken())
                .set(userTable.column("last_logged_in", JDBCType.TIMESTAMP)).equalToWhenPresent(user.getLastLoggedIn())
                .set(userTable.column("created_on", JDBCType.TIMESTAMP)).equalToWhenPresent(user.getCreatedOn())
                .set(userTable.column("created_by", JDBCType.INTEGER)).equalToWhenPresent(user.getCreatedBy())
                .set(userTable.column("modified_on", JDBCType.TIMESTAMP)).equalToWhenPresent(user.getModifiedOn())
                .set(userTable.column("modified_by", JDBCType.INTEGER)).equalToWhenPresent(user.getModifiedBy())
                .where(userTable.column("id", JDBCType.INTEGER), org.mybatis.dynamic.sql.SqlBuilder.isEqualTo(user.getId()))
                .build()
                .render(RenderingStrategy.MYBATIS3);

        if (ObjectUtil.checkAllFieldNullOrEmpty(user.getUserDetail())) {
            userDetailService.update(user.getSchema(), user.getUserDetail());
        }

        return userMapper.update(updateStatementProvider);
    }

    public int updateWithoutUserDetail(User user) {
        final SqlTable userTable = SqlTable.of("public.user");

        UpdateStatementProvider updateStatementProvider = org.mybatis.dynamic.sql.SqlBuilder.update(userTable)
                .set(userTable.column("username", JDBCType.VARCHAR)).equalToWhenPresent(user.getUsername())
                .set(userTable.column("email", JDBCType.VARCHAR)).equalToWhenPresent(user.getEmail())
                .set(userTable.column("number_of_login", JDBCType.INTEGER)).equalToWhenPresent(user.getNumberOfLogin())
                .set(userTable.column("online", JDBCType.BOOLEAN)).equalToWhenPresent(user.getOnline())
                .set(userTable.column("language", JDBCType.INTEGER)).equalToWhenPresent(user.getLanguage())
//                .set(userTable.column("password", JDBCType.VARCHAR)).equalToWhenPresent(user.getPassword())
                .set(userTable.column("google_auth", JDBCType.BOOLEAN)).equalToWhenPresent(user.isGoogleAuth())
                .set(userTable.column("google_auth_qr_url", JDBCType.VARCHAR)).equalToWhenPresent(user.getGoogleAuthQrUrl())
                .set(userTable.column("secret", JDBCType.VARCHAR)).equalToWhenPresent(user.getSecret())
                .set(userTable.column("enabled", JDBCType.BOOLEAN)).equalToWhenPresent(user.isEnabled())
                .set(userTable.column("activation_token", JDBCType.VARCHAR)).equalToWhenPresent(user.getActivationToken())
                .set(userTable.column("last_logged_in", JDBCType.TIMESTAMP)).equalToWhenPresent(user.getLastLoggedIn())
                .set(userTable.column("created_on", JDBCType.TIMESTAMP)).equalToWhenPresent(user.getCreatedOn())
                .set(userTable.column("created_by", JDBCType.INTEGER)).equalToWhenPresent(user.getCreatedBy())
                .set(userTable.column("modified_on", JDBCType.TIMESTAMP)).equalToWhenPresent(user.getModifiedOn())
                .set(userTable.column("modified_by", JDBCType.INTEGER)).equalToWhenPresent(user.getModifiedBy())
                .where(userTable.column("id", JDBCType.INTEGER), org.mybatis.dynamic.sql.SqlBuilder.isEqualTo(user.getId()))
                .build()
                .render(RenderingStrategy.MYBATIS3);

        return userMapper.update(updateStatementProvider);
    }


    public List<User> findAll() {
        return userMapper.findAll();
    }

    public void delete(int id) {
        throw new UnsupportedOperationException();
    }

    public int count() {
        return userMapper.count();
    }

    public int countByFilters(Map filters) {

        WhereClauseProvider whereClause = buildWhereClauseProvideByFilters(filters);

        return userMapper.countBy(whereClause);
    }

    public void register(User user) {

        if (StringUtils.isBlank(user.getUsername())) {
            user.setUsername(user.getEmail());
        }

        Timestamp now = DateAndTimeUtil.now();

        user.setEnabled(true);
        user.setCreatedOn(now);
        user.setModifiedOn(now);
        user.setPassword(new BCryptPasswordEncoder(11).encode(user.getPassword()));

        if (Boolean.TRUE.equals(user.isGoogleAuth())) {
            user.setGoogleAuthQrUrl(generateQRUrl(user));
        }
        //If there is no schemaId set, then CMS_HUN(700) will be the default
        if (user.getSchemaId() == null) {
            user.setSchemaId(SysKeys.CSI_ID_SCHEMA_CMS_HUN);
        }

        try {
            this.saveWithUserDetail(user.getSchema(), user);
            this.addRole(user, SysKeys.ROLE_LAND_LORD);

            Map<String, Object> params = new HashMap<>();
            params.put("recipient", user.getEmail());
            params.put("appname", "Flatshares Team");

            emailTemplateService.addMail(user.getEmail(), SysKeys.EMAIL_TEMPLATE_USER_REGISTRATION, params);

            if (user.isGoogleAuth()) {
                PrimeFacesUtil.redirect("/pages/public/qrcode?id=" + user.getId());
            } else {
                PrimeFacesUtil.redirect("/pages/public/login.xhtml");
            }
        } catch (Exception e) {
            log.error("An error has occurred during a user registration: ", e);

            PrimeFacesUtil.redirect("/pages/public/register?emailError=true");
        }
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    /**
     * @return my own user's id
     */
    public int getMyId() {

        return SecurityUtil.getLoggedInUser().getId();
    }

    /**
     * @return my own user's schema
     */
    public String getMySchema() {

        return SecurityUtil.getLoggedInUser().getSchema();
    }

    public String getMyUsername() {
        return SecurityUtil.getLoggedInUser().getEmail();
    }

    public void addRole(User user, int rolId) {
        userMapper.addRole(user.getId(), rolId);
    }

    private String generateQRUrl(User user) {
        try {
            return QR_PREFIX + URLEncoder.encode(String.format(
                    "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                    "FlatShares", user.getEmail(), user.getSecret(), "FlatShares"),
                    "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("{}", e);
        }
        return "";
    }

    public void ban(int id) {
        final SqlTable userTable = SqlTable.of("public.user");
        UpdateStatementProvider updateStatementProvider = org.mybatis.dynamic.sql.SqlBuilder.update(userTable)
                .set(userTable.column("enabled", JDBCType.BOOLEAN)).equalTo(false)
                .where(userTable.column("id", JDBCType.INTEGER), org.mybatis.dynamic.sql.SqlBuilder.isEqualTo(id))
                .build()
                .render(RenderingStrategy.MYBATIS3);

        userMapper.update(updateStatementProvider);
    }

    /**
     * Impersonate a user(temporary log in as an other user)
     *
     * @param id
     */
    public void impersonate(int id) {
        User user = userMapper.findOne(id);

        if (user != null) {
            user.setRoles(roleMapper.findAllByUserId(user.getId()));
            SecurityUtil.setUser(new UserPrincipal(user));
            PrimeFacesUtil.redirect("/pages/dashboard");
        } else {
            throw new SecurityException(Encode.forJava("Can not impersonate user by id: " + id));
        }
    }

    /**
     * @param offset
     * @param limit
     * @param filters
     * @return
     */
    public List<User> findAllWithPaginationAndFilterBy(int offset, int limit, Map filters) {

        WhereClauseProvider whereClause = buildWhereClauseProvideByFilters(filters);

        return userMapper.findAllWithPaginationAndFilterBy(offset, limit, whereClause);
    }

    /**
     * Build where clause provider based on filters
     *
     * @param filters
     * @return
     */
    private WhereClauseProvider buildWhereClauseProvideByFilters(Map filters) {
        final SqlTable userTable = SqlTable.of("public.user");

        String username = null;
        Integer userId = null;

        if (filters != null && filters.size() > 0) {
            username = StringUtil.isNotBlackAndNull(String.valueOf(filters.get("username"))) ? String.valueOf(filters.get("username")) : null;

            if (filters.get("id") != null) {
                userId = Integer.parseInt(String.valueOf(filters.get("id")));
            }

        }

        return org.mybatis.dynamic.sql.SqlBuilder
                .where(userTable.column("id", JDBCType.INTEGER), SqlBuilder.isEqualToWhenPresent(userId))
                .and(userTable.column("username", JDBCType.VARCHAR), SqlBuilder.isLikeCaseInsensitiveWhenPresent(username))
                .build()
                .render(RenderingStrategy.MYBATIS3, "whereClauseProvider");
    }

}
