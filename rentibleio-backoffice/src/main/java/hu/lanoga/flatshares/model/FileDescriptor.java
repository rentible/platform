package hu.lanoga.flatshares.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import hu.lanoga.flatshares.config.StringValueDeserializer;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;


@Getter
@Setter
public class FileDescriptor {

    private Integer id;
    private String uuid;

    @NotEmpty
    private String originalName;

    @NotEmpty
    private String filePath;
    private String mimeType;
    private Long fileSize;
    @JsonDeserialize(using = StringValueDeserializer.class)
    private String description;

    @NotNull
    private Timestamp createdOn;

    @NotNull
    private Integer createdBy;

    @NotNull
    private Timestamp modifiedOn;

    @NotNull
    private Integer modifiedBy;
}
