//package org.wallride.flatshares.domain;
//
//
//import org.wallride.domain.Property;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
//import java.sql.Timestamp;
//
//
//public class FileDescriptor {
//
//    @Id
//    @GeneratedValue
//    private Integer id;
//    private String uuid;
//
//    @NotEmpty
//    private String originalName;
//
//    @NotEmpty
//    private String filePath;
//    private String mimeType;
//    private Long fileSize;
//
//    private Property property;
//
//    private Property propertyMainImage;
//
//    @NotNull
//    private Timestamp createdOn;
//
//    @NotNull
//    private Integer createdBy;
//
//    @NotNull
//    private Timestamp modifiedOn;
//
//    @NotNull
//    private Integer modifiedBy;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getUuid() {
//        return uuid;
//    }
//
//    public void setUuid(String uuid) {
//        this.uuid = uuid;
//    }
//
//    public String getOriginalName() {
//        return originalName;
//    }
//
//    public void setOriginalName(String originalName) {
//        this.originalName = originalName;
//    }
//
//    public String getFilePath() {
//        return filePath;
//    }
//
//    public void setFilePath(String filePath) {
//        this.filePath = filePath;
//    }
//
//    public String getMimeType() {
//        return mimeType;
//    }
//
//    public void setMimeType(String mimeType) {
//        this.mimeType = mimeType;
//    }
//
//    public Long getFileSize() {
//        return fileSize;
//    }
//
//    public void setFileSize(Long fileSize) {
//        this.fileSize = fileSize;
//    }
//
//    public Property getProperty() {
//        return property;
//    }
//
//    public void setProperty(Property property) {
//        this.property = property;
//    }
//
//    public Timestamp getCreatedOn() {
//        return createdOn;
//    }
//
//    public void setCreatedOn(Timestamp createdOn) {
//        this.createdOn = createdOn;
//    }
//
//    public Integer getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(Integer createdBy) {
//        this.createdBy = createdBy;
//    }
//
//    public Timestamp getModifiedOn() {
//        return modifiedOn;
//    }
//
//    public void setModifiedOn(Timestamp modifiedOn) {
//        this.modifiedOn = modifiedOn;
//    }
//
//    public Integer getModifiedBy() {
//        return modifiedBy;
//    }
//
//    public void setModifiedBy(Integer modifiedBy) {
//        this.modifiedBy = modifiedBy;
//    }
//}
