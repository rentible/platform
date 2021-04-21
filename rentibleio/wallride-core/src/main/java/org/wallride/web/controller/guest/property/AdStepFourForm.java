package org.wallride.web.controller.guest.property;

import lombok.Getter;
import lombok.Setter;
import org.wallride.model.PropertyRequest;

import java.io.Serializable;

@Getter
@Setter
public class AdStepFourForm implements Serializable {

    private String description;
    private Integer step;
    private Long propertyId;

    public PropertyRequest toPropertyRequest() {
        PropertyRequest request = new PropertyRequest();
        request.setDescription(getDescription());
        request.setStep(getStep());
        request.setPropertyId(getPropertyId());
        return request;
    }
}
