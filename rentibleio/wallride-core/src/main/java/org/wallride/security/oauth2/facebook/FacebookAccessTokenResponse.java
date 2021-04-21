package org.wallride.security.oauth2.facebook;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FacebookAccessTokenResponse {
    private String accessToken;
    private String tokenType;
    private Integer expiresIn;

}
