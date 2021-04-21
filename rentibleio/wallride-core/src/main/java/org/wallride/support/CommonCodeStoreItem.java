package org.wallride.support;

import org.wallride.domain.BlogLanguage;
import org.wallride.domain.CodeStoreItem;
import org.wallride.service.CodeStoreItemService;
import org.wallride.web.support.SysKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonCodeStoreItem {

    private List<Map> currencies;
    private List<Map> termOfLease;
    private List<Map> languages;
    private List<Map> genders;
    private List<Map> schema;
    private List<Map> occupations;
    private List<Map> districts;
    private List<Map> floors;
    private List<Map> bathrooms;
    private List<Map> publicTransportHU;
    private List<Map> publicTransportGBR;
    private List<Map> surroundings;
    private List<Map> universitiesHU;
    private List<Map> universitiesGBR;
    private List<Map> hobbies;

    private CommonCodeStoreItem(CommonCodeStoreItemBuilder commonCodeStoreItemBuilder) {
        this.currencies = commonCodeStoreItemBuilder.currencies;
        this.termOfLease = commonCodeStoreItemBuilder.termOfLease;
        this.languages = commonCodeStoreItemBuilder.languages;
        this.genders = commonCodeStoreItemBuilder.genders;
        this.occupations = commonCodeStoreItemBuilder.occupations;
		this.schema = commonCodeStoreItemBuilder.schema;
        this.districts = commonCodeStoreItemBuilder.districts;
        this.floors = commonCodeStoreItemBuilder.floors;
        this.bathrooms = commonCodeStoreItemBuilder.bathrooms;
        this.publicTransportHU = commonCodeStoreItemBuilder.publicTransportHU;
        this.publicTransportGBR = commonCodeStoreItemBuilder.publicTransportGBR;
        this.surroundings = commonCodeStoreItemBuilder.surroundings;
        this.universitiesHU = commonCodeStoreItemBuilder.universitiesHU;
        this.universitiesGBR = commonCodeStoreItemBuilder.universitiesGBR;
        this.hobbies = commonCodeStoreItemBuilder.hobbies;
	}

    public static class CommonCodeStoreItemBuilder {
        public static final String CAPTION = "caption";
        public static final String ID = "id";
        private List<Map> currencies;
        private List<Map> termOfLease;
        private List<Map> languages;
        private List<Map> genders;
		private List<Map> schema = new ArrayList<>();
		private List<Map> occupations;
        private List<Map> districts;
        private List<Map> floors;
        private List<Map> bathrooms;
        private List<Map> publicTransportHU;
        private List<Map> publicTransportGBR;
        private List<Map> surroundings;
        private List<Map> universitiesHU;
        private List<Map> universitiesGBR;
        private List<Map> hobbies;

        public CommonCodeStoreItemBuilder() {
			List<CodeStoreItem> csiSchema = ContextProvider.getBean(CodeStoreItemService.class).findAllByCodeStoreTypeId(SysKeys.CST_ID_SCHEMA);
			csiSchema.forEach(value -> {
						Map<String, Object> item = new HashMap<>();
						switch (value.getId()) {
						case 700:
                            item.put(ID, value.getId());
                            item.put(CAPTION, "Budapest");
							this.schema.add(item);
							break;
						case 701:
                            item.put(ID, value.getId());
                            item.put(CAPTION, "London");
							this.schema.add(item);
							break;
						case 703:
                            item.put(ID, value.getId());
                            item.put(CAPTION, "Amsterdam");
							this.schema.add(item);
							break;
						default:
							break;
						}
					}
			);
		}

		public List<Map> getCsiSchema() {
			return this.schema;
		}

        public CommonCodeStoreItemBuilder withCsiCurrencies(BlogLanguage blogLanguage) {
            List<CodeStoreItem> csiCurrencies = ContextProvider.getBean(CodeStoreItemService.class).findAllByCodeStoreTypeId(SysKeys.CST_ID_CURRENCY);
            this.currencies = codeStoreItemListToMap(csiCurrencies, blogLanguage);
            return this;
        }

        public List<Map> getCsiCurrencies() {
            return this.currencies;
        }

        public CommonCodeStoreItemBuilder withCsiTermOfLease(BlogLanguage blogLanguage) {
            List<CodeStoreItem> csiTermOfLease = ContextProvider.getBean(CodeStoreItemService.class).findAllByCodeStoreTypeId(SysKeys.CST_ID_TERM_OF_LEASE);
            this.termOfLease = codeStoreItemListToMap(csiTermOfLease, blogLanguage);
            return this;
        }

        public List<Map> getCsiTermOfLease() {
            return this.termOfLease;
        }

        public CommonCodeStoreItemBuilder withCsiLanguages(BlogLanguage blogLanguage) {
            List<CodeStoreItem> csiLanguages = ContextProvider.getBean(CodeStoreItemService.class).findAllByCodeStoreTypeId(SysKeys.CST_ID_LANGUAGES);
            this.languages = codeStoreItemListToMap(csiLanguages, blogLanguage);
            return this;
        }

        public List<Map> getCsiLanguages() {
            return this.languages;
        }

        public CommonCodeStoreItemBuilder withCsiGenders(BlogLanguage blogLanguage) {
            List<CodeStoreItem> csiGenders = ContextProvider.getBean(CodeStoreItemService.class).findAllByCodeStoreTypeId(SysKeys.CST_ID_GENDER);
            this.genders = codeStoreItemListToMap(csiGenders, blogLanguage);
            return this;
        }

        public List<Map> getCsiGenders() {
            return this.genders;
        }

        public CommonCodeStoreItemBuilder withCsiOccupations(BlogLanguage blogLanguage) {
            List<CodeStoreItem> csiOccupations = ContextProvider.getBean(CodeStoreItemService.class).findAllByCodeStoreTypeId(SysKeys.CST_ID_OCCUPATION);
            this.occupations = codeStoreItemListToMap(csiOccupations, blogLanguage);
            return this;
        }

        public List<Map> getCsiOccupations() {
            return this.occupations;
        }

        public CommonCodeStoreItemBuilder withCsiDistricts(BlogLanguage blogLanguage) {
            List<CodeStoreItem> csiDistricts = ContextProvider.getBean(CodeStoreItemService.class).findAllByCodeStoreTypeId(SysKeys.CST_ID_DISTRICTS);
            this.districts = codeStoreItemListToMap(csiDistricts, blogLanguage);
            return this;
        }

        public List<Map> getCsiDistricts() {
            return this.districts;
        }

        public CommonCodeStoreItemBuilder withCsiFloors(BlogLanguage blogLanguage) {
            List<CodeStoreItem> csiFloors = ContextProvider.getBean(CodeStoreItemService.class).findAllByCodeStoreTypeId(SysKeys.CST_ID_FLOOR);
            this.floors = codeStoreItemListToMap(csiFloors, blogLanguage);
            return this;
        }

        public List<Map> getCsiFloors() {
            return this.floors;
        }

        public CommonCodeStoreItemBuilder withCsiBathrooms(BlogLanguage blogLanguage) {
            List<CodeStoreItem> csiBathrooms = ContextProvider.getBean(CodeStoreItemService.class).findAllByCodeStoreTypeId(SysKeys.CST_ID_BATHROOM);
            this.bathrooms = codeStoreItemListToMap(csiBathrooms, blogLanguage);
            return this;
        }

        public List<Map> getCsiBathrooms() {
            return this.bathrooms;
        }

        public CommonCodeStoreItemBuilder withCsiPublicTransportsHU(BlogLanguage blogLanguage) {
            List<CodeStoreItem> csiPublicTransportsHU = ContextProvider.getBean(CodeStoreItemService.class).findAllByCodeStoreTypeId(SysKeys.CST_ID_PUBLIC_TRANSPORT_HU);
            this.publicTransportHU = codeStoreItemListToMap(csiPublicTransportsHU, blogLanguage);
            return this;
        }

        public List<Map> getCsiPublicTransportsHU() {
            return this.publicTransportHU;
        }

        public CommonCodeStoreItemBuilder withCsiPublicTransportGBR(BlogLanguage blogLanguage) {
            List<CodeStoreItem> csiPublicTransportsGBR = ContextProvider.getBean(CodeStoreItemService.class).findAllByCodeStoreTypeId(SysKeys.CST_ID_PUBLIC_TRANSPORT_GBR);
            this.publicTransportGBR = codeStoreItemListToMap(csiPublicTransportsGBR, blogLanguage);
            return this;
        }

        public List<Map> getCsiPublicTransportGBR() {
            return this.publicTransportGBR;
        }

        public CommonCodeStoreItemBuilder withCsiSurroundings(BlogLanguage blogLanguage) {
            List<CodeStoreItem> csiSurroundings = ContextProvider.getBean(CodeStoreItemService.class).findAllByCodeStoreTypeId(SysKeys.CST_ID_SURROUNDINGS);
            this.surroundings = codeStoreItemListToMap(csiSurroundings, blogLanguage);
            return this;
        }

        public List<Map> getCsiSurroundings() {
            return this.surroundings;
        }

        public CommonCodeStoreItemBuilder withCsiUniversitiesHU(BlogLanguage blogLanguage) {
            List<CodeStoreItem> csiUniversitiesHU = ContextProvider.getBean(CodeStoreItemService.class).findAllByCodeStoreTypeId(SysKeys.CST_ID_UNIVERSITY_HU);
            this.universitiesHU = codeStoreItemListToMap(csiUniversitiesHU, blogLanguage);
            return this;
        }

        public List<Map> getCsiUniversitiesHU() {
            return this.universitiesHU;
        }

        public CommonCodeStoreItemBuilder withCsiUniversitiesGBR(BlogLanguage blogLanguage) {
            List<CodeStoreItem> csiUniversitiesGBR = ContextProvider.getBean(CodeStoreItemService.class).findAllByCodeStoreTypeId(SysKeys.CST_ID_UNIVERSITY_GBR);
            this.universitiesGBR = codeStoreItemListToMap(csiUniversitiesGBR, blogLanguage);
            return this;
        }

        public List<Map> getCsiUniversitiesGBR() {
            return this.universitiesGBR;
        }

        public CommonCodeStoreItemBuilder withCsiHobbies(BlogLanguage blogLanguage) {
            List<CodeStoreItem> csiHobbies = ContextProvider.getBean(CodeStoreItemService.class).findAllByCodeStoreTypeId(SysKeys.CST_ID_HOBBIES);
            this.hobbies = codeStoreItemListToMap(csiHobbies, blogLanguage);
            return this;
        }

        public List<Map> getCsiHobbies() {
            return this.hobbies;
        }

        public Map getOneCity(Integer id) {
            Map city;

            city = schema.stream()
                    .filter(c -> c.get(ID).equals(id))
                    .findAny()
                    .orElse(new HashMap());

            return city;
        }

        public CommonCodeStoreItem build() {
            return new CommonCodeStoreItem(this);
        }

        private List<Map> codeStoreItemListToMap(List<CodeStoreItem> codeStoreItemList, BlogLanguage blogLanguage) {
            List<Map> mapList = new ArrayList<>();
            codeStoreItemList.forEach(value -> {
                        Map<String, Object> item = new HashMap<>();
                item.put(ID, value.getId());
                item.put(CAPTION, value.getCaption(blogLanguage.getLanguage()));
                        mapList.add(item);
                    }
            );
            return mapList;
        }
    }


}
