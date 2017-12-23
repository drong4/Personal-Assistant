package com.example.danielrong.personalassistant.commands;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.danielrong.personalassistant.CalendarActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by danielrong on 12/3/17.
 */

public class ReminderCommand {
    private static final String TAG = "REMINDER";
    private static String[] weekdayNames = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};

    public static void createEvent(Context context){
        Intent intent = new Intent(context, CalendarActivity.class);
        context.startActivity(intent);
    }

    /**
     * Parses the input to get the date. If the input cannot be parsed,
     * return null. Currently can handle "today", "tommorow", and the name of
     * any day of the week ("Sunday", "Monday", etc.). If a weekday is the input,
     * return the soonest date after today that is that weekday. For example,
     * if today is Wednesday and the user says "Monday", return the date for the upcoming
     * Monday.
     *
     * @param input
     * @return a string in the form "YYYY-MM-DD"
     * */
    public static String getDate(String input){
        Calendar currCalendar = Calendar.getInstance();//current date
        currCalendar.add(Calendar.DATE, 1);//do this because it seems the calendar is offset by 1 day

        if("tomorrow".equalsIgnoreCase(input)){
            currCalendar.add(Calendar.DATE, 1);//add 1 day
            return getFormattedDate(currCalendar);
        } else if("today".equalsIgnoreCase(input)){
            return getFormattedDate(currCalendar);
        } else {
            int weekdayInt = getWeekday(input);//Get integer representation of the weekday the user wants (1-7)
            if(weekdayInt != -1){
                int currWeekdayInt = currCalendar.get(Calendar.DAY_OF_WEEK) - 1; //(1-7)
                if(currWeekdayInt == 0){
                    currWeekdayInt = 7;
                }

                int weekdayDiff = weekdayInt - currWeekdayInt;

                assert(weekdayDiff >= -6 && weekdayDiff <= 6);

                if(weekdayDiff >= 0){
                    //If the desired weekday is within this week or IS today
                    currCalendar.add(Calendar.DATE, weekdayDiff);
                } else {
                    //The desired weekday is not within this week
                    int daysToIncrement = (7 - currWeekdayInt) + weekdayInt;
                    currCalendar.add(Calendar.DATE, daysToIncrement);
                }
                return getFormattedDate(currCalendar);
            }
        }
        return null;
    }
    /**
     * Helper function to {@link #getDate(String)}. Returns the integer value of the name of
     * the first weekday contained in the input (1 = Sunday, ..., 7 = Saturday).
     * Return -1 if the input doesn't contain any weekdays or is null.
     *
     * @param input
     * @return integer representing the weekday found in the input
     * */
    private static int getWeekday(String input){
        if(input == null){
            return -1;
        }
        String inputLower = input.toLowerCase();
        for(int i = 0; i < weekdayNames.length; i++){
            String weekday = weekdayNames[i];
            if(inputLower.contains(weekday)){
                return i+1;
            }
        }
        return -1;
    }
    /**
     * Helper function to {@link #getDate(String)}. Returns a formatted string of the date in
     * the given Calendar instance. The returned string will have the format "yyyy-MM-dd".
     *
     * @param cal the calendar instance whose date we're supposed to convert into a string
     * @return a formatted string of the date of the given Calendar instance
     * */
    private static String getFormattedDate(Calendar cal){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(cal.getTime());
        return formattedDate;
    }

    /**
     * Parses the input to get the time of the reminder.
     * Currently only handles things like "5 P.M." or "11 A.M.", but not
     * "4:15 P.M.". If the input can't be parsed, return null.
     *
     * @param input
     * @return a string that represents the time in the format "THH-MM-SS"
     * */
    public static String getTime(String input){
        try {
            String[] strArray = input.split(" ");
            if (strArray.length != 2) {
                return null;
            }
            int hour = Integer.parseInt(strArray[0]);
            if (hour <= 0 || hour > 12) {
                return null;
            }

            if (input.contains("p.m.")) {
                if (hour < 12) {
                    hour += 12;
                }
            } else {
                if(hour == 12){
                    hour = 0;
                }
            }

            String hourString = "";
            if (hour < 10) {
                hourString = "0" + hour;
            } else {
                hourString += hour;
            }

            return "T" + hourString + ":00:00";
        } catch (NumberFormatException ex){
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Parses the input and returns it as an integer. If the input
     * cannot be parsed as an integer, return 60.
     *
     * @param input
     * @return the integer that the user said
     * */
    public static int getReminderNotificationMinutes(String input){
        try {
            int minutes = Integer.parseInt(input);
            return minutes;
        } catch (NumberFormatException ex){
            ex.printStackTrace();
            return 60;//default
        }
    }
}
