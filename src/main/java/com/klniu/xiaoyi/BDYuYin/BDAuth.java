package com.klniu.xiaoyi.BDYuYin;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

/**
 * Created by klniu on 17-1-12.
 */
abstract class BDAuth {
    String token;
    //put your own params here
    String apiKey = "zQGouzD3jmFq0wMob2G8MhQ5";
    String secretKey = "1afbf5f91fe7c79134110ba89950f69c";
    String cuid = "xiaoyi_robot";
    private LocalDateTime tokenCreationTime;

    BDAuth(String apiKey, String secretKey, String cuid) throws IOException {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.cuid = cuid;
        if (apiKey.trim().length() < 5 || secretKey.trim().length() < 5 || cuid.trim() == "") {
            throw new IllegalArgumentException("invalid apiKey, secretKey or cuid");
        }
        setToken();
    }

    BDAuth(String apiKey, String secretKey, String cuid, String token, LocalDateTime tokenTime) throws IOException {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.cuid = cuid;
        this.token = token;
        this.tokenCreationTime = tokenTime;
        if (apiKey.trim().length() < 5 || secretKey.trim().length() < 5 || cuid.trim() == "") {
            throw new IllegalArgumentException("invalid apiKey, secretKey or cuid");
        }
        // token invalid or expired
        if (token.length() <= 10 || tokenTime.plusDays(29).isBefore(LocalDateTime.now())) setToken();
    }

    private void setToken() throws IOException {
        String getTokenURL = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials" +
                "&client_id=" + apiKey + "&client_secret=" + secretKey;
        HttpURLConnection conn = (HttpURLConnection) new URL(getTokenURL).openConnection();
        token = getJSON(conn).getString("access_token");
        tokenCreationTime = LocalDateTime.now();
    }

    static JSONObject getJSON(HttpURLConnection conn) throws IOException {
        if (conn.getResponseCode() != 200) {
            // request error
            return new JSONObject();
        }
        InputStream is = conn.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        return new JSONObject(response.toString());
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getTokenCreationTime() {
        return tokenCreationTime;
    }

}

class BDResponseException extends Exception {
    BDResponseException(String s) {
        super(s);
    }
}
