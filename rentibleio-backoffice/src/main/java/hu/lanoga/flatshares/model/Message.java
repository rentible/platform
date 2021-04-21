package hu.lanoga.flatshares.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
public class Message {

    private Long id;
    private Integer senderUserId;
    private Integer recipientUserId;
    private String content;
    private String contentType;
    private boolean seen;
    private boolean archive;

    @NotNull
    private Timestamp createdOn;

    @NotNull
    private Integer createdBy;

    @NotNull
    private Timestamp modifiedOn;

    @NotNull
    private Integer modifiedBy;
}
