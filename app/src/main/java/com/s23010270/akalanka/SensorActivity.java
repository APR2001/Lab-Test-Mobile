package com.s23010270.akalanka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private TextView textView;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        textView = findViewById(R.id.textView);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }

    MediaPlayer mp;

    boolean isRunning = false;

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float currentTemp = event.values[0];            runOnUiThread(() -> textView.setText("Current Temperature: " + currentTemp + "°C"));

            if(currentTemp > 70 && !isRunning){
                isRunning = true;
                if (mp != null) {
                    mp.release();
                }
                mp = MediaPlayer.create(this, R.raw.audio);
                if (mp != null) {
                    mp.setLooping(true);
                    mp.start();
                }
            } else if(currentTemp <= 70 && isRunning) {
                if(mp != null) {
                    mp.stop();
                    mp.release();
                    mp = null;
                }
                isRunning = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
        isRunning = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }
}