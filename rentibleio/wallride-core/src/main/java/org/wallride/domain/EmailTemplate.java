package org.wallride.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "email_template", schema = "public")
public class EmailTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(updatable = false, insertable = false)
    private Integer templCode;

    @Column(updatable = false, insertable = false)
    private String content;

    @Column(updatable = false, insertable = false)//@Size(max = 50)
    private String subject;

    @Column(updatable = false, insertable = false)//
    private Boolean enabled;

    @Column(updatable = false, insertable = false)
    private Timestamp createdOn;

    @Column(updatable = false, insertable = false)
    private Integer createdBy;

    @Column(updatable = false, insertable = false)
    private Timestamp modifiedOn;

    @Column(updatable = false, insertable = false)
    private Integer modifiedBy;
}
