package hu.lanoga.flatshares.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
public class UserDetail {
    private Integer id;
    @NotNull(message = "user_id can not be null!")
    private Integer userId;
    private String firstName;
    private String lastName;
    private Timestamp dateOfBirth;
    private Integer profileImage;
    private Integer gender;
    private String phoneNumber;
    private Integer normalCredit;
    private Integer fbCredit;
    private Timestamp liftUpExpire;
    private Timestamp showLogoExpire;
    private Timestamp showUrlExpire;
    private String fbId;
    private Integer mailSmart;
    private Integer mailMessage;
    private Integer mailInterest;
    private Integer mailNews;
    private Timestamp emailApprovedOn;
    private String avatar;
    private Boolean adminVerified;
    private String banNote;
    private String verificationCode;
    private Integer originalId;
}
