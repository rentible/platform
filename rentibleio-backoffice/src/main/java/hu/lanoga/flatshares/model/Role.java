package hu.lanoga.flatshares.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Role {
    private Integer id;
    private String name;
    private Integer createdBy;
    private Timestamp createdOn;
    private Integer modifiedBy;
    private Timestamp modifiedOn;
}
