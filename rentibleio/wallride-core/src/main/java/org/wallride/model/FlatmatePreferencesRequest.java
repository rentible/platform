package org.wallride.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FlatmatePreferencesRequest {

    private Integer ageFrom;
    private Integer ageTo;
    private Integer gender;
    private Integer occupation;
    private Integer university;
    private List<Integer> languages;
    private Long propertyId;

}
