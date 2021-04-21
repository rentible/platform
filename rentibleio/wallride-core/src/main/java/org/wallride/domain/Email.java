package org.wallride.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "email", schema = "public")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private String fromEmail;

    @Column(updatable = false)
    private String toEmail;

    @Column(updatable = false)
    private String subject;

    @Column(updatable = false)
    private String body;

    @Column(updatable = false)
    private Boolean isPlainText;

	@Column
	private Boolean success;

	@Column
	private Integer attempt;

    @NotNull
    @Column(updatable = false)
    private Timestamp createdOn;

    @Column(updatable = false)
    private Long createdBy;

    @NotNull
    @Column
    private Timestamp modifiedOn;

    @Column
    private Long modifiedBy;
}
