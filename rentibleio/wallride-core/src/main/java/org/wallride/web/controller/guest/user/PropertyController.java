package org.wallride.web.controller.guest.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wallride.domain.*;
import org.wallride.service.AdService;
import org.wallride.service.PropertyService;
import org.wallride.service.UserService;
import org.wallride.support.AuthorizedUser;
import org.wallride.support.CommonCodeStoreItem;
import org.wallride.util.ObjectUtil;
import org.wallride.util.PagingUtil;
import org.wallride.web.controller.guest.flatmate.FlatmatePreferencesForm;
import org.wallride.web.controller.guest.property.*;
import org.wallride.web.support.Pagination;
import org.wallride.web.support.SysKeys;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
public class PropertyController {

	private static final String FORM_MODEL_KEY = "form";
	private static final String GOOGLE_API_KEY = "googleApiKey";
	private static final String CENTER_LAT_LNG = "CenterLatLng";
	private static final String GOOGLE_MAP_PROPERTIES = "googleMapProperties";
	private static final String LOCATION_SIZE = "locationsSize";
	private static final String PROPERTIES = "properties";
	private static final String ROOM_ADS = "roomAds";
	private static final String TOTAL_PAGES = "totalPages";
	private static final String PAGINATION = "pagination";
	private static final String CURRENT_PAGE = "currentPage";
	private static final String CITIES = "cities";
	private static final String TERM_OF_LEASE = "termOfLease";
	private static final String CURRENCIES = "currencies";
	private static final String PAGE_NUMBERS = "pageNumbers";
	private static final String OCCUPATIONS = "occupations";
	private static final String ACTIVE_CITY = "activeCity";
	private static final String ROOMS_MENU_ACTIVE = "roomsMenuActive";

	@Value("${google.map.static.api-key}")
	private String googleApiKey;

	@Inject
	private PropertyService propertyService;
	@Inject
	private AdService adService;
	@Inject
	private UserService userService;

	@RequestMapping("/property-list")
	public String init(
			BlogLanguage blogLanguage,
			Model model,
			HttpServletRequest servletRequest,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size,
			@RequestParam("city") Optional<Integer> city,
			@RequestParam(value = "termOfLease") Optional<List<Integer>> termOfLeases,
			@RequestParam(value = "occupations") Optional<List<Integer>> occupations,
			@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> from,
			@RequestParam("currency") Optional<Integer> currency,
			@RequestParam("minPrice") Optional<Integer> minPrice,
			@RequestParam("maxPrice") Optional<Integer> maxPrice,
			@RequestParam("propertySizeMin") Optional<Integer> propertySizeMin,
			@RequestParam("propertySizeMax") Optional<Integer> propertySizeMax,
			@RequestParam("bedroomsMin") Optional<Integer> bedroomsMin,
			@RequestParam("bedroomsMax") Optional<Integer> bedroomsMax,
			@Valid @ModelAttribute("form") PropertySearchForm propertySearchForm) {
		PropertySearchForm form;
		CommonCodeStoreItem.CommonCodeStoreItemBuilder builder = new CommonCodeStoreItem.CommonCodeStoreItemBuilder();
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(6);

		if (ObjectUtil.checkAllFieldNullOrEmpty(propertySearchForm)) {
			form = new PropertySearchForm();
			form.setCity(city.orElse(700));
			form.setTermOfLease(termOfLeases.orElse(null));
			form.setOccupations(occupations.orElse(null));
			form.setFrom(from.orElse(null));
			form.setCurrency(currency.orElse(null));
			form.setMinPrice(minPrice.orElse(null));
			form.setMaxPrice(maxPrice.orElse(null));
			form.setPropertySizeMin(propertySizeMin.orElse(null));
			form.setPropertySizeMax(propertySizeMax.orElse(null));
			form.setBedroomsMin(bedroomsMin.orElse(null));
			form.setBedroomsMax(bedroomsMax.orElse(null));
			form.setLanguage(blogLanguage.getLanguage());
		} else {
			form = propertySearchForm;
			form.setCity(city.orElse(700));
			form.setTermOfLease(termOfLeases.orElse(null));
			form.setOccupations(occupations.orElse(null));
			form.setFrom(from.orElse(null));
			form.setCurrency(currency.orElse(null));
			form.setMinPrice(minPrice.orElse(null));
			form.setMaxPrice(maxPrice.orElse(null));
			form.setPropertySizeMin(propertySizeMin.orElse(null));
			form.setPropertySizeMax(propertySizeMax.orElse(null));
			form.setBedroomsMin(bedroomsMin.orElse(null));
			form.setBedroomsMax(bedroomsMax.orElse(null));
			form.setLanguage(blogLanguage.getLanguage());
		}

		form.setTermOfLease(termOfLeases.orElse(Collections.emptyList()));
		form.setOccupations(occupations.orElse(Collections.emptyList()));
		if (form.getTermOfLease().size() != 0) {
			model = propertyService.setSelectedRentPeriods(form.getTermOfLease(), model, blogLanguage);
		}
		if (form.getOccupations().size() != 0) {
			model = propertyService.setSelectedOccupations(form.getOccupations(), model, blogLanguage);
		}
		Page<RoomAd> roomAds = propertyService.findAllWithPagination(form.toPropertySearchRequest(), PageRequest.of(currentPage - 1, pageSize));
		List<Integer> pageNumbers = PagingUtil.getPageNumbers(roomAds.getTotalPages(), currentPage);
		List<Property> properties = propertySearchForm.build(roomAds, blogLanguage);
		Map<String, Object> centerLatLng = propertyService.getCenterLatLng(form.getCity());
		List<Map> googleMapProperties = propertySearchForm.getGoogleMapProperties(roomAds);
		builder.withCsiTermOfLease(blogLanguage)
				.withCsiCurrencies(blogLanguage)
				.withCsiOccupations(blogLanguage)
				.build();

		model.addAttribute(FORM_MODEL_KEY, form);
		model.addAttribute(GOOGLE_API_KEY, googleApiKey);
		model.addAttribute(CENTER_LAT_LNG, centerLatLng);
		model.addAttribute(GOOGLE_MAP_PROPERTIES, googleMapProperties);
		model.addAttribute(LOCATION_SIZE, googleMapProperties.size());
		model.addAttribute(PROPERTIES, properties);
		model.addAttribute(ROOM_ADS, roomAds);
		model.addAttribute(TOTAL_PAGES, roomAds.getTotalPages());
		model.addAttribute(PAGINATION, new Pagination<>(roomAds, servletRequest));
		model.addAttribute(CURRENT_PAGE, currentPage);
		model.addAttribute(CITIES, builder.getCsiSchema());
		model.addAttribute(TERM_OF_LEASE, builder.getCsiTermOfLease());
		model.addAttribute(CURRENCIES, builder.getCsiCurrencies());
		model.addAttribute(PAGE_NUMBERS, pageNumbers);
		model.addAttribute(OCCUPATIONS, builder.getCsiOccupations());
		model.addAttribute(ACTIVE_CITY, builder.getOneCity(form.getCity()));
		model.addAttribute(ROOMS_MENU_ACTIVE, true);
		return "user/property-list";
	}

