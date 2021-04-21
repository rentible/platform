package hu.lanoga.flatshares.service;

import hu.lanoga.flatshares.model.UserDetail;
import hu.lanoga.flatshares.repository.UserDetailMapper;
import hu.lanoga.flatshares.util.ObjectUtil;
import org.mybatis.dynamic.sql.SqlTable;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategy;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.springframework.stereotype.Service;

import java.sql.JDBCType;
import java.util.List;

@Service
public class UserDetailService {

    private UserDetailMapper userDetailMapper;

    public UserDetailService(UserDetailMapper userDetailMapper) {
        this.userDetailMapper = userDetailMapper;
    }


    /**
     * Save a UserDetail
     *
     * @param schema     target schema
     * @param userDetail
     * @return number of updated record
     */
    public int save(String schema, UserDetail userDetail) {
        if (ObjectUtil.checkAllFieldNullOrEmpty(userDetail)) {
            final SqlTable userDetailTable = SqlTable.of(schema + ".user_detail");

            InsertStatementProvider<UserDetail> insertStatementProvider = org.mybatis.dynamic.sql.SqlBuilder.insert(userDetail)
                    .into(userDetailTable)
                    .map(userDetailTable.column("user_id", JDBCType.INTEGER)).toPropertyWhenPresent("userId", userDetail::getUserId)
                    .map(userDetailTable.column("first_name", JDBCType.VARCHAR)).toPropertyWhenPresent("firstName", userDetail::getFirstName)
                    .map(userDetailTable.column("last_name", JDBCType.VARCHAR)).toPropertyWhenPresent("lastName", userDetail::getLastName)
                    .map(userDetailTable.column("date_of_birth", JDBCType.TIMESTAMP)).toPropertyWhenPresent("dateOfBirth", userDetail::getDateOfBirth)
                    .map(userDetailTable.column("gender", JDBCType.INTEGER)).toPropertyWhenPresent("gender", userDetail::getGender)
                    .map(userDetailTable.column("phone_number", JDBCType.VARCHAR)).toPropertyWhenPresent("phoneNumber", userDetail::getPhoneNumber)
                    .map(userDetailTable.column("normal_credit", JDBCType.INTEGER)).toPropertyWhenPresent("normalCredit", userDetail::getNormalCredit)
                    .map(userDetailTable.column("fb_credit", JDBCType.INTEGER)).toPropertyWhenPresent("fbCredit", userDetail::getFbCredit)
                    .map(userDetailTable.column("lift_up_expire", JDBCType.TIMESTAMP)).toPropertyWhenPresent("liftUpExpire", userDetail::getLiftUpExpire)
                    .map(userDetailTable.column("show_logo_expire", JDBCType.TIMESTAMP)).toPropertyWhenPresent("showLogoExpire", userDetail::getShowLogoExpire)
                    .map(userDetailTable.column("show_url_expire", JDBCType.TIMESTAMP)).toPropertyWhenPresent("showUrlExpire", userDetail::getShowUrlExpire)
                    .map(userDetailTable.column("fb_id", JDBCType.VARCHAR)).toPropertyWhenPresent("fbId", userDetail::getFbId)
                    .map(userDetailTable.column("mail_smart", JDBCType.INTEGER)).toPropertyWhenPresent("mailSmart", userDetail::getMailSmart)
                    .map(userDetailTable.column("mail_message", JDBCType.INTEGER)).toPropertyWhenPresent("mailMessage", userDetail::getMailMessage)
                    .map(userDetailTable.column("mail_interest", JDBCType.INTEGER)).toPropertyWhenPresent("mailInterest", userDetail::getMailInterest)
                    .map(userDetailTable.column("mail_news", JDBCType.INTEGER)).toPropertyWhenPresent("mailNews", userDetail::getMailNews)
                    .map(userDetailTable.column("email_approved_on", JDBCType.TIMESTAMP)).toPropertyWhenPresent("emailApprovedOn", userDetail::getEmailApprovedOn)
                    .map(userDetailTable.column("avatar", JDBCType.VARCHAR)).toPropertyWhenPresent("avatar", userDetail::getAvatar)
                    .map(userDetailTable.column("admin_verified", JDBCType.BOOLEAN)).toPropertyWhenPresent("adminVerified", userDetail::getAdminVerified)
                    .map(userDetailTable.column("ban_note", JDBCType.VARCHAR)).toPropertyWhenPresent("banNote", userDetail::getBanNote)
                    .build()
                    .render(RenderingStrategy.MYBATIS3);

            return userDetailMapper.save(insertStatementProvider);
        } else {
            return 0;
        }
    }


