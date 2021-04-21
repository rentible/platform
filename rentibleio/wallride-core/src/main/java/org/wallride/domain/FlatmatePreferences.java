package org.wallride.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "flatmate_preferences", schema = "cms_hun")
public class FlatmatePreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer ageFrom;

    @Column
    private Integer ageTo;

    @Column
    private Integer occupation;

    @Column
    private Integer studiesAt;

//    @Column
//    private List<Integer> speaks;

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
