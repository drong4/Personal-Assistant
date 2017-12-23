package com.example.danielrong.personalassistant.utils.objects;

/**
 * Created by danielrong on 12/2/17.
 */

public class CallData {
    private static String number;
    private static String recipient;

    public CallData(){
        number = "";
        recipient = "";
    }

    public static String getNumber() {
        return number;
    }

    public static void setNumber(String number) {
        CallData.number = number;
    }

    public static String getRecipient() {
        return recipient;
    }

    public static void setRecipient(String recipient) {
        CallData.recipient = recipient;
    }
}
