package com.example.section5_weatherapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    String url = "http://api.openweathermap.org/data/2.5/weather?q=Kielce&units=metric&appid=81a2e49d6e0fc5e41bf3f4bfcc77cbbd";
    String name, description, temp, pressure, humidity, speed, rain, clouds;
    TextView weatherTextView, cityTextView;
    EditText setCityEditText;
    Map<String, String> weatherMap = new HashMap<>();

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

            try {
                JSONObject fullJSONCode = new JSONObject(s);

                name = fullJSONCode.getString("name");

                String weather = fullJSONCode.getString("weather");
                JSONArray weatherArray = new JSONArray(weather);
                JSONObject jsonPart = weatherArray.getJSONObject(0);
                description = jsonPart.getString("description");
                weatherMap.put("Description: ", description + "\n");

                temp = fullJSONCode.getJSONObject("main").getString("temp");
                weatherMap.put("Temperature: ", temp + "C\n");

                pressure = fullJSONCode.getJSONObject("main").getString("pressure");
                weatherMap.put("Pressure: ", pressure + "hPa\n");

                humidity = fullJSONCode.getJSONObject("main").getString("humidity");
                weatherMap.put("Humidity: ", humidity + "%\n");

                speed = fullJSONCode.getJSONObject("wind").getString("speed");
                weatherMap.put("Wind Speed: ", speed + "m/s\n");

                if (fullJSONCode.has("rain")) {
                    rain = fullJSONCode.getJSONObject("rain").getString("3h");
                    weatherMap.put("Rain: ", rain + "mm\n");
                }

                if (fullJSONCode.has("clouds")) {
                    clouds = fullJSONCode.getJSONObject("clouds").getString("all");
                    weatherMap.put("Clouds: ", clouds + "%\n");
                }


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

    @SuppressLint("SetTextI18n")
    public void displayWeather(View view) {
        weatherTextView.setText("");
        cityTextView.setText("");
        cityTextView.setText(name);

        Set entries = weatherMap.entrySet();
        Iterator iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            weatherTextView.append(key + " " + value);
            System.out.println(key + " " + value);
        }

//        setCityEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityTextView = findViewById(R.id.cityTextView);
        weatherTextView = findViewById(R.id.weatherTextView);
        setCityEditText = findViewById(R.id.setCityEditText);
        downloadContent();
    }
}
