package hu.lanoga.flatshares.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Getter
@Setter
public class EmailTemplate {

	private Integer id;

	@NotNull
	private Integer templCode;

	@NotEmpty
	private String content;

	@Size(max = 50)
	private String subject;

	private Boolean enabled;

    @NotNull
    private Timestamp createdOn;

    @NotNull
    private Integer createdBy;

    @NotNull
    private Timestamp modifiedOn;

    @NotNull
    private Integer modifiedBy;
}
