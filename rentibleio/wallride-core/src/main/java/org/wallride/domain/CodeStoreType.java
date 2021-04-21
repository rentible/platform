package org.wallride.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "code_store_type", schema = "public")
public class CodeStoreType {

    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "codeStoreType", fetch = FetchType.EAGER)
    List<CodeStoreItem> codeStoreItems;

    @Column(updatable = false, insertable = false)
    private String caption;

    @Column(updatable = false, insertable = false)
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
