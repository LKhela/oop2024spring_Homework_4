package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class ChatBot {
    private static final String WEATHER_API_KEY = "f16a16b76cd33b18133111e4675cd39a";
    private static final String EXCHANGE_RATE_API_KEY = "d2f75e01bcfb5a481ac9e817";

    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        String city = "London";
        String currencyFrom = "USD";
        String currencyTo = "EUR";

        // Get weather information
        String weatherResponse = getWeather(city);
        System.out.println("Current weather in " + city + ": " + weatherResponse);

        // Get exchange rate
        double exchangeRate = getExchangeRate(currencyFrom, currencyTo);
        System.out.println("Exchange rate from " + currencyFrom + " to " + currencyTo + ": " + exchangeRate);
    }

    private static String getWeather(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + WEATHER_API_KEY;
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("weather").get(0).get("description").asText();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error fetching weather data.";
        }
    }

    private static double getExchangeRate(String from, String to) {
        String url = "https://v6.exchangeratesapi.io/latest?base=" + from + "&symbols=" + to;
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("rates").get(to).asDouble();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
