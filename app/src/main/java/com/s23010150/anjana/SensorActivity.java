package com.s23010150.anjana;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private TextView temperatureText;
    private MediaPlayer mediaPlayer;
    private boolean hasPlayed = false;
    private final float THRESHOLD_TEMP = 50.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        temperatureText = findViewById(R.id.tempText);
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }

        if (temperatureSensor == null) {
            Toast.makeText(this, "No temperature sensor found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (temperatureSensor != null) {
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float temp = event.values[0];
        temperatureText.setText("Temperature: " + temp + "°C");

        if (temp > THRESHOLD_TEMP && !hasPlayed) {
            mediaPlayer.start();
            hasPlayed = true;
        } else if (temp <= THRESHOLD_TEMP) {
            hasPlayed = false; // Reset if temp goes back below
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
