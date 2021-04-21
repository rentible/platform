package org.wallride.web.controller.guest.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.wallride.model.PropertyRequest;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AdStepTwoForm implements Serializable {
    @NotNull
    private String title;
    @NotNull
    private BigDecimal rentPrice;
    @NotNull
    private Integer rentPriceCurrency;
    private BigDecimal deposit;
    private Integer depositCurrency;
    private String roomSize;
    private Integer apartmentSize;
    @NotNull
    private Integer flatmatesNumber;
    private List<Integer> bathrooms;
    private Integer numberOfToilets;
    @NotNull
    private List<Integer> termOfLease;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date availableFrom;
    @NotNull
    private Integer floor;
    private Boolean hasElevator;
    @NotNull
    private List<Integer> publicTransports;
    private Integer distanceToPublicTransport;
    @NotNull
    private List<Integer> surroundings;
    @NotNull
    private Boolean isSmokingAllowed;
    private Integer step;
    private Long propertyId;
    private Integer roommatesNumber;
    private Integer bedsNumber;

    public PropertyRequest toPropertyRequest() {
        PropertyRequest request = new PropertyRequest();
        request.setTitle(getTitle());
        request.setRoomSize(getRoomSize());
        request.setTermOfLease(getTermOfLease());
        request.setApartmentSize(getApartmentSize());
        request.setFlatmatesNumber(getFlatmatesNumber());
        request.setFloor(getFloor());
        request.setRentPrice(getRentPrice());
        request.setRentPriceCurrency(getRentPriceCurrency());
        request.setAvailableFrom(getAvailableFrom());
        request.setBathrooms(getBathrooms());
        request.setNumberOfToilets(getNumberOfToilets());
        request.setPublicTransports(getPublicTransports());
        request.setDistanceToPublicTransport(getDistanceToPublicTransport());
        request.setIsSmokingAllowed(getIsSmokingAllowed());
        request.setHasElevator(getHasElevator());
        request.setSurroundings(getSurroundings());
        request.setDeposit(getDeposit());
        request.setDepositCurrency(getDepositCurrency());
        request.setStep(getStep());
        request.setPropertyId(getPropertyId());
        request.setRoommatesNumber(getRoommatesNumber());
        request.setBedsNumber(getBedsNumber());
        return request;
    }
}
