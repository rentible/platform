package org.wallride.web.controller.guest.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.wallride.domain.UserDetail;
import org.wallride.model.PropertyRequest;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PropertyForm implements Serializable {

	private String street;
	private String streetNumber;
    private String zipCode;
    private Integer city;
    private Integer district;
	private String title;
	private String description;
	private String roomSize;
	private List<Integer> termOfLease;
	private Integer apartmentSize;
	private BigDecimal rentPrice;
	private Integer rentPriceCurrency;
	private BigDecimal deposit;
	private Integer depositCurrency;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date availableFrom;
	private Integer flatmatesNumber;
	private Integer floor;
	private List<Integer> bathrooms;
	private Integer numberOfToilets;
	private List<Integer> publicTransports;
	private Integer distanceToPublicTransport;
	private Boolean isSmokingAllowed;
	private Boolean hasElevator;
	private Integer step;
	private Long propertyId;
	private Boolean shower;
	private Boolean guestBathroom;
	private Boolean bathtub;
	private Boolean ensuite;
	private List<Integer> surroundings;
	private Integer roommatesNumber;
	private Integer bedsNumber;

	public PropertyRequest toPropertyRequest() {
		PropertyRequest request = new PropertyRequest();
		request.setStreet(getStreet());
		request.setTitle(getTitle());
		request.setDescription(getDescription());
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
		request.setDistrict(getDistrict());
		request.setStreetNumber(getStreetNumber());
		request.setStep(getStep());
		request.setZipCode(getZipCode());
		request.setPropertyId(getPropertyId());
		request.setSurroundings(getSurroundings());
		request.setDeposit(getDeposit());
		request.setDepositCurrency(getDepositCurrency());
		request.setRoommatesNumber(getRoommatesNumber());
		request.setBedsNumber(getBedsNumber());
		return request;
	}

    public Model setProfilePicture(UserDetail userDetail, Model model) {
        if (userDetail.getProfileImage() != null) {
            model.addAttribute("profilePicturePath", "../media/" + userDetail.getProfileImage().getFilePath());
        } else {
            model.addAttribute("profilePicturePath", "../media/default/default-profile.png");
        }
        return model;
    }

}
