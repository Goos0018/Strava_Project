package be.vdab.stravavisuals.controller;

import be.vdab.stravavisuals.config.MyAPIApplicationSettings;
import javastrava.api.v3.auth.AuthorisationService;
import javastrava.api.v3.auth.impl.retrofit.AuthorisationServiceImpl;
import javastrava.api.v3.auth.model.Token;
import javastrava.api.v3.model.StravaAthlete;
import javastrava.api.v3.service.Strava;
import org.apache.http.client.HttpResponseException;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

@Controller
public class AuthorizationController {

    //TRAINER: you can use rest-template if you need to send requests to other api's
    private RestTemplate restTemplate;

    private MyAPIApplicationSettings myAPI;

    @Autowired
    public AuthorizationController setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        return this;
    }

    @Autowired
    public AuthorizationController setMyAPI(MyAPIApplicationSettings myAPI) {
        this.myAPI = myAPI;
        return this;
    }

    @GetMapping("/exchange_token")
    public String getAuthorization(@RequestParam String code)
            throws OAuthSystemException, OAuthProblemException, IOException {

        myAPI.setCode(code);
        // Create the Application Authorization Request by
        OAuthClientRequest request = OAuthClientRequest
                // providing the Strava token endpoint,
                .tokenLocation("https://www.strava.com/oauth/token")
                // setting grant type to authorization code,
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                // setting the Client ID of your registered application,
                .setClientId(myAPI.getClientID().toString())
                // setting the Client secret of your registered application,
                .setClientSecret(myAPI.getClientSecret())
                // setting the redirect URI back to the servlet,
                .setRedirectURI(myAPI.getAuthorizationCallbackDomain())
                // setting the previously requested oauth code.
                .setCode(myAPI.getCode())
                .buildQueryMessage();

        // Receive your access token & refresh token.
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(
                request,
                OAuthJSONAccessTokenResponse.class);
        String accessToken = oAuthResponse.getAccessToken();
        myAPI.setAccessToken(accessToken);
        String refreshToken = oAuthResponse.getRefreshToken();
        myAPI.setRefreshToken(refreshToken);


        //Use the access token to query Strava.

        // Pass the desired URL as an object.
        URL url = new URL("http://www.strava.com/api/v3/athlete/activities");

//        // Typecast the URL into a HTTPURLConnection object.
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//        //Set the request type. GET / POST
//        conn.setRequestMethod("GET");
//
//        // Open a connection stream.
//        conn.connect();
//
//        // Get corresponding response code.
//        int responseCode = conn.getResponseCode();

        System.out.println(url.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer " + myAPI.getAccessToken());
        HttpEntity<String> entityWithHeader = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url.toString(), HttpMethod.GET, entityWithHeader, String.class);
        int responseCode = response.getStatusCodeValue();
        // Check if responseCode is 200.
        if (responseCode == 200) {
            System.out.println(response);
            System.out.println(response.getBody());
        } else {
            throw new HttpResponseException(responseCode, "HttpResponseCode: " + responseCode);
        }

        AuthorisationService service = new AuthorisationServiceImpl();
        Token token = service.tokenExchange(
                myAPI.getClientID(),
                myAPI.getClientSecret(),
                myAPI.getCode());
        Strava strava = new Strava(token);
        StravaAthlete athlete = strava.getAthlete(token.getAthlete().getId());

        /*// Use the access token to query Strava API.
        OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest("http://www.strava.com/api/v3/athlete/activities")
                .setAccessToken(myAPI.getAccessToken()).buildQueryMessage();

        // Get the response and print the body.
        OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
        //PrintWriter out = resp.getWriter();
        //out.println(resourceResponse.getBody());
        System.out.println(resourceResponse.getBody());*/

        return "home";

    }
}
