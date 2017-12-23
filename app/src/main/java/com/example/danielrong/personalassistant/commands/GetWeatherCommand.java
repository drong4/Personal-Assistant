package com.example.danielrong.personalassistant.commands;

import android.content.Context;

import com.example.danielrong.personalassistant.R;
import com.example.danielrong.personalassistant.utils.objects.WeatherData;
import com.example.danielrong.personalassistant.utils.api.GetJSONObjectTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by danielrong on 11/25/17.
 */

public class GetWeatherCommand {
    //End point for api request
    /*q=<city>
    * units= {imperial (fahrenheit), metric (celsius)} (default is kelvin)*/
    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=imperial&APPID=%s";

    /**
     * Parses the user input and returns the words after the first occurrence of "in" in 'input'.
     * If there is no occurrence of "in", return null.
     *
     * @param input the raw SMS command supplied by user
     * @return the city the user wants to know the weather in
     * */
    public static String getCity(String input){
        String city = null;

        int ind = input.toLowerCase().indexOf("in");
        if(ind != -1 && (ind + 3) < input.length()){
            //If "in" is a substring and is not the last word...
            //Get the substring after 'in'
            city = input.substring(ind + 3, input.length());
        }
        return city;
    }

    /**
     * Make request call to OpenWeatherMap API and return it as a WeatherData object.
     *
     * @param context
     * @param city
     * @return a WeatherData object containing the desired info
     * */
    public static WeatherData getWeather(Context context, String city){
        try{
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city, context.getString(R.string.open_weather_map_api_key)));
            JSONObject jsonObject = new GetJSONObjectTask().execute(url).get();
            if(jsonObject == null){
                //Something went wrong
                return null;
            }

            //Parse the JSONObject into a WeatherData object
            String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            String temperature = jsonObject.getJSONObject("main").getString("temp");

            return new WeatherData(description, temperature, city);
        } catch (JSONException ex){
            ex.printStackTrace();
            return null;
        } catch (MalformedURLException ex){
            ex.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
