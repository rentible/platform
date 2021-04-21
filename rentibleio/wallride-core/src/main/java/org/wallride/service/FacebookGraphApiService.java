package org.wallride.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wallride.domain.SocialMedia;
import org.wallride.domain.User;
import org.wallride.domain.UserDetail;
import org.wallride.repository.RoleRepository;
import org.wallride.repository.SocialMediaRepository;
import org.wallride.repository.UserDetailRepository;
import org.wallride.repository.UserRepository;
import org.wallride.security.oauth2.facebook.FacebookAccessTokenResponse;
import org.wallride.security.oauth2.facebook.FacebookOAuth2UserInfo;
import org.wallride.support.AuthorizedUser;
import org.wallride.util.SecurityUtil;
import org.wallride.web.support.SysKeys;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FacebookGraphApiService {

    private static Logger logger = LoggerFactory.getLogger(FacebookGraphApiService.class);

    @Value("${spring.security.oauth2.client.registration.facebook.clientId}")
    private String oauth2FacebookClientId;

    @Value("${spring.security.oauth2.client.registration.facebook.clientSecret}")
    private String oauth2FacebookClientSecret;

    @Value("${spring.security.oauth2.client.registration.facebook.redirectUriTemplate}")
    private String oauth2FacebookRedirectUri;

    @Value("${spring.security.oauth2.client.provider.facebook.tokenUri}")
    private String oauth2FacebookTokenUri;

	@Value("${spring.security.oauth2.client.provider.facebook.userInfoUri}")
	private String userInfoUri;

	@Resource
	private UserRepository userRepository;

	@Resource
	private UserDetailRepository userDetailRepository;

	@Resource
	private RoleRepository roleRepository;

	@Resource
	private SocialMediaRepository socialMediaRepository;

    /**
     * @param authorizationCode
     * @return
     */
	private FacebookAccessTokenResponse getAccessTokenByAuthorizationCode(String authorizationCode) {

        HttpResponse<JsonNode> response = null;
        FacebookAccessTokenResponse facebookAccessTokenResponse = null;
        try {
            response = Unirest.get(oauth2FacebookTokenUri)
                    .queryString("client_id", oauth2FacebookClientId)
                    .queryString("client_secret", oauth2FacebookClientSecret)
                    .queryString("code", authorizationCode)
                    .queryString("redirect_uri", oauth2FacebookRedirectUri).asJson();

            JSONObject jsonObject = response.getBody().getObject();

            facebookAccessTokenResponse = new FacebookAccessTokenResponse(jsonObject.getString("access_token"), jsonObject.getString("token_type"), (Integer) (jsonObject.get("expires_in")));

        } catch (UnirestException e) {
			logger.error("An error has occurred during getting Facebook accessToken by Authorization code", e);
        }

        return facebookAccessTokenResponse;
    }

	private FacebookOAuth2UserInfo getUserInfo(FacebookAccessTokenResponse facebookAccessTokenResponse) {

		HttpResponse<JsonNode> response;
		FacebookOAuth2UserInfo facebookOAuth2UserInfo = null;
		try {
			response = Unirest.get(userInfoUri)
					.queryString("access_token", facebookAccessTokenResponse.getAccessToken()).asJson();

			JSONObject jsonObject = response.getBody().getObject();
			Map<String, Object> userInfo = toMap(jsonObject);

			facebookOAuth2UserInfo = new FacebookOAuth2UserInfo(userInfo);

		} catch (UnirestException e) {
			logger.error("An error hass occured during getting Facebook acessToken by Authorization code", e);
		}

		return facebookOAuth2UserInfo;

	}

	private static Map<String, Object> toMap(JSONObject object) {
		Map<String, Object> map = new HashMap<>();

		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	public static List<Object> toList(JSONArray array) {
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}

	public User loadUser(String authorizationCode) {
		FacebookAccessTokenResponse facebookAccessTokenResponse = getAccessTokenByAuthorizationCode(authorizationCode);
        FacebookOAuth2UserInfo facebookOAuth2UserInfo = getUserInfo(facebookAccessTokenResponse);

		User user = userRepository.findOneByEmail(facebookOAuth2UserInfo.getEmail());

		if (user == null) {
			LocalDateTime now = LocalDateTime.now();

			SocialMedia socialMedia = new SocialMedia();
			socialMedia.setSocialMediaType(SysKeys.CSI_ID_FACEBOOK_PROVIDER);
			socialMedia.setSocialMediaUserId(facebookOAuth2UserInfo.getId());
			socialMedia.setEnabled(true);
			socialMedia.setCreatedOn(now);
			socialMedia.setCreatedBy(SysKeys.SYS_ADMIN_USER_ID);
			socialMedia.setModifiedOn(now);
			socialMedia.setModifiedBy(SysKeys.SYS_ADMIN_USER_ID);
			socialMedia = socialMediaRepository.saveAndFlush(socialMedia);

			user = new User();
			user.setUsername(facebookOAuth2UserInfo.getEmail());
			user.setEmail(facebookOAuth2UserInfo.getEmail());
			user.setSocialMediaId(socialMedia.getId());
			user.setEnabled(true);
			user.setSchemaId(SysKeys.CSI_ID_SCHEMA_CMS_HUN);
			user.getRoles().add(roleRepository.getRoleByName(SysKeys.ROLE_LODGER_NAME));
			user.getRoles().add(roleRepository.getRoleByName(SysKeys.ROLE_FACEBOOK_USER_NAME));
			user.setCreatedOn(now);
			user.setCreatedBy(SysKeys.SYS_ADMIN_USER_ID);
			user.setModifiedOn(now);
			user.setModifiedBy(SysKeys.SYS_ADMIN_USER_ID);
			user = userRepository.saveAndFlush(user);

			UserDetail userDetail = new UserDetail();
			userDetail.setUser(user);
			userDetail.setFirstName(facebookOAuth2UserInfo.getFirstName());
			userDetail.setLastName(facebookOAuth2UserInfo.getLastName());
			userDetailRepository.saveAndFlush(userDetail);

			SecurityUtil.setUser(new AuthorizedUser(user));

		} else if (user.getSocialMediaId() != null) {
			SocialMedia socialMedia = socialMediaRepository.findOneById(user.getSocialMediaId());
			if (socialMedia.getSocialMediaUserId().equals(facebookOAuth2UserInfo.getId())) {
				SecurityUtil.setUser(new AuthorizedUser(user));
			}
		}


		return user;
	}

}
