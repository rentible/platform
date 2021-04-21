package org.wallride.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "address", schema = "cms_hun")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String address1;

    @Column
    private String address2;

    @Column
    private String address3;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district", referencedColumnName = "id")
    private CodeStoreItem district;

    @Column
    private String city;

    @Column
    private String zip;

    @Column
    private String country;

    @Column
    private Boolean enabled;

    @Column
    @NotNull
    private Timestamp createdOn;

    @Column
    @NotNull
    private Long createdBy;

    @Column
    @NotNull
    private Timestamp modifiedOn;

    @Column
    @NotNull
    private Long modifiedBy;

}
