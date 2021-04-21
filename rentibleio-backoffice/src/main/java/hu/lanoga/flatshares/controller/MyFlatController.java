package hu.lanoga.flatshares.controller;

import hu.lanoga.flatshares.SysKeys;
import hu.lanoga.flatshares.model.CodeStoreItem;
import hu.lanoga.flatshares.model.Property;
import hu.lanoga.flatshares.service.CodeStoreItemService;
import hu.lanoga.flatshares.service.PropertyService;
import hu.lanoga.flatshares.util.SecurityUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;

@Getter
@Setter
@Controller
/**
 * Scopes a single bean definition to the lifecycle of a single HTTP request;
 * that is each and every HTTP request will have its own instance of a bean created off the back of a single bean definition.
 * Only valid in the context of a web-aware Spring ApplicationContext
 * https://docs.spring.io/spring/docs/3.0.0.M3/reference/html/ch04s04.html*/

@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyFlatController {

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
    private final PropertyService propertyService;

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private final CodeStoreItemService codeStoreItemService;

    @Setter(AccessLevel.NONE)
    private List<String> availableCountries;
	private String pageTitle;
	private String selectedCountry;
	private Property property = new Property();
	private List<CodeStoreItem> rentalTypes;
	private List<CodeStoreItem> propertyTypes;
	private List<CodeStoreItem> viewTypes;
	private List<CodeStoreItem> heatingTypes;
	private List<CodeStoreItem> buildingConditions;
	private List<CodeStoreItem> currencies;
	private int deleteId;

	public MyFlatController(PropertyService propertyService, CodeStoreItemService codeStoreItemService) {
		this.propertyService = propertyService;
		this.codeStoreItemService = codeStoreItemService;
	}

    @PreAuthorize("hasAuthority('land_lord')")
    @GetMapping(path = "/pages/myflat")
    public String open() {

//        properties = propertyService.findAllBy(SecurityUtil.getLoggedInUser().getId());
        availableCountries = propertyService.getAvailableCountryCodeByUserId(SecurityUtil.getLoggedInUser().getId());

        return "pages/flat/myflat";
    }

	@PreAuthorize("hasAuthority('land_lord')")
	@GetMapping(path = "/pages/myflat/edit/{id}")
	public String openEdit(@PathVariable int id) {
		pageTitle = "Edit property";
        property = propertyService.findOne(SecurityUtil.getLoggedInUser().getSchema(), id);
		getItemsFromCodeStore();
		return "pages/flat/property";
	}

	@PreAuthorize("hasAuthority('land_lord')")
	@GetMapping(path = "/pages/myflat/new")
	public String openNew() {
		pageTitle = "New property";
		property = new Property();
		getItemsFromCodeStore();
		return "pages/flat/property";
	}

	private void getItemsFromCodeStore() {
		rentalTypes = codeStoreItemService.findAllBy(SysKeys.CST_ID_RENTAL_TYPE);
		propertyTypes = codeStoreItemService.findAllBy(SysKeys.CST_ID_PROPERTY_TYPE);
		viewTypes = codeStoreItemService.findAllBy(SysKeys.CST_ID_VIEW_TYPE);
		heatingTypes = codeStoreItemService.findAllBy(SysKeys.CST_ID_HEATING_TYPE);
		buildingConditions = codeStoreItemService.findAllBy(SysKeys.CST_ID_BUILDING_CONDITION);
		currencies = codeStoreItemService.findAllBy(SysKeys.CST_ID_CURRENCY);
	}

	public void onSelectedCountryChange() {
//        properties = propertyService.findAllBy(SecurityUtil.getLoggedInUser().getId(), selectedCountry);
    }

	public void edit() {
        propertyService.update(SecurityUtil.getLoggedInUser().getSchema(), property);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Property updated", null));
	}

	public void save() {
		property.setUserId(SecurityUtil.getLoggedInUser().getId());
        propertyService.save(SecurityUtil.getLoggedInUser().getSchema(), property);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Property saved", null));
	}
}
