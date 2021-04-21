package org.wallride.web.controller.guest.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.wallride.domain.BlogLanguage;
import org.wallride.domain.Property;
import org.wallride.domain.RoomAd;
import org.wallride.domain.UserDetail;
import org.wallride.model.PropertySearchRequest;
import org.wallride.service.AdService;
import org.wallride.service.PropertyService;
import org.wallride.service.UserService;
import org.wallride.web.controller.guest.property.PropertySearchForm;

import javax.inject.Inject;
import java.util.List;

@Controller
public class PropertyViewController {

	private static final String LANDLORD = "landlord";
	private static final String PROPERTY = "property";
	private static final String GOOGLE_API_KEY = "googleApiKey";
	private static final String PROPERTIES = "properties";
	private static final String ROOMS_MENU_ACTIVE = "roomsMenuActive";

	@Inject
	PropertyService propertyService;
	@Inject
	UserService userService;
	@Inject
	AdService adService;

	@Value("${google.map.static.api-key}")
	private String googleApiKey;

	@RequestMapping(value = "/property-view", method = RequestMethod.GET)
	public String init(Model model, @RequestParam("id") Long id, BlogLanguage blogLanguage) {
		Property property = propertyService.findOne(id);
		Page<RoomAd> moreAds = propertyService.findAllWithPagination(new PropertySearchRequest(), PageRequest.of(0, 4));
		UserDetail landlord = userService.getUserById(property.getCreatedBy()).getUserDetail();
        PropertySearchForm propertySearchForm = new PropertySearchForm();
        List<Property> properties = propertySearchForm.build(moreAds, blogLanguage);
		adService.buildPropertyAdView(model, landlord, property, blogLanguage);

		model.addAttribute(LANDLORD, landlord);
		model.addAttribute(PROPERTY, property);
		model.addAttribute(GOOGLE_API_KEY, googleApiKey);
        model.addAttribute(PROPERTIES, properties);
		model.addAttribute(ROOMS_MENU_ACTIVE, true);
		return "user/property-view";
	}

}
