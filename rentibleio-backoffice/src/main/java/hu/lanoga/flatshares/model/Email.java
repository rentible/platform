package hu.lanoga.flatshares.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class Email {

    private Integer id;
    private String fromEmail;
    private String toEmail;
    private String subject;
    private String body;
    private Boolean isPlainText;
    private Boolean success;
    private Integer attempt;

    @NotNull
    private Timestamp createdOn;

    @NotNull
    private Integer createdBy;

    @NotNull
    private Timestamp modifiedOn;

    @NotNull
    private Integer modifiedBy;
}
