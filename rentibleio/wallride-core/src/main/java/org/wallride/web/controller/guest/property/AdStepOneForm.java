package org.wallride.web.controller.guest.property;

import lombok.Getter;
import lombok.Setter;
import org.wallride.model.PropertyRequest;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class AdStepOneForm implements Serializable {
    @NotNull
    private String street;
    private String streetNumber;
    private String zipCode;
    @NotNull
    private Integer city;
    @NotNull
    private Integer district;
    private String cityCaption;
    private Integer step;
    private Long propertyId;

    public PropertyRequest toPropertyRequest() {
        PropertyRequest request = new PropertyRequest();
        request.setStreet(getStreet());
        request.setDistrict(getDistrict());
        request.setStreetNumber(getStreetNumber());
        request.setZipCode(getZipCode());
        request.setStep(getStep());
        request.setCityCaption(getCityCaption());
        return request;
    }
}
