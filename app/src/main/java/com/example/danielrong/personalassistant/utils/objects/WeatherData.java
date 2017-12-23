package com.example.danielrong.personalassistant.utils.objects;

/**
 * Created by danielrong on 11/25/17.
 */

/* Object to hold the data from an API call*/
public class WeatherData {
    private String description;//"broken clouds", "light shower", etc.
    private String temperature;
    private String city;

    public WeatherData(){
        this.description = "";
        this.temperature = "";
        this.city = "";
    }

    public WeatherData(String description, String temperature, String city){
        this.description = description;
        this.temperature = temperature;
        this.city = city;
    }

    /* Getter/setters */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
