package be.vdab.stravavisuals.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;

//TRAINER: This looks more like a configuration class than a service?
@Component
public class MyAPIApplicationSettings {

    //all the values are set in application.properties so you don't need to hardcode values in your java classes
    @Value("${category}")
    private String category ;
    private String club = null;
    @Value("${clientID}")
    private String clientID ;
    @Value("${clientSecret}")
    private String clientSecret ;
    private String Code = null;
    private String accessToken = null;
    private String refreshToken = null;
    @Value("${website}")
    private String website ;
    @Value("${authorizationCallbackDomain}")
    private String authorizationCallbackDomain ;

    public void setCode(String code) {
        Code = code;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getCategory() {
        return category;
    }

    public String getClub() {
        return club;
    }

    public String getClientID() {
        return clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getCode() {
        return Code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getWebsite() {
        return website;
    }

    public String getAuthorizationCallbackDomain() {
        return authorizationCallbackDomain;
    }
}
