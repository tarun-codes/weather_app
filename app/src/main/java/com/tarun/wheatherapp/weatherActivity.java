package com.tarun.wheatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class weatherActivity extends AppCompatActivity {

    EditText cityInput;
    Button cityBtn;
    TextView cityTextView, timeTextView, dateTextView, weatherStatusText, temperatureText;
    ImageView weatherStatusImg;
    String currentTime, dateOutput;
    String cityEntered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        cityTextView = findViewById(R.id.city_text_view);
        cityTextView.setText("");
        timeTextView = findViewById(R.id.time_text_view);
        timeTextView.setText("");
        dateTextView = findViewById(R.id.date_text_view);
        dateTextView.setText("");
        weatherStatusImg = findViewById(R.id.weather_img);
        temperatureText = findViewById(R.id.temperature_text);
        temperatureText.setText("");
        weatherStatusText = findViewById(R.id.weather_status_text);
        weatherStatusText.setText("");
        cityInput = findViewById(R.id.city_txt_input);
        cityBtn = findViewById(R.id.city_btn);
        cityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //to hide keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).
                        getRootView().getWindowToken(),0);
                cityEntered = cityInput.getText().toString();

                //To ensure editText is not empty
                if(cityEntered.trim().equals(""))
                    Toast.makeText(weatherActivity.this, "Enter city name", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(weatherActivity.this, "Fetching data, please wait", Toast.LENGTH_SHORT).show();
                    getDateAndTime();
                    api_url(cityEntered); //passing user input
                }
            }
        });
    }

    private void getDateAndTime() {

        //getting date and time
        Date date = new Date();
        currentTime = new SimpleDateFormat("hh:mm a",Locale.getDefault()).format(date);
        String currentDay = new SimpleDateFormat("EEEE",Locale.getDefault()).format(date);
        String currentDate = new SimpleDateFormat("dd",Locale.getDefault()).format(date);
        String currentMonth = new SimpleDateFormat("MMMM",Locale.getDefault()).format(date);
        dateOutput = currentDay +  ", " + currentDate + " " + currentMonth;
    }

    public void api_url(String citySearch)
    {
        // creating url as per user input
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+citySearch+"&appid=b761b4cfe64507fdd7579ab7daf29996&units=metric";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject mainObject = response.getJSONObject("main");
                    JSONArray weatherArray = response.getJSONArray("weather");
                    JSONObject description = weatherArray.getJSONObject(0);
                    JSONObject icon = weatherArray.getJSONObject(0);
                    String iconId = icon.getString("icon");
                    String temp = (Math.round(mainObject.getDouble("temp")))+"°C";
                    String desc = description.getString("main");
                    updateUI(temp,desc); // method for setting values to the textViews
                    SetIcon(iconId); // setting icon as per response from api
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Log.e("volley", Objects.requireNonNull(error.getMessage()));
                    Toast.makeText(weatherActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(weatherActivity.this, "Invalid City name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void updateUI(String temperature, String description)
    {
        // showing output
        temperatureText.setText(temperature);
        weatherStatusText.setText(description);
        timeTextView.setText(currentTime);
        dateTextView.setText(dateOutput);
        cityTextView.setText(cityEntered);
    }

   public void SetIcon(String id){
        switch (id){
            case "01d":
               weatherStatusImg.setImageResource(R.drawable.clear_skyd);
               break;
            case "01n":
                weatherStatusImg.setImageResource(R.drawable.clear_skyn);
                break;
            case "02d":
                weatherStatusImg.setImageResource(R.drawable.few_cloudsd);
                break;
            case "02n":
                weatherStatusImg.setImageResource(R.drawable.few_cloudn);
                break;
            case "03d":
                weatherStatusImg.setImageResource(R.drawable.few_cloudsd);
                break;
            case "03n":
                weatherStatusImg.setImageResource(R.drawable.few_cloudn);
                break;
            case "04d":
                weatherStatusImg.setImageResource(R.drawable.few_cloudsd);
                break;
            case "04n":
                weatherStatusImg.setImageResource(R.drawable.few_cloudn);
                break;
            case "09d":
                weatherStatusImg.setImageResource(R.drawable.rain);
                break;
            case "09n":
                weatherStatusImg.setImageResource(R.drawable.rain);
                break;
            case "10d":
                weatherStatusImg.setImageResource(R.drawable.rain);
                break;
            case "10n":
                weatherStatusImg.setImageResource(R.drawable.rain);
                break;
            case "11d":
                weatherStatusImg.setImageResource(R.drawable.storm);
                break;
            case "11n":
                weatherStatusImg.setImageResource(R.drawable.storm);
                break;
            case "13d":
                weatherStatusImg.setImageResource(R.drawable.snow);
                break;
            case "13n":
                weatherStatusImg.setImageResource(R.drawable.snow);
                break;
            case "50d":

                weatherStatusImg.setImageResource(R.drawable.mist);
                break;
            case "50n":
                weatherStatusImg.setImageResource(R.drawable.mistn);
                break;
        }
   }
}
