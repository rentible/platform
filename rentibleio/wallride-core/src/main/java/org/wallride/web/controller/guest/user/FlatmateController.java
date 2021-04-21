package org.wallride.web.controller.guest.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wallride.domain.BlogLanguage;
import org.wallride.domain.FlatmateAd;
import org.wallride.service.AdService;
import org.wallride.support.CommonCodeStoreItem;
import org.wallride.util.ObjectUtil;
import org.wallride.util.PagingUtil;
import org.wallride.web.controller.guest.flatmate.FlatmateAdForm;
import org.wallride.web.controller.guest.flatmate.FlatmateForm;
import org.wallride.web.controller.guest.flatmate.FlatmateSearchForm;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class FlatmateController {

	private static final String REDIRECT_TO_EDIT = "redirect:/flatmate-ad?error";
	private static final String ERRORS_MODEL_KEY = BindingResult.MODEL_KEY_PREFIX + "flatmateForm";
	private static final String FORM_MODEL_KEY = "form";
	private static final String ADS = "ads";
	private static final String FLATMATE_ADS = "flatmateAds";
	private static final String TOTAL_PAGES = "totalPages";
	private static final String GENDERS = "genders";
	private static final String TERM_OF_LEASE = "termOfLease";
	private static final String PAGE_NUMBERS = "pageNumbers";
	private static final String CITIES = "cities";
	private static final String FLATMATES_MENU_ACTIVE = "flatmatesMenuActive";
	private static final String CURRENCIES = "currencies";
	private static final String LANGUAGES = "languages";
	private static final String POSTED_FLATMATES = "postedFlatmate";

	@Inject
	private AdService adService;

	@RequestMapping(value = "/flatmate-list")
	public String init(
			BlogLanguage blogLanguage,
			Model model,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size,
			@RequestParam("gender") Optional<Integer> gender,
			@RequestParam("area") Optional<String> area,
			@RequestParam("minAge") Optional<Integer> minAge,
			@RequestParam("maxAge") Optional<Integer> maxAge,
			@RequestParam("city") Optional<Integer> city,
			@RequestParam(value = "termOfLeases[]") Optional<List<Integer>> termOfLeases,
			@Valid @ModelAttribute("form") FlatmateSearchForm flatmateSearchForm) {
		FlatmateSearchForm form;
		CommonCodeStoreItem.CommonCodeStoreItemBuilder builder = new CommonCodeStoreItem.CommonCodeStoreItemBuilder();
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(12);

		if (ObjectUtil.checkAllFieldNullOrEmpty(flatmateSearchForm)) {
			form = new FlatmateSearchForm();
			form.setGender(gender.orElse(null));
			form.setArea(area.orElse(null));
			form.setMinAge(minAge.orElse(null));
			form.setMaxAge(maxAge.orElse(null));
			form.setTermOfLease(termOfLeases.orElse(null));
			form.setCity(city.orElse(700));
			form.setLanguage(blogLanguage.getLanguage());
		} else {
			form = flatmateSearchForm;
			form.setLanguage(blogLanguage.getLanguage());
		}

		form.setTermOfLease(termOfLeases.orElse(Collections.emptyList()));
		Page<FlatmateAd> flatmateAds = adService.findAllFlatmateAdsWithPagination(form.toFlatmateSearchRequest(), PageRequest.of(currentPage - 1, pageSize));
		List<Integer> pageNumbers = PagingUtil.getPageNumbers(flatmateAds.getTotalPages(), currentPage);
		List<FlatmateAdForm> ads = adService.buildFlatmateAds(flatmateAds, blogLanguage);

		builder.withCsiGenders(blogLanguage)
				.withCsiTermOfLease(blogLanguage)
				.build();

		model.addAttribute(FORM_MODEL_KEY, form);
		model.addAttribute(ADS, ads);
		model.addAttribute(FLATMATE_ADS, flatmateAds);
		model.addAttribute(TOTAL_PAGES, flatmateAds.getTotalPages());
		model.addAttribute(GENDERS, builder.getCsiGenders());
		model.addAttribute(TERM_OF_LEASE, builder.getCsiTermOfLease());
		model.addAttribute(PAGE_NUMBERS, pageNumbers);
		model.addAttribute(CITIES, builder.getCsiSchema());
		model.addAttribute(FLATMATES_MENU_ACTIVE, true);
		return "user/flatmate-list";
	}

	@RequestMapping(value = "/post-flatmate-ad", method = RequestMethod.POST)
	public String savePropertyAd(@Valid @ModelAttribute("flatmateForm") FlatmateForm flatmateForm,
			BindingResult errors,
			Model model,
			RedirectAttributes redirectAttributes,
			BlogLanguage blogLanguage) {
		redirectAttributes.addFlashAttribute("flatmateForm", flatmateForm);
		redirectAttributes.addFlashAttribute(ERRORS_MODEL_KEY, errors);
		if (errors.hasErrors()) {
			return REDIRECT_TO_EDIT;
		}
		adService.saveFlatmateAd(flatmateForm.toFlatmateRequest());

		model.addAttribute(POSTED_FLATMATES, flatmateForm);
		return "redirect:/flatmate-list";
	}

	@RequestMapping(value = "/flatmate-ad", method = RequestMethod.GET, params = "error")
	public String validationError(Model model, BlogLanguage blogLanguage) {
		CommonCodeStoreItem.CommonCodeStoreItemBuilder builder = new CommonCodeStoreItem.CommonCodeStoreItemBuilder();

		builder.withCsiCurrencies(blogLanguage)
				.withCsiTermOfLease(blogLanguage)
				.withCsiLanguages(blogLanguage)
				.build();

		model.addAttribute(TERM_OF_LEASE, builder.getCsiTermOfLease());
		model.addAttribute(CURRENCIES, builder.getCsiCurrencies());
		model.addAttribute(LANGUAGES, builder.getCsiLanguages());
		return "user/flatmate";
	}

}
