package com.example.danielrong.personalassistant.utils;

/**
 * Created by danielrong on 11/23/17.
 */

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.example.danielrong.personalassistant.MainActivity;

import java.util.Locale;

/**
 * Handles converting text to speech. Contains several functions to speak customized phrases.
 * */
public class MyTextToSpeech {
    private static TextToSpeech ttsObj;
    /**
     * Custom constructor to set up the text-to-speech obj in English
     *
     * @param context the context of where to speak
     * */
    public MyTextToSpeech(Context context){
        Log.d("TTS", "hey");
        ttsObj = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    ttsObj.setLanguage(Locale.US);
                }
            }
        });
    }

    public static boolean isSpeaking(){
        return ttsObj.isSpeaking();
    }

    /**
     * Says the specified message
     *
     * @param message message to speak
     * */
    public static void speak(String message){
        MainActivity.setAppOutputText(message);//Show the user what we're saying
        ttsObj.speak(message, TextToSpeech.QUEUE_FLUSH, null);
    }

    /* -----------------Speaking functions for text messaging----------------------- */

    public static void askForMessageContent(String name){
        speak("Found the contact, \"" + name + "\". What would you like the message to say?");
    }
    public static void askForMessageConfirmation(String message){
        speak("The message you wanted to send says: \"" + message + "\". Is that okay?");
    }
    public static void contactNameNotFound(String name){
        speak("I couldn't find \"" + name + "\" in your contacts.");
    }
    public static void contactNameNotSpecified(){
        speak("Please try that again, but specify the name of the contact you want to text.");
    }
    public static void messageSent(String recipientName){
        speak("Sent text message to \"" + recipientName + "\".");
    }

    /* -----------------Speaking functions for getting the weather----------------------- */

    public static void sayWeather(String description, String temperature, String city){
        speak("The weather in " + city + " is " + temperature + " degrees Fahrenheit with " + description + ".");
    }
    public static void cityNotSpecified(){
        speak("Please try that again, but specify the city that you want to know the weather about.");
    }
    public static void weatherRequestFailed(String city){
        speak("I couldn't find anything for \"" + city + "\". Please make sure you're saying a city.");
    }

    /* -----------------Speaking functions for calling ----------------------- */

    public static void askForCallConfirmation(String recipient){
        speak("You want to call \"" + recipient + "\", is that correct?");
    }

    public static void callRecipientNotSpecified(){
        speak("Please try that again, but specify the contact you want to call.");
    }

    /* -----------------Speaking functions for calling ----------------------- */

    public static void searchQueryNotSpecified(){
        speak("Please specify what you want to search.");
    }

    /* -----------------Speaking functions for setting reminders ----------------------- */

    public static void askForReminderSummary(){
        speak("What do you want the reminder to be about?");
    }

    public static void askForReminderDate(){
        speak("What day do you want the reminder?");
    }

    public static void askForReminderTime(){
        speak("What time do you want the reminder?");
    }

    public static void askForNotificationTime(){
        speak("How many minutes before-hand do you want to be reminded?");
    }

    public static void invalidReminderInput(){
        speak("I couldn't understand that. Please try again.");
    }

    public static void creatingReminder(){
        speak("Okay, let me set that up for you.");
    }

    /* -----------------Speaking functions for initial commands ----------------------- */

    public static void commandNotRecognized(String command){
        speak("I don't understand the command, \"" + command + "\".");
    }
}
