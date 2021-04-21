package org.wallride.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.wallride.exception.WallrideGeneralException;
import org.wallride.support.StringValueDeserializer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "code_store_item", schema = "public")
public class CodeStoreItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "code_store_type_id", referencedColumnName = "id")
    private CodeStoreType codeStoreType;

    @Column(updatable = false, insertable = false)
    @JsonDeserialize(using = StringValueDeserializer.class)
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

    /**
     * @param language
     * @return the caption in selected language
     */
    public String getCaption(String language) {

        if (StringUtils.isNotBlank(language)) {
            JSONObject jsonObject = new JSONObject(caption);

            if (jsonObject.length() == 1) {
                return (String) jsonObject.get("en");
            } else {
                return (String) jsonObject.get(language);
            }
        } else {
            throw new WallrideGeneralException("Missing language parameter");
        }
    }
}
