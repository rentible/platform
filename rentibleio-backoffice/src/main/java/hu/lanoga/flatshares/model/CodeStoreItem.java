package hu.lanoga.flatshares.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CodeStoreItem {

    private Integer id;
    private Integer codeStoreTypeId;
    private String caption;
    private Boolean enabled;
    private Timestamp createdOn;
    private Integer createdBy;
    private Timestamp modifiedOn;
    private Integer modifiedBy;
}
