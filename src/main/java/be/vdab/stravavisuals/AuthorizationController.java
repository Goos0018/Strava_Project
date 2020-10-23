package be.vdab.stravavisuals;

import be.vdab.stravavisuals.config.MyAPIApplicationSettings;
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
import org.springframework.web.util.UriBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

@Controller
public class AuthorizationController {

    //TRAINER: you can use resttemplete if you need to send requests to other api's
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MyAPIApplicationSettings myAPI;

    @GetMapping("/exchange_token")
    public String getAuthorization(@RequestParam String code) throws OAuthSystemException, OAuthProblemException, IOException {

        myAPI.setCode(code);
        // Create the Application Authorization Request by
        OAuthClientRequest request = OAuthClientRequest
                // providing the Strava token endpoint,
                .tokenLocation("https://www.strava.com/oauth/token")
                // setting grant type to authorization code,
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                // setting the Client ID of your registered application,
                .setClientId(myAPI.getClientID())
                // setting the Client secret of your registered application,
                .setClientSecret(myAPI.getClientSecret())
                // setting the redirect URI back to the servlet,
                .setRedirectURI(myAPI.getAuthorizationCallbackDomain())
                // setting the previously requested oauth code.
                .setCode(myAPI.getCode())
                .buildQueryMessage();

        // Receive your access token & refresh token.
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(request, OAuthJSONAccessTokenResponse.class);
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
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url.toString(), HttpMethod.GET, entity, String.class);
        int responseCode = response.getStatusCodeValue();
        // Check if responseCode is not 200.
        String inline = null;
        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        } else {
            Scanner sc = new Scanner(url.openStream());
            while (sc.hasNext()) {
                System.out.println(sc.nextLine());
            }
        }



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
