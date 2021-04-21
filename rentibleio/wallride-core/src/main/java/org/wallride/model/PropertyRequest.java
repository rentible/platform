package org.wallride.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PropertyRequest implements Serializable {
	private String street;
	private String streetNumber;
	private String title;
	private String description;
	private String roomSize;
	private List<Integer> termOfLease;
	private Integer apartmentSize;
	private Integer flatmatesNumber;
	private Integer floor;
	private BigDecimal rentPrice;
	private Integer rentPriceCurrency;
    private BigDecimal deposit;
    private Integer depositCurrency;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date availableFrom;
	private List<Integer> bathrooms;
	private Integer numberOfToilets;
	private List<Integer> publicTransports;
	private Integer distanceToPublicTransport;
	private Boolean isSmokingAllowed;
	private Boolean hasElevator;
	private Integer district;
	private Integer step;
	private String zipCode;
	private Long propertyId;
	private List<Integer> surroundings;
    private String cityCaption;
	private Integer roommatesNumber;
	private Integer bedsNumber;

}
