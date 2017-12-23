package com.example.danielrong.personalassistant;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danielrong.personalassistant.commands.CallCommand;
import com.example.danielrong.personalassistant.commands.GetWeatherCommand;
import com.example.danielrong.personalassistant.commands.ReminderCommand;
import com.example.danielrong.personalassistant.commands.SendSMSCommand;
import com.example.danielrong.personalassistant.commands.WebSearchCommand;
import com.example.danielrong.personalassistant.utils.CommandInterpreter;
import com.example.danielrong.personalassistant.utils.MyContacts;
import com.example.danielrong.personalassistant.utils.objects.CallData;
import com.example.danielrong.personalassistant.utils.objects.ReminderData;
import com.example.danielrong.personalassistant.utils.objects.TextMessageData;
import com.example.danielrong.personalassistant.utils.MyTextToSpeech;
import com.example.danielrong.personalassistant.utils.objects.WeatherData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

/**
 * Listener for wake up phrase implemented with the open-source library, CMUSphinx.
 * Listener for commands implemented with Google listening API.
 *
 * CMUSphinx: https://cmusphinx.github.io/wiki/
 */
public class MainActivity extends AppCompatActivity implements RecognitionListener {
    /* Request codes */
    private static final int LISTENING_REQUEST = 200;//initial listening
    private static final int SEND_SMS_REQUEST = 201;//what user want's to text
    private static final int CONFIRM_SMS_REQUEST = 202;//confirming if user wants to send text
    private static final int CONFIRM_CALL_REQUEST = 203;//confirming if user wants to call
    private static final int REMINDER_SUMMARY_REQUEST = 204;//what user wants reminder to be about
    private static final int REMINDER_DATE_REQUEST = 205;//what day user wants reminder to be set
    private static final int REMINDER_TIME_REQUEST = 206;//what time user wants reminder to be set
    private static final int REMINDER_NOTIFICATION_TIME_REQUEST = 207;//how many minutes beforehand that the user wants to be reminded

    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_ALL = 124;

    /* We only need the keyphrase to start recognition, one menu with list of choices,
       and one word that is required for method switchSearch - it will bring recognizer
       back to listening for the keyphrase*/
    private static final String KWS_SEARCH = "wakeup";
    private static final String MENU_SEARCH = "menu";

    SharedPreferences sharedPref;

    /* Keyword we are looking for to activate recognition */
    private static String keyphrase;

    /* Recognition object */
    private static SpeechRecognizer recognizer;

    /* Whether or not attentive listening is on */
    private boolean isListening = false;

