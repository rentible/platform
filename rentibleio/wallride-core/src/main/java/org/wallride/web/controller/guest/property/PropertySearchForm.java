package org.wallride.web.controller.guest.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.wallride.domain.BlogLanguage;
import org.wallride.domain.Property;
import org.wallride.domain.RoomAd;
import org.wallride.model.PropertySearchRequest;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
public class PropertySearchForm implements Serializable {

    public String language;
    public List<Integer> termOfLease;
    public List<Integer> occupations;
    public String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date from;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date to;
    public Integer currency;
    public Integer minPrice;
    public Integer maxPrice;
    public Integer propertySizeMin;
    public Integer propertySizeMax;
    public Integer bedroomsMin;
    public Integer bedroomsMax;
    public Integer city;

    public PropertySearchRequest toPropertySearchRequest() {
        PropertySearchRequest request = new PropertySearchRequest();
        request.setFrom(getFrom());
        request.setTermOfLease(getTermOfLease());
        request.setTo(getTo());
        request.setCurrency(getCurrency());
        request.setMinPrice(getMinPrice());
        request.setMaxPrice(getMaxPrice());
        return request;
    }

    public List<Property> build(Page<RoomAd> roomAds, BlogLanguage blogLanguage) {
        List<Property> properties = new ArrayList<>();
        roomAds.forEach(ad -> {
                    ad.getProperty().setCurrencyCaption(ad.getProperty().getRentPriceCurrency().getCaption(blogLanguage.getLanguage()));
                    properties.add(ad.getProperty());
                }
        );
        return properties;
    }

    public List<Map> getGoogleMapProperties(Page<RoomAd> roomAds) {
        List<Map> googleMapProperties = new ArrayList<>();

        roomAds.forEach(ad -> {
                    Map<String, Object> location = new HashMap<>();
            location.put("title", ad.getProperty().getTitle());
            location.put("currencyCaption", ad.getProperty().getCurrencyCaption());
            location.put("roomSize", ad.getProperty().getRoomSize());
            location.put("address", ad.getProperty().getAddress().getAddress1() + " " + ad.getProperty().getAddress().getAddress2());
            location.put("rentPrice", ad.getProperty().getRentPrice());
            location.put("rentPriceCurrency", ad.getProperty().getCurrencyCaption());
            location.put("size", ad.getProperty().getRoomSize());
            location.put("imgUrl", ad.getProperty().getImages());
                    location.put("lat", ad.getProperty().getLocationLat());
                    location.put("lng", ad.getProperty().getLocationLng());
            googleMapProperties.add(location);
        });

        return googleMapProperties;
    }
}
