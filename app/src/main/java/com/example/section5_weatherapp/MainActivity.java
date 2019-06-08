package com.example.section5_weatherapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String url = "http://api.openweathermap.org/data/2.5/weather?q=Kielce&units=metric&appid=81a2e49d6e0fc5e41bf3f4bfcc77cbbd";
    String object, value;

    @SuppressLint("StaticFieldLeak")
    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result.append(current);
                    data = reader.read();
                }
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            try {
//                JSONObject jsonObject = new JSONObject(s);
//                weatherInfo = jsonObject.getString("weather");
//                Log.i("Weather info content",weatherInfo);
//                JSONArray jsonArray = new JSONArray(weatherInfo);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonPart = jsonArray.getJSONObject(i);
//                    Log.i("Main",jsonPart.getString("main"));
//                }
            try {
//
                JSONObject fullJSONCode = new JSONObject(s);

                object = fullJSONCode.getString("name");
                Log.i("name",object);

                value = fullJSONCode.getString("weather");
                JSONArray weather = new JSONArray(value);
                JSONObject jsonPart = weather.getJSONObject(0);
                value = jsonPart.getString("description");
                Log.i("weather->description",value);

                value = fullJSONCode.getJSONObject("main").getString("temp");
                Log.i("main->temp", value);



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void downloadContent() {
        DownloadTask task = new DownloadTask();
        try {
            task.execute(url).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayWeather(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadContent();
    }
}
