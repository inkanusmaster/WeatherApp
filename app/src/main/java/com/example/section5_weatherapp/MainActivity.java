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

        protected void onPostExecute(String s) { //pod stringiem s mamy wynik result
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather"); //Pobiera z JSONa to co jest w sekcji "weather"
                JSONArray jsonArray = new JSONArray(weatherInfo); //dodajemy do tablicy obiekty z weatherInfo (z sekcji "weather")
                for (int i = 0; i < jsonArray.length(); i++) { //lecimy po tablicy i wybieramy te obiekty, które nas interesują (z sekcji "weather")
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    Log.i("Obiekt z tablicy",jsonPart.toString());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void downloadContent(){
        DownloadTask task = new DownloadTask();
        try {
            task.execute(url).get();
        } catch (Exception e){
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
