package com.example.danielrong.personalassistant.commands;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.example.danielrong.personalassistant.utils.PhoneStateReceiver;

/**
 * Created by danielrong on 12/2/17.
 */

public class CallCommand {
    private static TelephonyManager manager;
    private static PhoneStateReceiver myPhoneStateListener;
    private static Context context;
    private static boolean callFromApp=false; // To see if the call has been made from the application
    private static boolean callFromOffHook=false; // To see if the change to idle state is from the app call

    public static void initialize(Context c){
        context = c;
        manager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
        myPhoneStateListener = new PhoneStateReceiver(context);
    }

    /**
     * Parses the user input and returns the words after the first occurrence of "to" or "call" in 'input'.
     * If there is no occurrence of "to" or "call", return null.
     * "to" has higher priority.
     *
     * @param input the raw call command by user
     * @return the intended recipient of the call
     * */
    public static String getRecipient(String input){
        String recipient = null;

        int indCall = input.toLowerCase().indexOf("call");
        int indTo = input.toLowerCase().indexOf("to");

        if(indTo != -1 && (indTo + 3) < input.length()){
            //If "to" is a substring and is not the last word...
            //Get the substring after "to"
            recipient = input.substring(indTo + 3, input.length());
        }

        if(recipient == null && indCall != -1 && (indCall + 5) < input.length()){
            //If there's no occurrence of "to", and "call" is a substring and is not the last word...
            //Get the substring after "call"
            recipient = input.substring(indCall + 5, input.length());
        }
        return recipient;
    }

    /**
     * Makes a call to 'number' and tells the PhoneStateReceiver class
     * to start listening on phone changes.
     *
     * @param context the context from where to start the call
     * @param number the number to call
     * */
    public static void makeCall(Context context, String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.listen(myPhoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE); // start listening to the phone changes
        callFromApp = true;

        context.startActivity(callIntent);//make the call
    }

    /**
     * The following classes are used by the PhoneStateReceiver class
     */
    public static void stopListeningToPhoneChanges(){
        manager.listen(myPhoneStateListener, // Remove listener
                PhoneStateListener.LISTEN_NONE);
    }

    public static boolean isCallFromApp() {
        return callFromApp;
    }

    public static void setCallFromApp(boolean callFromApp) {
        CallCommand.callFromApp = callFromApp;
    }

    public static boolean isCallFromOffHook() {
        return callFromOffHook;
    }

    public static void setCallFromOffHook(boolean callFromOffHook) {
        CallCommand.callFromOffHook = callFromOffHook;
    }
}