    /* UI */
    private static TextView tvUserInput;
    private static TextView tvAppOutput;
    private static TextView tvIntro;
    private static final String HOME_INTRO_FORMAT = "Hello.\n Press the button below \n or say \"%s\" to begin.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set action bar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        //Floating mic button
        FloatingActionButton fab = findViewById(R.id.main_mic_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Listening...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //Do voice recognition by button
                startListening(LISTENING_REQUEST);
            }
        });

        tvUserInput = findViewById(R.id.user_input);
        tvAppOutput = findViewById(R.id.app_output);
        tvIntro = findViewById(R.id.home_intro);

        setUp();//Initialize/set up everything needed
    }

    private void setUp(){
        // Check if user has granted permissions. If not, request permissions
        checkPermissions();

        //Set up wake up phrase listening
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        keyphrase = sharedPref.getString(SettingsActivity.KEY_PREF_WAKE_UP_PHRASE, "").toLowerCase();//pull from preferences
        runRecognizerSetup();

        //Set up text-to-speech object
        new MyTextToSpeech(getApplicationContext());

        //Set up call command
        CallCommand.initialize(getApplicationContext());

        //Set up introductory instructions
        tvIntro.setText(String.format(HOME_INTRO_FORMAT, keyphrase));
    }

    /**
     * Will be called whenever the wake up phrase is changed in the preferences.
     *
     * @param newKeyPhrase the updated value that the user wants as the keyphrase
     * */
    public static void setKeyphrase(String newKeyPhrase) {
        keyphrase = newKeyPhrase.toLowerCase();

        //Update the introductory instructions
        tvIntro.setText(String.format(HOME_INTRO_FORMAT, keyphrase));

        Log.d("MAIN", "changed keyphrase to: " + keyphrase);
    }

    /**
     * Functions to change the UI to let the user see what has been said, both
     * by the user and the app.
     * */
    public static void setUserInputText(String text){
        tvUserInput.setText(text);
    }
    public static void setAppOutputText(String text){
        tvAppOutput.setText(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            makeToast("Settings");
            //Go over to settings activity
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //Every time we switch off the application and switch back, we need to reinstantiate our
        //attentive listener recognizer
        runRecognizerSetup();
    }

    /* -------------------------Functions for listening/voice commands-------------------------- */

    /**
     * Start listening
     *
     * @param requestCode specifies the request we're supposed to be listening for
     * */
    private void startListening(int requestCode) {
        while (MyTextToSpeech.isSpeaking()) {
            //Wait until app isn't speaking so it doesn't over-ride the listening. REALLY bad solution.
            //TODO: Come up with alternative
        }
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);

        recognizer.stop();//turn off attentive listening
        isListening = true;
        try {
            startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            recognizer.startListening(KWS_SEARCH);
            isListening = false;
            makeToast("Intent problem");
            e.printStackTrace();
        }
    }

    /**
     * This function receives the results from calls to startActivityForResult().
     *
     * @param requestCode specifies which request is sending data back
     * @param resultCode specifies the end status of the request
     * @param data the returned data from the request
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        recognizer.startListening(KWS_SEARCH);//turn attentive listening back on
        isListening = false;

        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String speechInput = result.get(0);
            setUserInputText(speechInput);//show on screen what the user said
            if (speechInput.toLowerCase().contains("cancel")) {
                //stop
                return;
            }
            switch (requestCode) {
                case LISTENING_REQUEST:
                    //For initial commands
                    executeVoiceCommand(speechInput);//process command
                    break;
                case SEND_SMS_REQUEST:
                    //Ask user for confirmation that this is the message they want to send
                    MyTextToSpeech.askForMessageConfirmation(speechInput);
                    TextMessageData.setTextMessage(speechInput);//update
                    startListening(CONFIRM_SMS_REQUEST);
                    break;
                case CONFIRM_SMS_REQUEST:
                    if (speechInput.equalsIgnoreCase("yes")) {
                        //User wants to send the current text message
                        SendSMSCommand.sendText(TextMessageData.getTextNumber(), TextMessageData.getTextMessage());
                        MyTextToSpeech.messageSent(TextMessageData.getTextRecipientName());
                    } else {
                        //User doesn't want to send this message. Ask what they want to send.
                        MyTextToSpeech.askForMessageContent(TextMessageData.getTextRecipientName());
                        startListening(SEND_SMS_REQUEST);
                    }
                    break;
                case CONFIRM_CALL_REQUEST:
                    if (speechInput.equalsIgnoreCase("yes")) {
                        //User wants to call
                        CallCommand.makeCall(getApplicationContext(), CallData.getNumber());
                    } else {
                        //User doesn't want to send this message. Stop listening
                    }
                    break;
                case REMINDER_SUMMARY_REQUEST:
                    ReminderData.setSummary(speechInput);//store summary
                    MyTextToSpeech.askForReminderDate();
                    startListening(REMINDER_DATE_REQUEST);
                    break;
                case REMINDER_DATE_REQUEST:
                    String date = ReminderCommand.getDate(speechInput);
                    if(date == null){
                        MyTextToSpeech.invalidReminderInput();
                        startListening(REMINDER_DATE_REQUEST);
                        break;
                    }
                    ReminderData.setDate(date);//store date
                    MyTextToSpeech.askForReminderTime();
                    startListening(REMINDER_TIME_REQUEST);
                    break;
                case REMINDER_TIME_REQUEST:
                    String time = ReminderCommand.getTime(speechInput);
                    if(time == null){
                        MyTextToSpeech.invalidReminderInput();
                        startListening(REMINDER_TIME_REQUEST);
                        break;
                    }
                    ReminderData.setTime(time);//store time
                    MyTextToSpeech.askForNotificationTime();
                    startListening(REMINDER_NOTIFICATION_TIME_REQUEST);
                    break;
                case REMINDER_NOTIFICATION_TIME_REQUEST:
                    int reminderNotificationMinutes = ReminderCommand.getReminderNotificationMinutes(speechInput);
                    ReminderData.setReminderNotificationMinutes(reminderNotificationMinutes);
                    ReminderCommand.createEvent(getApplicationContext());
                    break;
            }
        }
    }

    /**
     * Function to execute the given command. Won't do anything if command is unrecognizable
     *
     * @param command specified command to execute
     * */
    private void executeVoiceCommand(String command) {
        if (command == null) {
            return;
        }

        if (CommandInterpreter.isTextCommand(command)) {
            //User wants to send a text message. Format: "send text to ___"
            startTextCommand(command);
        } else if (CommandInterpreter.isWeatherCommand(command)) {
            //User wants to know the weather. Format: "what's the weather in ___"
            startWeatherCommand(command);
        } else if (CommandInterpreter.isCallCommand(command)) {
            //User wants to make a call.
            startCallCommand(command);
        } else if (CommandInterpreter.isWebSearchCommand(command)) {
            startWebSearchCommand(command);
        } else if (CommandInterpreter.isReminderCommand(command)) {
            startReminderCommand(command);
        } else {
            //Can't recognize this command
            MyTextToSpeech.commandNotRecognized(command);
        }
    }

    //Helper functions to executeVoiceCommand()
    private void startTextCommand(String command){
        TextMessageData.setTextRecipientName(SendSMSCommand.getRecipient(command));
        if (TextMessageData.getTextRecipientName() != null) {
            TextMessageData.setTextNumber(MyContacts.getNumber(getContentResolver(), TextMessageData.getTextRecipientName()));
            if (TextMessageData.getTextNumber() == null) {
                //Couldn't find the contact that the user specified. Stop.
                MyTextToSpeech.contactNameNotFound(TextMessageData.getTextRecipientName());
                return;
            }
            //Ask the user what they want to send.
            MyTextToSpeech.askForMessageContent(TextMessageData.getTextRecipientName());
            startListening(SEND_SMS_REQUEST);
        } else {
            //User didn't specify a contact name
            MyTextToSpeech.contactNameNotSpecified();
        }
    }
    private void startWeatherCommand(String command){
        String city = GetWeatherCommand.getCity(command);
        if (city != null) {
            WeatherData weatherData = GetWeatherCommand.getWeather(getApplicationContext(), city);
            if (weatherData != null) {
                //We got the data! Tell the user.
                MyTextToSpeech.sayWeather(weatherData.getDescription(), weatherData.getTemperature(), weatherData.getCity());
            } else {
                //Something went wrong
                MyTextToSpeech.weatherRequestFailed(city);
            }
        } else {
            //User didn't specify a city
            MyTextToSpeech.cityNotSpecified();
        }
    }
    private void startCallCommand(String command){
        String callRecipient = CallCommand.getRecipient(command);
        String number = MyContacts.getNumber(getContentResolver(), callRecipient);
        if(number != null) {
            CallData.setRecipient(callRecipient);//store name
            CallData.setNumber(number);//store number
            MyTextToSpeech.askForCallConfirmation(callRecipient);
            startListening(CONFIRM_CALL_REQUEST);
        } else {
            if(callRecipient != null) {
                MyTextToSpeech.contactNameNotFound(callRecipient);
            } else {
                MyTextToSpeech.callRecipientNotSpecified();
            }
        }
    }
    private void startWebSearchCommand(String command){
        String searchQuery = WebSearchCommand.getSearchQuery(command);
        if(searchQuery != null) {
            Intent intent = new Intent(this, WebviewActivity.class);
            intent.putExtra("QUERY", searchQuery); // specify search query
            startActivity(intent); //open webview and search
        } else {
            //Search query wasn't specified
            MyTextToSpeech.searchQueryNotSpecified();
        }
    }
    private void startReminderCommand(String command){
        MyTextToSpeech.askForReminderSummary();
        startListening(REMINDER_SUMMARY_REQUEST);
    }

    /* -------------------------Functions for permissions-------------------------- */

    /**
     * Checks if some permissions are enabled. If not, request permissions
     * */
    private void checkPermissions(){
        String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO, Manifest.permission.SEND_SMS,
                                Manifest.permission.READ_CONTACTS, Manifest.permission.INTERNET,
                                Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALENDAR,
                                Manifest.permission.WRITE_CALENDAR, Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.GET_ACCOUNTS, Manifest.permission.MODIFY_AUDIO_SETTINGS};
        if(!hasPermissions(PERMISSIONS)){
            //Request for all these permissions again
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST_ALL);
        }
    }
    /**
     * Helper function for checkPermissions() that says if all the specified permissions are granted
     *
     * @param permissions a string of permissions for us to check
     * @return boolean saying if all the specified permissions are granted
     * */
    private boolean hasPermissions(String... permissions){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    //This permission is not granted to us
                    return false;
                }
            }
        }
        return true;
    }

    /* -------------------------Functions for attentive listening-------------------------- */

    private void runRecognizerSetup() {
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(MainActivity.this);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    System.out.println(result.getMessage());
                } else {
                    switchSearch(KWS_SEARCH);
                }
            }
        }.execute();
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                .getRecognizer();

        recognizer.addListener(this);

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, keyphrase);

        // Create your custom grammar-based search
        File menuGrammar = new File(assetsDir, "mymenu.gram");
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        if ((keyphrase.toLowerCase().equalsIgnoreCase(text))) {
            if(!isListening) {
                startListening(LISTENING_REQUEST);
            }
        } else {
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
        }
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onEndOfSpeech() {
        if (!recognizer.getSearchName().equals(KWS_SEARCH))
            switchSearch(KWS_SEARCH);
    }

    private void switchSearch(String searchName) {
        recognizer.stop();

        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else
            recognizer.startListening(searchName, 10000);
    }

    @Override
    public void onError(Exception error) {
        makeToast(error.getMessage());
    }

    @Override
    public void onTimeout() {
        switchSearch(KWS_SEARCH);
    }

    /* -------------------------Helper functions for utility-------------------------- */

    private void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
