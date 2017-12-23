package com.example.danielrong.personalassistant.utils;

import android.content.Context;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.example.danielrong.personalassistant.commands.CallCommand;

/**
 * Created by danielrong on 12/11/17.
 */

/**
 * This class is used by the CallCommand class to listen on the state of the phone.
 * Whenever this app creates a call, this class automatically turns on speaker phone.
 * After the call is over, it turns speaker phone off.
 *
 * Template taken from
 * http://danielthat.blogspot.com/2013/06/android-make-phone-call-with-speaker-on.html
 */
public class PhoneStateReceiver extends PhoneStateListener {
    Context context;

    public PhoneStateReceiver(Context context){
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        switch (state) {

            case TelephonyManager.CALL_STATE_OFFHOOK: //Call is established
                if (CallCommand.isCallFromApp()) {
                    CallCommand.setCallFromApp(false);
                    CallCommand.setCallFromOffHook(true);

                    try {
                        Thread.sleep(500); // Delay 0,5 seconds to handle better turning on loudspeaker
                    } catch (InterruptedException e) {
                    }

                    //Activate loudspeaker
                    AudioManager audioManager = (AudioManager)
                            context.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                    audioManager.setSpeakerphoneOn(true);
                }
                break;

            case TelephonyManager.CALL_STATE_IDLE: //Call is finished
                if (CallCommand.isCallFromOffHook()) {
                    CallCommand.setCallFromOffHook(false);
                    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setMode(AudioManager.MODE_NORMAL); //Deactivate loudspeaker
                    CallCommand.stopListeningToPhoneChanges(); //Remove the listener
                }
                break;
        }
    }
}