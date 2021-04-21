package org.wallride.web.controller.guest.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.wallride.domain.BlogLanguage;
import org.wallride.domain.FlatmateAd;
import org.wallride.domain.UserDetail;
import org.wallride.model.FlatmateSearchRequest;
import org.wallride.service.AdService;
import org.wallride.web.controller.guest.flatmate.FlatmateAdForm;

import javax.inject.Inject;
import java.util.List;

@Controller
public class FlatmateViewController {

	private static final String AD = "ad";
	private static final String ADS = "ads";
	private static final String USER_DETAIL = "userDetail";
	private static final String RENT_PERIOD = "rentPeriod";
	private static final String CITY = "city";
	private static final String FLATMATES_MENU_ACTIVE = "flatmatesMenuActive";

	@Inject
	AdService adService;

	@RequestMapping(value = "/flatmate-view", method = RequestMethod.GET)
	public String init(Model model, @RequestParam("id") Long id, BlogLanguage blogLanguage) {
		FlatmateAd flatmateAd = adService.findOneFlatmateAd(id);
		UserDetail userDetail = flatmateAd.getUser().getUserDetail();
		Page<FlatmateAd> flatmateAds = adService.findAllFlatmateAdsWithPagination(new FlatmateSearchRequest(), PageRequest.of(0, 4));
		List<FlatmateAdForm> ads = adService.buildFlatmateAds(flatmateAds, blogLanguage);
		adService.buildFlatmateAdView(model, flatmateAd, userDetail, blogLanguage);

		model.addAttribute(AD, flatmateAd);
        model.addAttribute(ADS, ads);
		model.addAttribute(USER_DETAIL, userDetail);
		model.addAttribute(RENT_PERIOD, flatmateAd.getTermOfLease().getCaption(blogLanguage.getLanguage()));
		model.addAttribute(CITY, "Budapest");
		model.addAttribute(FLATMATES_MENU_ACTIVE, true);
		return "user/flatmate-view";
	}

}
