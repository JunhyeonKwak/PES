package com.powersupply.PES.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class GithubLoginController {

    @Value("${spring.security.oauth2.client.registration.github.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.clientSecret}")
    private String clientSecret;

    @GetMapping("/login/oauth2/code/github")
    public String getCode(@RequestParam String code) throws IOException {

        //System.out.println("client_id=" + clientId + "&client_secret=" + clientSecret + "&code=" + code);

        URL url = new URL("https://github.com/login/oauth/access_token");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json");

        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))) {
            bufferedWriter.write("client_id=" + clientId + "&client_secret=" + clientSecret + "&code=" + code);
            bufferedWriter.flush();
        }

        int responseCode = connection.getResponseCode();

        String responseData = getResponse(connection, responseCode);

        connection.disconnect();

        System.out.println("responseCode = " + responseCode + " responseData = " + responseData);
        return "index";
    }

    private String getResponse(HttpURLConnection connection, int responseCode) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if(responseCode == 200) {
            try (InputStream inputStream = connection.getInputStream();
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                    stringBuilder.append(line);
                }
            }
        }
        return stringBuilder.toString();
    }
}
