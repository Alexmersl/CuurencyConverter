package com.example.myfirstapponkotlin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;

public class HttpConnectionClass {
    private static final String apiKey = "3e20f4c7e005a1301fc8ccca";
    private final static String urlGetCurrency = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/USD";
    private static String urlGetAmount = "";

    public static void updateUrl(String inCurrency, String outCurrency, String amount) {
        urlGetAmount = String.format("https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/%s/%s/%s",
                inCurrency,
                outCurrency,
                amount);
    }

    private static String establishHTTPСonnect(String url) {
        String jsonResponse = "";
        String uri;
        if(url.equals(urlGetCurrency)) {
            uri = urlGetCurrency;
        } else {
            uri = urlGetAmount;
        }
        try {
            URL apiUrl = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    jsonResponse = response.toString();
                }
            } else {
                System.out.println("HTTP Error: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }

    public static HashSet<String> getCurrencySet(){
        HashSet<String> currencySet = new HashSet<>();
        String jsonString = establishHTTPСonnect(urlGetCurrency);
        System.out.println("JSON Response: " + jsonString);
        
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();
            if (jsonObject.has("conversion_rates")) {
                JsonObject ratesObject = jsonObject.getAsJsonObject("conversion_rates");
                for (String key : ratesObject.keySet()) {
                    currencySet.add(key);
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
        }
        
        System.out.println("Currency Set: " + currencySet);
        return currencySet;
    }

    public static String getAmountResult(){
        String jsonString = establishHTTPСonnect(urlGetAmount);
        Gson gson = new Gson();
        String res = "";
        ConversionResult conversionResult = gson.fromJson(jsonString, ConversionResult.class);
        if(conversionResult != null){
            res = conversionResult.getConversionResult();
        }
        return res;
    }
/*
    private static String establishHTTPConnect(String uri)  {
        String responseBody = "";
        HttpRequest httpRequest;
        HttpClient httpClient = HttpClient.newHttpClient();
        if (uri.equals(urlGetCurrency)){
            httpRequest = HttpRequest.newBuilder().
                    uri(URI.create(urlGetCurrency)).
                    GET().
                    build();
        }  else  httpRequest = HttpRequest.newBuilder().
                uri(URI.create(urlGetAmount)).
                GET().
                build();


        try {
            HttpResponse<String> response = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();
        } catch (IOException e){

        } catch (InterruptedException e){

        }
        return responseBody;
    }

 */

}
