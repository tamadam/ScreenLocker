package com.example.screenlocker;


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Calendar;

public class SensorsActivity extends AppCompatActivity implements SensorEventListener {

    TextView textViewCurrentValueX;
    TextView textViewCurrentValueY;
    TextView textViewCurrentValueZ;
    TextView textViewDate;
    Switch aSwitch;

    float [] lastValues = new float[2];
    boolean enabled = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        MainActivity.patternLockView.clearPattern();

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        textViewDate = findViewById(R.id.textViewDate);
        textViewDate.setText(currentDate);

        textViewCurrentValueX = findViewById(R.id.textViewCurrentValueX);
        textViewCurrentValueY = findViewById(R.id.textViewCurrentValueY);
        textViewCurrentValueZ = findViewById(R.id.textViewCurrentValueZ);
        aSwitch = findViewById(R.id.switch1);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked == true){
                    enabled = true;
                    Toast.makeText(SensorsActivity.this, "Shaking enabled", Toast.LENGTH_SHORT).show();

                }
                else{
                    enabled = false;
                }
            }
        });


        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        textViewCurrentValueX.setText("X = " + (int)x);
        textViewCurrentValueY.setText("Y = " + (int)y);
        textViewCurrentValueZ.setText("Z = " + (int)z);


        float xChange = lastValues[0] - event.values[0];
        float yChange = lastValues[1] - event.values[1];

        lastValues[0] = event.values[0];
        lastValues[1] = event.values[1];

        if (xChange > 3 || xChange < -3 || yChange > 3 || yChange < -3){
            Intent intent = new Intent(SensorsActivity.this, MainActivity.class);
            if (enabled){
                enabled = false;
                startActivity(intent);
            }

        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do here
    }
}