	@RequestMapping(value = "/post-room-ad/step-2", method = RequestMethod.POST)
	public String savePropertyAdStep1(@Validated @ModelAttribute("form") AdStepOneForm form,
								 BindingResult errors,
								 Model model,
								 RedirectAttributes redirectAttributes,
								 Principal principal,
								 BlogLanguage blogLanguage) {
		redirectAttributes.addFlashAttribute("adStepOneForm", form);
		redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "adStepOneForm", errors);
		if (errors.hasErrors()) {
			return "redirect:/room-ad?error";
		}

		CommonCodeStoreItem.CommonCodeStoreItemBuilder builder = new CommonCodeStoreItem.CommonCodeStoreItemBuilder();
		AuthorizedUser authorizedUser = new AuthorizedUser((User) ((Authentication) principal).getPrincipal());
		UserDetail userDetail = authorizedUser.getUserDetail();
		Property savedProperty = adService.saveRoomAd(form.toPropertyRequest(), form.getPropertyId());

		AdStepTwoForm adStepTwoForm = new AdStepTwoForm();

		adStepTwoForm.setPropertyId(savedProperty.getId());
		adStepTwoForm.setStep(form.getStep() + 1);

		builder.withCsiTermOfLease(blogLanguage)
				.withCsiCurrencies(blogLanguage)
				.withCsiFloors(blogLanguage)
				.withCsiBathrooms(blogLanguage)
				.withCsiPublicTransportsHU(blogLanguage)
				.withCsiSurroundings(blogLanguage)
				.build();

		PropertyForm propertyForm = new PropertyForm();
		propertyForm.setProfilePicture(userDetail, model);

