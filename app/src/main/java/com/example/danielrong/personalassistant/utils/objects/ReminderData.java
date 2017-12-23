package com.example.danielrong.personalassistant.utils.objects;

/**
 * Created by danielrong on 12/4/17.
 */

public class ReminderData {
    private static String summary;
    private static String date;//"YYYY-MM-DD"
    private static String time;//"T14:45:00" = 2:45 P.M.

    //minutes before the reminder that the user will be notified
    private static int reminderNotificationMinutes;

    public ReminderData(){
        summary = "";
        date = "";
        time = "";
        reminderNotificationMinutes = 0;
    }

    public static String getSummary() {
        return summary;
    }

    public static void setSummary(String summary) {
        ReminderData.summary = summary;
    }

    public static String getDate() {
        return date;
    }

    public static void setDate(String date) {
        ReminderData.date = date;
    }

    public static String getTime() {
        return time;
    }

    public static void setTime(String time) {
        ReminderData.time = time;
    }

    public static int getReminderNotificationMinutes() {
        return reminderNotificationMinutes;
    }

    public static void setReminderNotificationMinutes(int reminderNotificationMinutes) {
        ReminderData.reminderNotificationMinutes = reminderNotificationMinutes;
    }
}
