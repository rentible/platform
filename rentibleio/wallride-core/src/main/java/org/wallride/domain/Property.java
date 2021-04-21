package org.wallride.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.wallride.support.googlemap.*;
import org.wallride.web.support.HashMapConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "property", schema = "cms_hun")
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rental_type", referencedColumnName = "id")
    private CodeStoreItem rentalType;

    @Column
    private String crossStreet1;

    @Column
    private String crossStreet2;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_type", referencedColumnName = "id")
    private CodeStoreItem propertyType;

    @Column
    private String referenceId;

    @Column
    private BigDecimal rentPrice;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rent_price_currency", referencedColumnName = "id")
    private CodeStoreItem rentPriceCurrency;

    @Column
    private BigDecimal deposit;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "deposit_currency", referencedColumnName = "id")
    private CodeStoreItem depositCurrency;

    @Column
    private Integer apartmentSize;

    @Column
    private String roomSize;

    @Column
    private Integer bedroom;

    @Column
    private Integer room;

    @Column
    private String bathroom;

    @Column
    private Integer toilet;

    @Column
    private Boolean furnished;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "rent_period_x_property", schema = "cms_hun",
            joinColumns = { @JoinColumn(name = "property_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "code_store_item_id", referencedColumnName = "id") }
    )
    Set<CodeStoreItem> termOfLease = new HashSet<>();

    @Column
    private Timestamp availableFrom;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "apartment_condition", referencedColumnName = "id")
    private CodeStoreItem apartmentCondition;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "view", referencedColumnName = "id")
    private CodeStoreItem view;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "floor", referencedColumnName = "id")
    private CodeStoreItem floor;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "heating", referencedColumnName = "id")
    private CodeStoreItem heating;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "building_condition", referencedColumnName = "id")
    private CodeStoreItem buildingCondition;

    @Column
    private Integer yearBuilt;

    @Column
    private BigDecimal commonCost;

    @Column
    private Boolean heatingIncluded;

    @Column
    private String nearToMetro;

    @Column
    private Integer distanceToPublicTransport;

    @Column
    private String surrounding;

    @Column
    private String amenity;

    @Column
    private Boolean elevator;

    @Column
    private Double locationLat;

    @Column
    private Double locationLng;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column
    private String title;

    @Column
    private Integer mainImage;
//    private FileDescriptor mainImageObject;

    @Column
    private String description;

    @Column
    private Boolean enabled;

    @Column
    private Integer userId;

    @Column
    private Integer flatmatesNumber;

    @Column
    private Boolean smokingAllowed;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "property_x_file_descriptor", schema = "cms_hun",
            joinColumns = { @JoinColumn(name = "property_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "file_descriptor_id", referencedColumnName = "id") }
    )
    private Set<FileDescriptor> images = new HashSet<>();

    private String currencyCaption;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String additionalInfo;

    @Column
    private Integer bedsNumber;

    @Column
    private Integer roommatesNumber;

    @Transient
    @Setter(AccessLevel.NONE)
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> additionalInfoMap = new HashMap<>();

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

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String googleMapImgUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Deprecated
    public String getGoogleMapImgUrl() {
        if (this.locationLat != null && this.locationLng != null) {
            GoogleMapParam googleMapParam = new GoogleMapParam();

            LocationParameter locationParameter = new LocationParameter();
            locationParameter.setCenter(this.locationLat + "," + this.locationLng);
            locationParameter.setZoom(16);

            MapParameter mapParameter = new MapParameter();
            mapParameter.setSize("250x250");

            FeatureParameter featureParameter = new FeatureParameter();
            featureParameter.setMarkers("color:blue|" + this.locationLat + "," + this.locationLng);

            googleMapParam.setLocationParameter(locationParameter);
            googleMapParam.setMapParameter(mapParameter);
            googleMapParam.setFeatureParameter(featureParameter);

            return GoogleMapUtil.buildStaticImgUrl(googleMapParam);
        }

        return "";
    }

    public void setAdditionalInfoMap(Map<String, Object> additionalInfoMap) {
        this.additionalInfoMap = additionalInfoMap;
        try {
            this.serializeAdditionalInfoAttributes();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void serializeAdditionalInfoAttributes() throws JsonProcessingException {
        this.additionalInfo = objectMapper.writeValueAsString(additionalInfoMap);
    }

    public void deserializeAdditionalInfoAttributes() throws IOException {
        this.additionalInfoMap = objectMapper.readValue(additionalInfo, HashMap.class);
    }
}