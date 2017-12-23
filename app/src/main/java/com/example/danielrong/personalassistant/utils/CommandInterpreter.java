package com.example.danielrong.personalassistant.utils;

import android.util.Log;

/**
 * Created by danielrong on 12/2/17.
 */

public class CommandInterpreter {
    //TODO: Edit distance
    private static String[] textCommandVariations =
            {"send text", "send a text", "send message", "send a message"};
    private static String[] weatherCommandVariations =
            {"what's the weather", "what's the temperature", "get the weather", "get the temperature"};
    private static String[] callCommandVariations =
            {"call", "make a call"};
    private static String[] webSearchCommandVariations =
            {"search", "search for"};
    private static String[] reminderCommandVariations =
            {"set reminder", "set a reminder"};

    public static boolean isTextCommand(String command){
        return containsVariation(textCommandVariations, command.toLowerCase());
    }

    public static boolean isWeatherCommand(String command){
        return containsVariation(weatherCommandVariations, command.toLowerCase());
    }

    public static boolean isCallCommand(String command){
        return containsVariation(callCommandVariations, command.toLowerCase());
    }

    public static boolean isWebSearchCommand(String command){
        return containsVariation(webSearchCommandVariations, command.toLowerCase());
    }

    public static boolean isReminderCommand(String command){
        return containsVariation(reminderCommandVariations, command.toLowerCase());
    }

    /**
     * Runs through all the given command variations and if input contains any of them,
     * return true.
     *
     * @param commandVariations
     * @param input
     * @return boolean
     * */
    private static boolean containsVariation(String[] commandVariations, String input){
        for(String variation: commandVariations){
            if(input.contains(variation)){
                return true;
            }
        }
        return false;
    }
}
