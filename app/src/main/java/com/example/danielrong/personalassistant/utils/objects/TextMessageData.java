package com.example.danielrong.personalassistant.utils.objects;

/**
 * Created by danielrong on 11/23/17.
 */

/**
 * This class holds information about the latest text message, recipient name, and recipient number
 * */
public class TextMessageData {
    private static String textNumber;
    private static String textMessage;
    private static String textRecipientName;

    public TextMessageData(){
        textMessage = textNumber = textRecipientName = "";
    }

    public static String getTextNumber() {
        return textNumber;
    }

    public static void setTextNumber(String textNumber) {
        TextMessageData.textNumber = textNumber;
    }

    public static String getTextMessage() {
        return textMessage;
    }

    public static void setTextMessage(String textMessage) {
        TextMessageData.textMessage = textMessage;
    }

    public static String getTextRecipientName() {
        return textRecipientName;
    }

    public static void setTextRecipientName(String textRecipientName) {
        TextMessageData.textRecipientName = textRecipientName;
    }
}
