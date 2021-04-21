package org.wallride.web.controller.guest.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.wallride.domain.*;
import org.wallride.service.AdService;
import org.wallride.service.PropertyService;
import org.wallride.service.UserService;
import org.wallride.support.AuthorizedUser;
import org.wallride.support.CommonCodeStoreItem;
import org.wallride.web.controller.guest.flatmate.FlatmateAdForm;
import org.wallride.web.controller.guest.flatmate.FlatmateForm;
import org.wallride.web.controller.guest.flatmate.FlatmatePreferencesForm;
import org.wallride.web.controller.guest.flatmate.FlatmateSearchForm;
import org.wallride.web.controller.guest.property.AdStepFourForm;
import org.wallride.web.controller.guest.property.AdStepOneForm;
import org.wallride.web.controller.guest.property.PropertyForm;
import org.wallride.web.controller.guest.property.PropertySearchForm;
import org.wallride.web.support.SysKeys;

import javax.inject.Inject;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class AdController {

	@Value("${google.map.static.api-key}")
	private String googleApiKey;

	@Inject
	private PropertyService propertyService;

	@Inject
	private AdService adService;

	@Inject
	private UserService userService;

	@RequestMapping(value = "/room-ad")
	public String roomAd(Model model, BlogLanguage blogLanguage, Principal principal) {
		AdStepOneForm adStepOneForm = new AdStepOneForm();
		CommonCodeStoreItem.CommonCodeStoreItemBuilder builder = new CommonCodeStoreItem.CommonCodeStoreItemBuilder();
		AuthorizedUser authorizedUser = new AuthorizedUser((User) ((Authentication) principal).getPrincipal());
		UserDetail userDetail = authorizedUser.getUserDetail();
		Map<String, Object> latLngHun = propertyService.getCenterLatLng(SysKeys.CSI_ID_SCHEMA_CMS_HUN);
		Map<String, Object> latLngGbr = propertyService.getCenterLatLng(SysKeys.CSI_ID_SCHEMA_CMS_GBR);
		Map<String, Object> latLngNl = propertyService.getCenterLatLng(SysKeys.CSI_ID_SCHEMA_CMS_NL);

		builder.withCsiDistricts(blogLanguage)
				.build();

		adStepOneForm.setStep(1);
		PropertyForm propertyForm = new PropertyForm();
		propertyForm.setProfilePicture(userDetail, model);
		model.addAttribute("currencies", builder.getCsiCurrencies());
		model.addAttribute("termOfLease", builder.getCsiTermOfLease());
		model.addAttribute("districts", builder.getCsiDistricts());
		model.addAttribute("cities", builder.getCsiSchema());
		model.addAttribute("adStepOneForm", adStepOneForm);
		model.addAttribute("latLngHun", latLngHun);
		model.addAttribute("latLngGbr", latLngGbr);
		model.addAttribute("latLngNl", latLngNl);
		model.addAttribute("step", adStepOneForm.getStep());
		model.addAttribute("googleApiKey", googleApiKey);
		model.addAttribute("postAdMenuActive", true);

		return "user/room";
	}

	@RequestMapping(value = "/flatmate-ad")
	public String flatmateAd(Model model, BlogLanguage blogLanguage, Principal principal) {
		FlatmateForm form = new FlatmateForm();
		CommonCodeStoreItem.CommonCodeStoreItemBuilder builder = new CommonCodeStoreItem.CommonCodeStoreItemBuilder();
		AuthorizedUser authorizedUser = new AuthorizedUser((User) ((Authentication) principal).getPrincipal());
		UserDetail userDetail = authorizedUser.getUserDetail();

		builder.withCsiCurrencies(blogLanguage)
				.withCsiTermOfLease(blogLanguage)
				.withCsiLanguages(blogLanguage)
				.withCsiDistricts(blogLanguage)
				.build();

		form.setProfilePicture(userDetail, model);
		model.addAttribute("termOfLease", builder.getCsiTermOfLease());
		model.addAttribute("currencies", builder.getCsiCurrencies());
		model.addAttribute("languages", builder.getCsiLanguages());
		model.addAttribute("districts", builder.getCsiDistricts());
		model.addAttribute("flatmateForm", form);
		model.addAttribute("postAdMenuActive", true);

		return "user/flatmate";
	}

	@RequestMapping(value = "/settings/profile/my-room-ads")
	public String getMyRoomAds(Model model,
			BlogLanguage blogLanguage,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {
		PropertySearchForm form = new PropertySearchForm();
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(4);
		Page<RoomAd> myRoomAds = propertyService.findMyAdsWithPagination(form.toPropertySearchRequest(), PageRequest.of(currentPage - 1, pageSize));
		List<Property> properties = form.build(myRoomAds, blogLanguage);
		model.addAttribute("properties", properties);
		model.addAttribute("form", form);
		model.addAttribute("myRoomAds", myRoomAds);
		model.addAttribute("profileMenuActive", true);

		return "user/my-room-ads";
	}

	@RequestMapping(value = "/settings/profile/my-flatmate-ads")
	public String getMyFlatmateAds(Model model,
			BlogLanguage blogLanguage,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {
		FlatmateSearchForm form = new FlatmateSearchForm();
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(4);
		Page<FlatmateAd> myFlatmateAds = adService.findMyFlatmateAdsWithPagination(form.toFlatmateSearchRequest(), PageRequest.of(currentPage - 1, pageSize));
		List<FlatmateAdForm> ads = adService.buildFlatmateAds(myFlatmateAds, blogLanguage);

		model.addAttribute("form", form);
		model.addAttribute("ads", ads);
		model.addAttribute("profileMenuActive", true);

		return "user/my-flatmate-ads";
	}

	@RequestMapping(value = "/post-room-ad/step-4", method = RequestMethod.POST)
	public String saveFlatmatePreferences(@Validated @ModelAttribute("flatmatePreferencesForm") FlatmatePreferencesForm flatmatePreferencesForm,
										  Model model,
										  Principal principal,
										  BlogLanguage blogLanguage) {
		AuthorizedUser authorizedUser = new AuthorizedUser((User) ((Authentication) principal).getPrincipal());
		UserDetail userDetail = authorizedUser.getUserDetail();
		AdStepFourForm adStepFourForm = new AdStepFourForm();

		Long propertyId = adService.saveFlatmatePreferences(flatmatePreferencesForm.toFlatmatePreferencesRequest(), flatmatePreferencesForm.getPropertyId());
		adStepFourForm.setPropertyId(propertyId);
		adStepFourForm.setStep(flatmatePreferencesForm.getStep() + 1);

		PropertyForm propertyForm = new PropertyForm();
		propertyForm.setProfilePicture(userDetail, model);
		model.addAttribute("form", adStepFourForm);
		model.addAttribute("step", adStepFourForm.getStep());
		model.addAttribute("postAdMenuActive", true);

		return "user/room";
	}

}
