package hu.lanoga.flatshares.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class Property {
    private Integer id;
    private Integer adId;//TODO
    private Integer rentalType;
    private String crossStreet1;
    private String crossStreet2;
    private Integer propertyType;
    private String referenceId;
    private BigDecimal rentPrice;
    private Integer rentPriceCurrency;
    private String deposit;
    private Integer depositCurrency;
    private Integer apartmentSize;
    private String roomSize;
    private Integer bedroom;
    private String room;
    private String bathroom;
    private Integer toilet;
    private Boolean furnished;
    private String termoflease;
    private Timestamp availableFrom;
    private Integer apartmentCondition;
    private Integer view;
    private Integer floor;
    private Integer heating;
    private Integer buildingCondition;
    private Integer yearBuilt;
    private BigDecimal commonCost;
    private Boolean heatingIncluded;
    private String nearToMetro;
    private Integer distanceToPublicTransport;
    private String surrounding;
    private String amenity;
    private Boolean elevator;
    private Float locationLat;
    private Float locationLng;
    private Integer addressId;

//    @NotNull
//    private Address address; TODO
    private String title;
    private Integer mainImage;
    private FileDescriptor mainImageObject;
    private String description;
    private Boolean enabled;

    @NotNull(message = "Can not create a property without user_id!")
    private Integer userId;

    @NotNull
    private Timestamp createdOn;

    @NotNull
    private Integer createdBy;

    @NotNull
    private Timestamp modifiedOn;

    @NotNull
    private Integer modifiedBy;
    private List<FileDescriptor> images;
}
