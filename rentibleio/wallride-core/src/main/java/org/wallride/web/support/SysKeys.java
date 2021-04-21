package org.wallride.web.support;

public class SysKeys {

    public static final long SYS_ADMIN_USER_ID = 1;

    //available schemas
    public static final int CSI_ID_SCHEMA_CMS_HUN = 700;
    public static final String SCHEMA_CMS_HUN = "cms_hun";
    public static final int CSI_ID_SCHEMA_CMS_GBR = 701;
    public static final String SCHEMA_CMS_GBR = "cms_gbr";
    public static final int CSI_ID_SCHEMA_CMS_NL = 703;
    public static final String SCHEMA_CMS_NL = "cms_nl";


    //Crypto Role:
    public static final int ROLE_CRYPTO = 100;
    public static final String ROLE_CRYPTO_NAME = "crypto";

    //Lodger Role:
    public static final int ROLE_LODGER = 101;
    public static final String ROLE_LODGER_NAME = "lodger";

    //Property Owner Role:
    public static final int ROLE_LAND_LORD = 102;
    public static final String ROLE_LAND_LORD_NAME = "land_lord";

    //Site Admin Role:
    public static final int SITE_ADMIN = 103;
    public static final String SITE_ADMIN_NAME = "site_admin";

    //Admin Role:
    public static final int ROLE_ADMIN = 104;
    public static final String ROLE_ADMIN_NAME = "admin";

    //unactivated lodger Role:
    public static final int ROLE_UNACTIVATED_LODGER = 111;
    public static final String ROLE_UNACTIVATED_LODGER_NAME = "unactivated_lodger";

    //facebook user Role:
    public static final int ROLE_FACEBOOK_USER = 121;
    public static final String ROLE_FACEBOOK_USER_NAME = "facebook_user";

    //Email template for user registration
    public static final int EMAIL_TEMPLATE_USER_REGISTRATION = 500;
    //Email template for user activation
    public static final int EMAIL_TEMPLATE_USER_ACTIVATION = 502;
    //Email template for user deletion
    public static final int EMAIL_TEMPLATE_USER_DELETION = 504;

    //Code store type ids
    public static final int CST_ID_RENTAL_TYPE = 1;
    public static final int CST_ID_PROPERTY_TYPE = 2;
    public static final int CST_ID_VIEW_TYPE = 3;
    public static final int CST_ID_HEATING_TYPE = 4;
    public static final int CST_ID_BUILDING_CONDITION = 5;
    public static final int CST_ID_CURRENCY = 6;
    public static final int CST_ID_SCHEMA = 7;
    public static final int CST_ID_TERM_OF_LEASE = 8;
    public static final int CST_ID_FLOOR = 9;
    public static final int CST_ID_OCCUPATION = 10;
    public static final int CST_ID_GENDER = 11;
    public static final int CST_ID_SOCIAL_MEDIA_PROVIDER_TYPE = 12;
    public static final int CST_ID_NUMBER_OF_ROOMS = 13;
	public static final int CST_ID_LANGUAGES = 14;
    public static final int CST_ID_DISTRICTS = 15;
    public static final int CST_ID_BATHROOM = 16;
    public static final int CST_ID_PUBLIC_TRANSPORT_HU = 17;
    public static final int CST_ID_PUBLIC_TRANSPORT_GBR = 18;
    public static final int CST_ID_SURROUNDINGS = 19;
    public static final int CST_ID_UNIVERSITY_HU = 20;
    public static final int CST_ID_UNIVERSITY_GBR = 21;
    public static final int CST_ID_HOBBIES = 22;

	//Code store item ids
	public static final int CSI_ID_ROOM_RENTAL_TYPE = 101;
	public static final int CSI_ID_FACEBOOK_PROVIDER = 1200;

    private SysKeys() {
    }
}