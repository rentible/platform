package org.wallride.web.controller.guest.flatmate;

import lombok.Getter;
import lombok.Setter;
import org.wallride.model.FlatmatePreferencesRequest;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class FlatmatePreferencesForm implements Serializable {

    private Integer ageFrom;
    private Integer ageTo;
    private Integer gender;
    private Integer occupation;
    private Integer university;
    private List<Integer> languages;
    private Long propertyId;
    private Integer step;

    public FlatmatePreferencesRequest toFlatmatePreferencesRequest() {
        FlatmatePreferencesRequest flatmatePreferencesRequest = new FlatmatePreferencesRequest();

        flatmatePreferencesRequest.setAgeFrom(getAgeFrom());
        flatmatePreferencesRequest.setAgeTo(getAgeTo());
        flatmatePreferencesRequest.setGender(getGender());
        flatmatePreferencesRequest.setOccupation(getOccupation());
        flatmatePreferencesRequest.setUniversity(getUniversity());
        flatmatePreferencesRequest.setLanguages(getLanguages());
        flatmatePreferencesRequest.setPropertyId(getPropertyId());

        return flatmatePreferencesRequest;
    }
}
