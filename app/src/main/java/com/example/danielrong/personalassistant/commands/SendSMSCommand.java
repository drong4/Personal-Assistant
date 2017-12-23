package com.example.danielrong.personalassistant.commands;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.SmsManager;

/**
 * Created by danielrong on 11/20/17.
 */

public class SendSMSCommand {
    /**
     * Parses the user input and returns the words after the first occurrence of "to" in 'input'.
     * If there is no occurrence of "to", return null.
     *
     * @param input the raw SMS command supplied by user
     * @return the intended recipient of the text message
     * */
    public static String getRecipient(String input){
        String recipient = null;

        int ind = input.toLowerCase().indexOf("to");
        if(ind != -1 && (ind + 3) < input.length()){
            //If "to" is a substring and is not the last word...
            //Get the substring after 'to'
            recipient = input.substring(ind + 3, input.length());
        }
        return recipient;
    }

    /**
     * Sends a text message to the specified number
     *
     * @param number the recipient of the text
     * @param message the message to be sent
     * */
    public static void sendText(String number, String message){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, message, null, null);
    }
}