    /**
     * Update a UserDetail entity by id
     *
     * @param schema     target schema
     * @param userDetail
     * @return number of updated record
     */
    public int update(String schema, UserDetail userDetail) {
        final SqlTable userDetailTable = SqlTable.of(schema + ".user_detail");

        UpdateStatementProvider updateStatementProvider = org.mybatis.dynamic.sql.SqlBuilder.update(userDetailTable)
                .set(userDetailTable.column("user_id", JDBCType.INTEGER)).equalToWhenPresent(userDetail.getUserId())
                .set(userDetailTable.column("first_name", JDBCType.VARCHAR)).equalToWhenPresent(userDetail.getFirstName())
                .set(userDetailTable.column("last_name", JDBCType.VARCHAR)).equalToWhenPresent(userDetail.getLastName())
                .set(userDetailTable.column("date_of_birth", JDBCType.TIMESTAMP)).equalToWhenPresent(userDetail.getDateOfBirth())
                .set(userDetailTable.column("gender", JDBCType.INTEGER)).equalToWhenPresent(userDetail.getGender())
                .set(userDetailTable.column("phone_number", JDBCType.VARCHAR)).equalToWhenPresent(userDetail.getPhoneNumber())
                .set(userDetailTable.column("normal_credit", JDBCType.INTEGER)).equalToWhenPresent(userDetail.getNormalCredit())
                .set(userDetailTable.column("fb_credit", JDBCType.INTEGER)).equalToWhenPresent(userDetail.getFbCredit())
                .set(userDetailTable.column("lift_up_expire", JDBCType.TIMESTAMP)).equalToWhenPresent(userDetail.getLiftUpExpire())
                .set(userDetailTable.column("show_logo_expire", JDBCType.TIMESTAMP)).equalToWhenPresent(userDetail.getShowLogoExpire())
                .set(userDetailTable.column("show_url_expire", JDBCType.TIMESTAMP)).equalToWhenPresent(userDetail.getShowUrlExpire())
                .set(userDetailTable.column("fb_id", JDBCType.VARCHAR)).equalToWhenPresent(userDetail.getFbId())
                .set(userDetailTable.column("mail_smart", JDBCType.INTEGER)).equalToWhenPresent(userDetail.getMailSmart())
                .set(userDetailTable.column("mail_message", JDBCType.INTEGER)).equalToWhenPresent(userDetail.getMailMessage())
                .set(userDetailTable.column("mail_interest", JDBCType.INTEGER)).equalToWhenPresent(userDetail.getMailInterest())
                .set(userDetailTable.column("mail_news", JDBCType.INTEGER)).equalToWhenPresent(userDetail.getMailNews())
                .set(userDetailTable.column("email_approved_on", JDBCType.TIMESTAMP)).equalToWhenPresent(userDetail.getEmailApprovedOn())
                .set(userDetailTable.column("avatar", JDBCType.VARCHAR)).equalToWhenPresent(userDetail.getAvatar())
                .set(userDetailTable.column("admin_verified", JDBCType.BOOLEAN)).equalToWhenPresent(userDetail.getAdminVerified())
                .set(userDetailTable.column("ban_note", JDBCType.VARCHAR)).equalToWhenPresent(userDetail.getBanNote())
                .where(userDetailTable.column("id", JDBCType.INTEGER), org.mybatis.dynamic.sql.SqlBuilder.isEqualTo(userDetail.getId()))
                .build()
                .render(RenderingStrategy.MYBATIS3);

        return userDetailMapper.update(updateStatementProvider);
    }

    public UserDetail findOne(String schema, int userId) {
        return userDetailMapper.findOne(schema, userId);
    }

    /**
     * Find all userDetail record in the source schema
     *
     * @param schema source schema
     * @return all userDetail record in the source schema
     */
    public List<UserDetail> findAll(String schema) {
        return userDetailMapper.findAll(schema);
    }

    /**
     * Delete record by id
     *
     * @param schema target schema
     * @param id
     * @return number of deleted records - it has to one or zero
     */
    public int delete(String schema, int id) {
        return userDetailMapper.delete(schema, id);
    }
}