		model.addAttribute("adStepTwoForm", adStepTwoForm);
		model.addAttribute("propertyId", savedProperty.getId());
		model.addAttribute("step", adStepTwoForm.getStep());
		model.addAttribute("termOfLease", builder.getCsiTermOfLease());
		model.addAttribute("currencies", builder.getCsiCurrencies());
		model.addAttribute("floors", builder.getCsiFloors());
		model.addAttribute("bathrooms", builder.getCsiBathrooms());
		model.addAttribute("publicTransportHU", builder.getCsiPublicTransportsHU());
		model.addAttribute("surroundings", builder.getCsiSurroundings());
		model.addAttribute("postAdMenuActive", true);

		return "user/room";
	}

	@RequestMapping(value = "/post-room-ad/step-3", method = RequestMethod.POST)
	public String savePropertyAdStep2(@Validated @ModelAttribute("form") AdStepTwoForm form,
									  BindingResult errors,
									  Model model,
									  RedirectAttributes redirectAttributes,
									  Principal principal,
									  BlogLanguage blogLanguage) {
		redirectAttributes.addFlashAttribute("adStepTwoForm", form);
		redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "adStepTwoForm", errors);
		if (errors.hasErrors()) {
			return "redirect:/post-room-ad/step-3?error";
		}

		CommonCodeStoreItem.CommonCodeStoreItemBuilder builder = new CommonCodeStoreItem.CommonCodeStoreItemBuilder();
		AuthorizedUser authorizedUser = new AuthorizedUser((User) ((Authentication) principal).getPrincipal());
		UserDetail userDetail = authorizedUser.getUserDetail();
		Property savedProperty = adService.saveRoomAd(form.toPropertyRequest(), form.getPropertyId());

		builder.withCsiGenders(blogLanguage)
				.withCsiOccupations(blogLanguage)
				.withCsiLanguages(blogLanguage)
				.withCsiUniversitiesHU(blogLanguage)
				.build();

		FlatmatePreferencesForm flatmatePreferencesForm = new FlatmatePreferencesForm();
		flatmatePreferencesForm.setPropertyId(savedProperty.getId());
		flatmatePreferencesForm.setStep(form.getStep() + 1);

		PropertyForm propertyForm = new PropertyForm();
		propertyForm.setProfilePicture(userDetail, model);

		model.addAttribute("flatmatePreferencesForm", flatmatePreferencesForm);
		model.addAttribute("propertyId", savedProperty.getId());
		model.addAttribute("step", flatmatePreferencesForm.getStep());
		model.addAttribute("genders", builder.getCsiGenders());
		model.addAttribute("occupations", builder.getCsiOccupations());
		model.addAttribute("languages", builder.getCsiLanguages());
		model.addAttribute("universities", builder.getCsiUniversitiesHU());
		model.addAttribute("postAdMenuActive", true);


		return "user/room";
	}

	@RequestMapping(value = "/post-room-ad/step-5", method = RequestMethod.POST)
	public String savePropertyAdStep4(@Validated @ModelAttribute("form") AdStepFourForm form,
									  BindingResult errors,
									  Model model,
									  RedirectAttributes redirectAttributes,
									  Principal principal,
									  BlogLanguage blogLanguage) {
		redirectAttributes.addFlashAttribute("adStepFourForm", form);
		redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "adStepFourForm", errors);
		if (errors.hasErrors()) {
			return "redirect:/post-room-ad/step-5?error";
		}

		AuthorizedUser authorizedUser = new AuthorizedUser((User) ((Authentication) principal).getPrincipal());
		UserDetail userDetail = authorizedUser.getUserDetail();
		Property savedProperty = adService.saveRoomAd(form.toPropertyRequest(), form.getPropertyId());


		PropertyForm propertyForm = new PropertyForm();
		propertyForm.setStep(form.getStep() + 1);
		propertyForm.setPropertyId(form.getPropertyId());
		propertyForm.setProfilePicture(userDetail, model);

		model.addAttribute("form", propertyForm);
		model.addAttribute("propertyId", savedProperty.getId());
		model.addAttribute("step", propertyForm.getStep());
		model.addAttribute("postAdMenuActive", true);


		return "user/room";
	}

	@RequestMapping(value = "/post-room-ad/images", method = RequestMethod.POST)
	public String uploadImagesForAd(@RequestParam("file") MultipartFile multipartFile,
									@Validated @ModelAttribute("form") AdStepFourForm form,
									Principal principal,
									Model model) {
		AuthorizedUser authorizedUser = new AuthorizedUser((User) ((Authentication) principal).getPrincipal());
		UserDetail userDetail = authorizedUser.getUserDetail();
		Property savedProperty = adService.uploadImagesForAd(multipartFile, authorizedUser, form.getPropertyId());

		AdStepFourForm adStepFourForm = new AdStepFourForm();
		adStepFourForm.setStep(form.getStep());
		adStepFourForm.setPropertyId(form.getPropertyId());
		List<String> imageUrls = new ArrayList<>();

		savedProperty.getImages().forEach(image ->
				imageUrls.add(image.getFilePath())
		);
		PropertyForm propertyForm = new PropertyForm();
		propertyForm.setProfilePicture(userDetail, model);
		model.addAttribute("form", adStepFourForm);
		model.addAttribute("uploadedPictures", true);
		model.addAttribute("imageUrls", imageUrls);
		model.addAttribute("step", form.getStep());
		model.addAttribute("postAdMenuActive", true);
		return "user/room";
	}

	@RequestMapping(value = "/publish-room-ad", method = RequestMethod.POST)
	public String publishAd(@Validated @ModelAttribute("form") PropertyForm form) {
		adService.publishAd(form.getPropertyId());
		return "redirect:/property-list";
	}

	@RequestMapping(value = "/room-ad", method = RequestMethod.GET, params = "error")
	public String validationError(BlogLanguage blogLanguage, Model model, Principal principal) {
		CommonCodeStoreItem.CommonCodeStoreItemBuilder builder = new CommonCodeStoreItem.CommonCodeStoreItemBuilder();
		AuthorizedUser authorizedUser = new AuthorizedUser((User) ((Authentication) principal).getPrincipal());
		UserDetail userDetail = authorizedUser.getUserDetail();
		Map<String, Object> latLngHun = propertyService.getCenterLatLng(SysKeys.CSI_ID_SCHEMA_CMS_HUN);
		Map<String, Object> latLngGbr = propertyService.getCenterLatLng(SysKeys.CSI_ID_SCHEMA_CMS_GBR);
		Map<String, Object> latLngNl = propertyService.getCenterLatLng(SysKeys.CSI_ID_SCHEMA_CMS_NL);

		builder.withCsiDistricts(blogLanguage)
				.build();

		PropertyForm propertyForm = new PropertyForm();
		propertyForm.setProfilePicture(userDetail, model);
		model.addAttribute("districts", builder.getCsiDistricts());
		model.addAttribute("cities", builder.getCsiSchema());
		model.addAttribute("googleApiKey", googleApiKey);
		model.addAttribute("latLngHun", latLngHun);
		model.addAttribute("latLngGbr", latLngGbr);
		model.addAttribute("latLngNl", latLngNl);
		model.addAttribute("step", 1);
		model.addAttribute("postAdMenuActive", true);
		return "user/room";
	}

	@RequestMapping(value = "/post-room-ad/step-3", method = RequestMethod.GET, params = "error")
	public String step2ValidationError(BlogLanguage blogLanguage, Model model, Principal principal) {
		CommonCodeStoreItem.CommonCodeStoreItemBuilder builder = new CommonCodeStoreItem.CommonCodeStoreItemBuilder();
		AuthorizedUser authorizedUser = new AuthorizedUser((User) ((Authentication) principal).getPrincipal());
		UserDetail userDetail = authorizedUser.getUserDetail();

		builder.withCsiTermOfLease(blogLanguage)
				.withCsiCurrencies(blogLanguage)
				.withCsiFloors(blogLanguage)
				.withCsiBathrooms(blogLanguage)
				.withCsiPublicTransportsHU(blogLanguage)
				.withCsiSurroundings(blogLanguage)
				.build();

		PropertyForm propertyForm = new PropertyForm();
		propertyForm.setProfilePicture(userDetail, model);

		model.addAttribute("termOfLease", builder.getCsiTermOfLease());
		model.addAttribute("currencies", builder.getCsiCurrencies());
		model.addAttribute("floors", builder.getCsiFloors());
		model.addAttribute("bathrooms", builder.getCsiBathrooms());
		model.addAttribute("publicTransportHU", builder.getCsiPublicTransportsHU());
		model.addAttribute("surroundings", builder.getCsiSurroundings());
		model.addAttribute("step", 2);
		model.addAttribute("postAdMenuActive", true);
		return "user/room";
	}

	@RequestMapping(value = "/post-room-ad/step-5", method = RequestMethod.GET, params = "error")
	public String step4ValidationError(Model model, Principal principal) {
		AuthorizedUser authorizedUser = new AuthorizedUser((User) ((Authentication) principal).getPrincipal());
		UserDetail userDetail = authorizedUser.getUserDetail();

		PropertyForm propertyForm = new PropertyForm();
		propertyForm.setProfilePicture(userDetail, model);

		model.addAttribute("step", 4);
		model.addAttribute("postAdMenuActive", true);
		return "user/room";
	}

}
