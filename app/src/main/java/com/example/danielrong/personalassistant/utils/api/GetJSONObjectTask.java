package com.example.danielrong.personalassistant.utils.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.danielrong.personalassistant.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by danielrong on 11/25/17.
 */

/**
 * This class is for getting the JSON data in the form of a JSONObject from a url.
 * AsyncTask<Type of input, Type of progress units published during background computation, Type of the result>
 * */
public class GetJSONObjectTask extends AsyncTask<URL, Void, JSONObject> {
    /**
     * Gets the weather data for the specified city, and returns it as a JSONObject.
     *
     * @return JSONObject containing the data
     * */
    @Override
    protected JSONObject doInBackground(URL... urls){
        try {
            URL currURL = urls[0];
            HttpURLConnection urlConnection = (HttpURLConnection) currURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            //Process the inputStream and store it in a JSONObject
            String result = readStream(in);
            JSONObject jsonObject = new JSONObject(result);

            in.close();
            int response = urlConnection.getResponseCode();

            urlConnection.disconnect();

            Log.d("TASK", "Get request response: " + response);
            return jsonObject;
        } catch (MalformedURLException e) {
//            Log.d("WEATHER", "MalformedURL");
            return null;
        } catch (IOException e) {
//            Log.d("WEATHER", "IOException");
            return null;
        } catch (JSONException e) {
//            Log.d("WEATHER", "JSONException");
            return null;
        }
    }

    /**
     * The following method, readStream, was taken from
     * https://stackoverflow.com/questions/8654876/http-get-using-android-httpurlconnection
     * */
    // Converting InputStream to String
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    /**
     * Will be called after doInBackground() is done, and the result of that
     * function will be passed in as the 'message' parameter.
     * */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        //process message
    }
}
