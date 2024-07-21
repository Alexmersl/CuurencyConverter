package com.example.myfirstapponkotlin;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;


public class HttpConnectionClass {
    private static final String apiKey = "367217b51779d0d06a6717f7";
    private final static String urlGetCurrency = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/USD";
    private static String urlGetAmount =
            String.format("https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/%s/%s/%s",
                    FieldInput.Companion.getInCurrency(),FieldInput.Companion.getOutCurrency(),
                    FieldInput.Companion.getAmount());

    public static void updateUrl(String inCurrency,String outCurrency,String amount) {
        urlGetAmount = String.format("https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/%s/%s/%s",
                inCurrency,
                outCurrency,
                amount);
    }
    private static String establishHTTPСonnect(String url){
        String jsonResponse = "";
        String uri;
        if(url.equals(urlGetCurrency)){
            uri = urlGetCurrency;
        } else
            uri = urlGetAmount;
        try {
            var apiUrl = new URL(uri);
            var connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try(var reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
                    var response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    jsonResponse = response.toString();
                }
            }
            connection.disconnect(); }
        catch (IOException e){
            System.out.println("Какие-то проблемы");
        }
        return jsonResponse;
    }


    public static HashSet<String> getCurrencySet(){
        var currencySet = new HashSet<String>();
        var jsonString = establishHTTPСonnect(urlGetCurrency);
        var gson = new Gson();
        var currencyData = gson.fromJson(jsonString, CurrencyData.class);

        if (currencyData != null) {
            Map<String, Double> conversionRates = currencyData.getConversionRates();

            if (conversionRates != null) {
                currencySet.addAll(conversionRates.keySet());
            }
        }
        return currencySet;
    }

    public static String getAmountResult(){
        var jsonString = establishHTTPСonnect(urlGetAmount);
        var gson = new Gson();
        String res = "";
        var conversionResult = gson.fromJson(jsonString,ConversionResult.class);
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
