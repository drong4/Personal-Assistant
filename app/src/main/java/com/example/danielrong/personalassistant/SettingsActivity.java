package com.example.danielrong.personalassistant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.example.danielrong.personalassistant.R;

/**
 * Created by danielrong on 11/13/17.
 */

/**
 * This class keeps track of the preference values. Whenever a specific value is changed,
 * this class notifies the related classes that the value changed.
 */
public class SettingsActivity extends PreferenceActivity {
    public static final String KEY_PREF_ATTENTIVE_MODE = "pref_attentiveMode";
    public static final String KEY_PREF_WAKE_UP_PHRASE = "pref_wakeUpPhrase";

    private SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
            SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                      String key) {
                    if(key.equals(KEY_PREF_WAKE_UP_PHRASE)){
                        //if wake up phrase is changed... notify the main activity that the wake-up phrase changed.
                        MainActivity.setKeyphrase(sharedPreferences.getString(key, ""));
                    }
                }
            };

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Refer to and inflate our settings file
        addPreferencesFromResource(R.xml.settings);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(spChanged);
    }
}
