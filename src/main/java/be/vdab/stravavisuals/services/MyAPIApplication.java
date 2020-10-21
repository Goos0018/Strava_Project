package be.vdab.stravavisuals.services;

import java.net.URL;

public class MyAPIApplication {

    private String category = "Visualizer";
    private String club = null;
    private String clientID = "54588";
    private String clientSecret = "4fa6771db362011bc5d4c0b98930b241e9af34cc";
    private String Code = null;
    private String accessToken = null;
    private String refreshToken = null;
    private String website = "http://www.strava-testapp.com";
    private String authorizationCallbackDomain = "localhost:8080/home";

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
