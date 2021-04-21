package org.wallride.service;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.wallride.domain.*;
import org.wallride.model.FlatmatePreferencesRequest;
import org.wallride.model.FlatmateRequest;
import org.wallride.model.FlatmateSearchRequest;
import org.wallride.model.PropertyRequest;
import org.wallride.repository.*;
import org.wallride.support.AuthorizedUser;
import org.wallride.support.DateAndTimeUtil;
import org.wallride.util.OpenStreetMapUtils;
import org.wallride.util.SecurityUtil;
import org.wallride.web.controller.guest.flatmate.FlatmateAdForm;
import org.wallride.web.support.SysKeys;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdService {

	@Autowired
	private RoomAdRepository roomAdRepository;
	@Autowired
	private FlatmateAdRepository flatmateAdRepository;
	@Autowired
	private PropertyRepository propertyRepository;
	@Autowired
	private CodeStoreItemRepository codeStoreItemRepository;
	@Inject
	private FileStoreService fileStoreService;
	@Resource
	private FlatmateAdRepositoryCustom flatmateAdRepositoryCustom;
	@Resource
	private AddressRepository addressRepository;
	@Resource
	private FlatmatePreferencesRepository flatmatePreferencesRepository;

	public Property saveRoomAd(PropertyRequest request, Long propertyId) {

		Property savedProperty;
		if (request != null) {
			AuthorizedUser authorizedUser = new AuthorizedUser((User) SecurityUtil.getAuthentication().getPrincipal());

			if (request.getStep() == 1) {
				RoomAd roomAd = new RoomAd();
				roomAd.setProperty(new Property());
				roomAd.getProperty().setAddress(new Address());

				CodeStoreItem district = codeStoreItemRepository.findOneById(request.getDistrict());
				CodeStoreItem roomRentalType = codeStoreItemRepository.findOneById(SysKeys.CSI_ID_ROOM_RENTAL_TYPE);
				Pair<Double, Double> coordinates = OpenStreetMapUtils.getCoordinates(request.getCityCaption() + " " + request.getStreet() + " " + request.getStreetNumber());

				roomAd.getProperty().getAddress().setCity(request.getCityCaption());
				roomAd.getProperty().getAddress().setAddress1(request.getStreet());
				roomAd.getProperty().getAddress().setAddress2(request.getStreetNumber());
				roomAd.getProperty().getAddress().setZip(request.getZipCode());
				roomAd.getProperty().getAddress().setDistrict(district);
				roomAd.getProperty().setRentalType(roomRentalType);
				if (coordinates != null) {
					roomAd.getProperty().setLocationLat(coordinates.getKey());
					roomAd.getProperty().setLocationLng(coordinates.getValue());
				}
				roomAd.getProperty().setEnabled(true);
				roomAd.getProperty().getAddress().setCreatedBy(authorizedUser.getId());
				roomAd.getProperty().getAddress().setCreatedOn(DateAndTimeUtil.now());
				roomAd.getProperty().getAddress().setModifiedBy(authorizedUser.getId());
				roomAd.getProperty().getAddress().setModifiedOn(DateAndTimeUtil.now());
				roomAd.getProperty().setCreatedBy(authorizedUser.getId());
				roomAd.getProperty().setCreatedOn(DateAndTimeUtil.now());
				roomAd.getProperty().setModifiedBy(authorizedUser.getId());
				roomAd.getProperty().setModifiedOn(DateAndTimeUtil.now());
				roomAd.setEnabled(false);
				roomAd.setCreatedBy(authorizedUser.getId());
				roomAd.setCreatedOn(DateAndTimeUtil.now());
				roomAd.setModifiedBy(authorizedUser.getId());
				roomAd.setModifiedOn(DateAndTimeUtil.now());

				addressRepository.saveAndFlush(roomAd.getProperty().getAddress());

				savedProperty = propertyRepository.saveAndFlush(roomAd.getProperty());
				roomAdRepository.saveAndFlush(roomAd);
			} else if (request.getStep() == 2) {
				Property property = propertyRepository.findOneById(propertyId);
				CodeStoreItem rentPriceCurrency = codeStoreItemRepository.findOneById(request.getRentPriceCurrency());
				CodeStoreItem depositCurrency = codeStoreItemRepository.findOneById(request.getDepositCurrency());
				CodeStoreItem floor = codeStoreItemRepository.findOneById(request.getFloor());
				Set<CodeStoreItem> rentPeriods = new HashSet<>();
				List<HashMap> additionalInfoList = new ArrayList<>();

				if (request.getBathrooms() != null) {
					property.getAdditionalInfoMap().put("bathrooms", request.getBathrooms());
					property.setAdditionalInfoMap(property.getAdditionalInfoMap());
				}

				if (request.getPublicTransports() != null) {
					property.getAdditionalInfoMap().put("publicTransports", request.getPublicTransports());
					property.setAdditionalInfoMap(property.getAdditionalInfoMap());
				}

				if (request.getSurroundings() != null) {
					property.getAdditionalInfoMap().put("surroundings", request.getSurroundings());
					property.setAdditionalInfoMap(property.getAdditionalInfoMap());
				}

				if (request.getTermOfLease() != null) {
					request.getTermOfLease().forEach(language ->
							rentPeriods.add(codeStoreItemRepository.findOneById(language))
					);
				}

				property.setTitle(request.getTitle());
				property.setRoomSize(request.getRoomSize());
				property.setTermOfLease(rentPeriods);
				property.setApartmentSize(request.getApartmentSize());
				property.setFlatmatesNumber(request.getFlatmatesNumber());
				property.setFloor(floor);
				property.setRentPrice(request.getRentPrice());
				property.setRentPriceCurrency(rentPriceCurrency);
				property.setDeposit(request.getDeposit());
				property.setDepositCurrency(depositCurrency);
				property.setToilet(request.getNumberOfToilets());
				property.setSmokingAllowed(request.getIsSmokingAllowed());
				property.setElevator(request.getHasElevator());
				property.setRoommatesNumber(request.getRoommatesNumber());
				property.setBedsNumber(request.getBedsNumber());
				if (request.getAvailableFrom() != null) {
					property.setAvailableFrom(new java.sql.Timestamp(request.getAvailableFrom().getTime()));
				}
				property.setDistanceToPublicTransport(request.getDistanceToPublicTransport());
				savedProperty = propertyRepository.saveAndFlush(property);
			} else if (request.getStep() == 4) {
				savedProperty = propertyRepository.findOneById(propertyId);
				savedProperty.setDescription(request.getDescription());
				propertyRepository.saveAndFlush(savedProperty);
			} else if (request.getStep() == 5) {
				savedProperty = propertyRepository.findOneById(propertyId);
				roomAdRepository.publishAd(propertyId);
			} else {
				savedProperty = new Property();
			}

		} else {
			savedProperty = new Property();
		}
		return savedProperty;
	}

	public void saveFlatmateAd(FlatmateRequest request) {

		if (request != null) {
			AuthorizedUser authorizedUser = new AuthorizedUser((User) SecurityUtil.getAuthentication().getPrincipal());
			CodeStoreItem currency = codeStoreItemRepository.findOneById(request.getCurrency());
			Set<CodeStoreItem> languages = new HashSet<>();
			CodeStoreItem rentPeriod = codeStoreItemRepository.findOneById(request.getTermOfLease());

			if (request.getLanguages() != null) {
				request.getLanguages().forEach(language ->
						languages.add(codeStoreItemRepository.findOneById(language))
				);
			}

			FlatmateAd flatmateAd = new FlatmateAd();
			flatmateAd.setUser(authorizedUser);
			flatmateAd.setTitle(request.getTitle());
			flatmateAd.setDescription(request.getDescription());

			flatmateAd.getDistrictsMap().put("districts", request.getDistricts());
			flatmateAd.setDistrictsMap(flatmateAd.getDistrictsMap());

			flatmateAd.setBudget(request.getBudget());
			flatmateAd.setCurrency(currency);
			flatmateAd.setTermOfLease(rentPeriod);
			flatmateAd.setSmokingAllowed(request.getIsSmokingAllowed());
			flatmateAd.setMoveInFrom(new java.sql.Timestamp(request.getMoveInFrom().getTime()));
			flatmateAd.setLanguages(languages);
			flatmateAd.setEnabled(true);
			flatmateAd.setCreatedBy(authorizedUser.getId());
			flatmateAd.setCreatedOn(DateAndTimeUtil.now());
			flatmateAd.setModifiedBy(authorizedUser.getId());
			flatmateAd.setModifiedOn(DateAndTimeUtil.now());
			flatmateAdRepository.saveAndFlush(flatmateAd);
		}

	}

	public Property uploadImagesForAd(MultipartFile multipartFile, User user, Long propertyId) {

		FileDescriptor file = fileStoreService.store(multipartFile, user);
		Property property = propertyRepository.findOneById(propertyId);
		property.getImages().add(file);
		property = propertyRepository.saveAndFlush(property);

		return property;

	}

	public void publishAd(Long propertyId) {
		roomAdRepository.publishAd(propertyId);
	}

	public org.springframework.data.domain.Page<FlatmateAd> findAllFlatmateAdsWithPagination(FlatmateSearchRequest flatmateSearchRequest, Pageable pageable) {

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (flatmateSearchRequest.getTermOfLease() != null) {
			flatmateSearchRequest.getTermOfLease().forEach(id ->
					flatmateSearchRequest.getRentPeriods().add(codeStoreItemRepository.findOneById(id))
			);
		}

		List<FlatmateAd> list;
		List<FlatmateAd> filteredFlatmateAdList = flatmateAdRepositoryCustom.findAllByCustomFilter(flatmateSearchRequest);

		if (filteredFlatmateAdList.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, filteredFlatmateAdList.size());
			list = filteredFlatmateAdList.subList(startItem, toIndex);
			list = list.stream()
					.distinct()
					.collect(Collectors.toList());
		}

		return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), filteredFlatmateAdList.size());
	}

	public FlatmateAd findOneFlatmateAd(Long id) {
		return flatmateAdRepository.findOneById(id);
	}

	public org.springframework.data.domain.Page<FlatmateAd> findMyFlatmateAdsWithPagination(FlatmateSearchRequest flatmateSearchRequest, Pageable pageable) {

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		flatmateSearchRequest.setAuthUserId(SecurityUtil.getLoggedInUser().getId());

		List<FlatmateAd> list;
		List<FlatmateAd> filteredFlatmateAdList = flatmateAdRepositoryCustom.findAllByCustomFilter(flatmateSearchRequest);

		if (filteredFlatmateAdList.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, filteredFlatmateAdList.size());
			list = filteredFlatmateAdList.subList(startItem, toIndex);
		}

		return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), filteredFlatmateAdList.size());
	}

	public Long saveFlatmatePreferences(FlatmatePreferencesRequest request, Long propertyId) {
		AuthorizedUser authorizedUser = new AuthorizedUser((User) SecurityUtil.getAuthentication().getPrincipal());
		FlatmatePreferences flatmatePreferences = new FlatmatePreferences();

		flatmatePreferences.setAgeFrom(request.getAgeFrom());
		flatmatePreferences.setAgeTo(request.getAgeTo());
		flatmatePreferences.setOccupation(request.getOccupation());
		flatmatePreferences.setStudiesAt(request.getUniversity());
//		flatmatePreferences.setSpeaks(request.getLanguages());
		flatmatePreferences.setEnabled(true);
		flatmatePreferences.setCreatedOn(DateAndTimeUtil.now());
		flatmatePreferences.setCreatedBy(authorizedUser.getId());
		flatmatePreferences.setModifiedOn(DateAndTimeUtil.now());
		flatmatePreferences.setModifiedBy(authorizedUser.getId());

		FlatmatePreferences savedFlatmatePreferences = flatmatePreferencesRepository.saveAndFlush(flatmatePreferences);
//		roomAdRepository.setFlatmatePreferences(savedFlatmatePreferences.getId(), propertyId);
		return propertyId;
	}

	public List<FlatmateAdForm> buildFlatmateAds(Page<FlatmateAd> flatmateAds, BlogLanguage blogLanguage) {
		List<FlatmateAdForm> ads = new ArrayList<>();
		flatmateAds.forEach(ad -> {
			FlatmateAdForm item = new FlatmateAdForm();
			item.setTitle(ad.getTitle());
			item.setId(ad.getId());
			item.setBudget(ad.getBudget());
			item.setUserDetail(ad.getUser().getUserDetail());
			item.setCurrencyCaption(ad.getCurrency().getCaption(blogLanguage.getLanguage()));
			if (ad.getTermOfLease() != null) {
				item.setTermOfLease(ad.getTermOfLease().getCaption(blogLanguage.getLanguage()));
			}
			if (ad.getDistricts() != null) {
				try {
					ad.deserializeDistrictsAttributes();
				} catch (IOException e) {
					e.printStackTrace();
				}
				List<Integer> districtValues = (List<Integer>) ad.getDistrictsMap().get("districts");
				districtValues.forEach(value ->
						item.getDistricts().add(codeStoreItemRepository.findOneById(value).getCaption(blogLanguage.getLanguage()))
				);
			}
			ads.add(item);
		});
		return ads;
	}

	public Model buildFlatmateAdView(Model model, FlatmateAd flatmateAd, UserDetail userDetail, BlogLanguage blogLanguage) {
		Integer userAge = Period.between(userDetail.getDateOfBirth().toLocalDate(), LocalDateTime.now().toLocalDate()).getYears();
		String gender = "";
		String occupation = "";
		List<String> districts = new ArrayList<>();
		String currency = flatmateAd.getCurrency().getCaption(blogLanguage.getLanguage());
		List<String> languages = new ArrayList<>();
		List<String> hobbies = new ArrayList<>();
		String moveInFrom = new SimpleDateFormat("yyyy.MM.dd").format((flatmateAd.getMoveInFrom().getTime()));

		if (userDetail.getGender() != null) {
			gender = userDetail.getGender().getCaption(blogLanguage.getLanguage());
		}
		if (userDetail.getOccupation() != null) {
			occupation = userDetail.getOccupation().getCaption(blogLanguage.getLanguage());
		}

		if (flatmateAd.getDistricts() != null) {
			try {
				flatmateAd.deserializeDistrictsAttributes();
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<Integer> districtValues = (List<Integer>) flatmateAd.getDistrictsMap().get("districts");
			districtValues.forEach(value ->
					districts.add(codeStoreItemRepository.findOneById(value).getCaption(blogLanguage.getLanguage()))
			);
		}

		if (userDetail.getHobbies() != null) {
			try {
				userDetail.deserializeHobbiesAttributes();
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<Integer> hobbyValues = (List<Integer>) userDetail.getHobbiesMap().get("hobbies");
			hobbyValues.forEach(value ->
					hobbies.add(codeStoreItemRepository.findOneById(value).getCaption(blogLanguage.getLanguage()))
			);
		}

		if (userDetail.getLanguages() != null) {
			try {
				userDetail.deserializeLanguagesAttributes();
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<Integer> languageValues = (List<Integer>) userDetail.getLanguagesMap().get("languages");
			languageValues.forEach(value ->
					languages.add(codeStoreItemRepository.findOneById(value).getCaption(blogLanguage.getLanguage()))
			);
		}

		model.addAttribute("userAge", userAge);
		model.addAttribute("gender", gender);
		model.addAttribute("languages", languages);
		model.addAttribute("rentPeriod", flatmateAd.getTermOfLease().getCaption(blogLanguage.getLanguage()));
		model.addAttribute("occupation", occupation);
		model.addAttribute("currency", currency);
		model.addAttribute("moveInFrom", moveInFrom);
		model.addAttribute("districts", districts);
		model.addAttribute("hobbies", hobbies);
		return model;
	}

	public Model buildPropertyAdView(Model model, UserDetail landlord, Property property, BlogLanguage blogLanguage) {
		String gender = "";
		String occupation = "";
		String floor = "";
		List<String> hobbies = new ArrayList<>();
		List<String> languages = new ArrayList<>();
		Integer landlordAge;
		List<String> imageUrls = new ArrayList<>();
		List<String> bathrooms = new ArrayList<>();
		List<String> publicTransports = new ArrayList<>();
		List<String> surroundings = new ArrayList<>();
		String rentPriceCurrency = property.getCurrencyCaption();
		String rentPrice = property.getRentPrice().toString();

		try {
			property.deserializeAdditionalInfoAttributes();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<Integer> bathroomValues = (List<Integer>) property.getAdditionalInfoMap().get("bathrooms");
		bathroomValues.forEach(value ->
				bathrooms.add(codeStoreItemRepository.findOneById(value).getCaption(blogLanguage.getLanguage()))
		);

		List<Integer> publicTransportValues = (List<Integer>) property.getAdditionalInfoMap().get("publicTransports");
		publicTransportValues.forEach(value ->
				publicTransports.add(codeStoreItemRepository.findOneById(value).getCaption(blogLanguage.getLanguage()))
		);

		List<Integer> surroundingsValues = (List<Integer>) property.getAdditionalInfoMap().get("surroundings");
		surroundingsValues.forEach(value ->
				surroundings.add(codeStoreItemRepository.findOneById(value).getCaption(blogLanguage.getLanguage()))
		);

		if (landlord.getDateOfBirth() != null) {
			landlordAge = Period.between(landlord.getDateOfBirth().toLocalDate(), LocalDateTime.now().toLocalDate()).getYears();
			model.addAttribute("landlordAge", landlordAge);
		}
		if (landlord.getProfileImage() != null) {
			model.addAttribute("landlordProfilePic", "/media/" + landlord.getProfileImage().getFilePath());
		} else {
			model.addAttribute("landlordProfilePic", "/media/default/default-profile.png");
		}

		property.getImages().forEach(image ->
				imageUrls.add(image.getFilePath())
		);

		String availableFrom = new SimpleDateFormat("yyyy.MM.dd").format((property.getAvailableFrom().getTime()));
		if (landlord.getGender() != null) {
			gender = landlord.getGender().getCaption(blogLanguage.getLanguage());
		}
		if (landlord.getOccupation() != null) {
			occupation = landlord.getOccupation().getCaption(blogLanguage.getLanguage());
		}
		if (property.getFloor() != null) {
			floor = property.getFloor().getCaption(blogLanguage.getLanguage());
		}
		Map<String, Object> centerLatLng = new HashMap<>();
		List<String> rentPeriods = new ArrayList<>();

		property.setCurrencyCaption(property.getRentPriceCurrency().getCaption(blogLanguage.getLanguage()));
		centerLatLng.put("lat", property.getLocationLat());
		centerLatLng.put("lng", property.getLocationLng());

		property.getTermOfLease().forEach(rentPeriod ->
				rentPeriods.add(rentPeriod.getCaption(blogLanguage.getLanguage()))
		);

		try {
			if (property.getAdditionalInfo() != null) {
				property.deserializeAdditionalInfoAttributes();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (landlord.getHobbies() != null) {
			try {
				landlord.deserializeHobbiesAttributes();
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<Integer> hobbyValues = (List<Integer>) landlord.getHobbiesMap().get("hobbies");
			hobbyValues.forEach(value ->
					hobbies.add(codeStoreItemRepository.findOneById(value).getCaption(blogLanguage.getLanguage()))
			);
		}

		if (landlord.getLanguages() != null) {
			try {
				landlord.deserializeLanguagesAttributes();
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<Integer> languageValues = (List<Integer>) landlord.getLanguagesMap().get("languages");
			languageValues.forEach(value ->
					languages.add(codeStoreItemRepository.findOneById(value).getCaption(blogLanguage.getLanguage()))
			);
		}

		model.addAttribute("CenterLatLng", centerLatLng);
		model.addAttribute("availableFrom", availableFrom);
		model.addAttribute("rentPeriods", rentPeriods);
		model.addAttribute("gender", gender);
		model.addAttribute("occupation", occupation);
		model.addAttribute("floor", floor);
		model.addAttribute("hobbies", hobbies);
		model.addAttribute("languages", languages);
		model.addAttribute("imageUrls", imageUrls);
		model.addAttribute("highlightedImageUrl", imageUrls.get(0));
		model.addAttribute("languages", languages);
		model.addAttribute("imageUrls", imageUrls);
		model.addAttribute("bathrooms", bathrooms);
		model.addAttribute("publicTransports", publicTransports);
		model.addAttribute("surroundings", surroundings);
		model.addAttribute("rentPriceCurrency", rentPriceCurrency);
		model.addAttribute("rentPrice", rentPrice);
		return model;
	}

}
