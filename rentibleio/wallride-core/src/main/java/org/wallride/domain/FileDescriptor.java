package org.wallride.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "file_descriptor", schema = "public")
public class FileDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;

    @NotEmpty
    @Column
    private String originalName;

    @NotEmpty
    @Column
    private String filePath;
    private String mimeType;
    private Long fileSize;

    //    @JsonDeserialize(using = StringValueDeserializer.class)
//    @Column
//    private String description;

    @NotNull
    @Column(updatable = false)
    private Timestamp createdOn;

    @NotNull
    @Column(updatable = false)
    private Long createdBy;

    @NotNull
    @Column
    private Timestamp modifiedOn;

    @NotNull
    @Column
    private Long modifiedBy;
}
