package com.worldwideweather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Context context;
    EditText city;
    Button ok_btn;
    ProgressDialog progressDialog;
    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        city = (EditText)findViewById(R.id.city);
        ok_btn = (Button)findViewById(R.id.ok_btn);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(city.getText().toString().trim())){
                    getWeatherData(city.getText().toString());
                }
            }
        });
    }

    private void getWeatherData(String city) {
        progressDialog = ProgressDialog.show(context,null,"One moment please...",false,false);
        String  url = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s",city);
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-api-key",getString(R.string.api_key));
        Fuel.get(url).header(headers).responseString(new Handler<String>() {
            @Override
            public void success(@NotNull Request request, @NotNull Response response, String s) {
                progressDialog.dismiss();
               // Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                playSound(context, R.raw.pop);
                mPlayer.start();
                Intent intent = new Intent(context, WeatherData.class);
                intent.putExtra("data",s);
                startActivity(intent);
            }

            @Override
            public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {
                progressDialog.dismiss();
                Toast.makeText(context, "Error: City not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void playSound(Context context, int file){
        mPlayer = MediaPlayer.create(context, file);
    }
}
