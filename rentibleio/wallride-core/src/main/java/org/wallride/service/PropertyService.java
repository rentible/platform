package org.wallride.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.wallride.domain.BlogLanguage;
import org.wallride.domain.Property;
import org.wallride.domain.RoomAd;
import org.wallride.model.PropertySearchRequest;
import org.wallride.repository.CodeStoreItemRepository;
import org.wallride.repository.PropertyRepository;
import org.wallride.repository.RoomAdRepositoryCustom;
import org.wallride.util.SecurityUtil;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PropertyService {

	@Value("${google.map.center-lat-hun}")
	private Double centerLatHun;
	@Value("${google.map.center-lng-hun}")
	private Double centerLngHun;
	@Value("${google.map.center-lat-gbr}")
	private Double centerLatGbr;
	@Value("${google.map.center-lng-gbr}")
	private Double centerLngGbr;
	@Value("${google.map.center-lat-nl}")
	private Double centerLatNl;
	@Value("${google.map.center-lng-nl}")
	private Double centerLngNl;

    @Autowired
    private PropertyRepository propertyRepository;

	@Resource
	private RoomAdRepositoryCustom roomAdRepositoryCustom;

	@Resource
	private CodeStoreItemRepository codeStoreItemRepository;

    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    public Property findOne(long id) {
        Optional<Property> p = propertyRepository.findById(id);
        return p.orElse(null);
    }

    public long count() {
        return propertyRepository.count();
    }

    //    @Cacheable(value = WallRideCacheConfiguration.PROPERTY_CACHE)
	public Page<RoomAd> findAllWithPagination(PropertySearchRequest propertySearchRequest, Pageable pageable) {

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
		if (propertySearchRequest.getTermOfLease() != null) {
			propertySearchRequest.getTermOfLease().forEach(id ->
					propertySearchRequest.getRentPeriods().add(codeStoreItemRepository.findOneById(id))
			);
		}
		List<RoomAd> list;
		List<RoomAd> filteredPropertyList = roomAdRepositoryCustom.findAllByCustomFilter(propertySearchRequest);

		if (filteredPropertyList.size() < startItem) {
			list = Collections.emptyList();
        } else {
			int toIndex = Math.min(startItem + pageSize, filteredPropertyList.size());
			list = filteredPropertyList.subList(startItem, toIndex);
			list = list.stream()
					.distinct()
					.collect(Collectors.toList());
		}

		return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), filteredPropertyList.size());
	}

	public Map<String, Object> getCenterLatLng(Integer city) {
		Map<String, Object> centerLatLng = new HashMap<>();

		switch (city) {
		case 700:
			centerLatLng.put("lat", centerLatHun);
			centerLatLng.put("lng", centerLngHun);
			break;
		case 701:
			centerLatLng.put("lat", centerLatGbr);
			centerLatLng.put("lng", centerLngGbr);
			break;
		case 703:
			centerLatLng.put("lat", centerLatNl);
			centerLatLng.put("lng", centerLngNl);
			break;
		default:
			centerLatLng.put("lat", centerLatHun);
			centerLatLng.put("lng", centerLngHun);
			break;
		}

		return centerLatLng;
	}

	public Page<RoomAd> findMyAdsWithPagination(PropertySearchRequest propertySearchRequest, Pageable pageable) {

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		propertySearchRequest.setAuthUserId(SecurityUtil.getLoggedInUser().getId());

		List<RoomAd> list;
		List<RoomAd> filteredPropertyList = roomAdRepositoryCustom.findAllByCustomFilter(propertySearchRequest);

		if (filteredPropertyList.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, filteredPropertyList.size());
			list = filteredPropertyList.subList(startItem, toIndex);
		}

		return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), filteredPropertyList.size());
	}

	public Model setSelectedRentPeriods(List<Integer> rentPeriods, Model model, BlogLanguage blogLanguage) {

		List<String> selectedRentPeriods = new ArrayList<>();

		rentPeriods.forEach(item ->
				selectedRentPeriods.add(codeStoreItemRepository.findOneById(item).getCaption(blogLanguage.getLanguage()))
		);

		model.addAttribute("selectedRentPeriods", selectedRentPeriods);
		return model;
	}

	public Model setSelectedOccupations(List<Integer> occupations, Model model, BlogLanguage blogLanguage) {

		List<String> selectedOccupations = new ArrayList<>();

		occupations.forEach(item ->
				selectedOccupations.add(codeStoreItemRepository.findOneById(item).getCaption(blogLanguage.getLanguage()))
		);

		model.addAttribute("selectedOccupations", selectedOccupations);
		return model;
	}

}
