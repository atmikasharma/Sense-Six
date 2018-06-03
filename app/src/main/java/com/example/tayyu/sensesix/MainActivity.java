package com.example.tayyu.sensesix;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    static String TAG = "MainActivity";
    TextView proxText,proxDimAlertText, temperatureText, temperatureAlert, pressureText, motionText, stepDetectText, stepBonusText, accTextX, accTextY, accTextZ;
    SensorManager senManProx, senManTemperature, senManPressure, senManMotion, senManStepDetect, senManSigMotion, senManAccel;
    Sensor proxSensor, temperatureSensor, pressureSensor, motionSensor, stepDetectSensor,sigMotionSensor, accelSensor;
    static final int RESULT_ENABLE = 1;

//    DevicePolicyManager deviceManger;
//    ActivityManager activityManager;
//    ComponentName compName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "Application is running");

        senManProx = (SensorManager) getSystemService(SENSOR_SERVICE);
        proxSensor = senManProx.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        proxText = (TextView) findViewById(R.id.proxTextView);
        proxDimAlertText = (TextView) findViewById(R.id.proxDimAlert);
        // Create listener
        SensorEventListener proximitySensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // More code goes here
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                if(sensorEvent.values[0] < proxSensor.getMaximumRange()) {
                    // Detected something nearby
                    //getWindow().getDecorView().setBackgroundColor(Color.RED);
                    proxDimAlertText.setText(String.valueOf("Object in close proximity"));

                    lp.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    lp.screenBrightness = 0.00001f; // i needed to dim the display
                    getWindow().setAttributes(lp);
                } else {
                    // Nothing is nearby
                    //getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                    proxDimAlertText.setText(String.valueOf(""));

                    lp.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    lp.screenBrightness = -1f; // i needed to dim the display
                    getWindow().setAttributes(lp);
                }
                proxText.setText(String.valueOf(sensorEvent.values[0]));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        // Register it, specifying the polling interval in microseconds
        senManProx.registerListener(proximitySensorListener, proxSensor, 1 * 1000 * 1000);

        senManTemperature = (SensorManager) getSystemService(SENSOR_SERVICE);
        temperatureSensor = senManTemperature.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        temperatureText = (TextView) findViewById(R.id.temperatureValue);
        temperatureText.setText(String.valueOf("Sensor not found!"));
        temperatureAlert = (TextView) findViewById(R.id.freezeAlertTextView);
        if(temperatureSensor != null)
        {
            SensorEventListener temperatureSensorListner = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    temperatureText.setText(String.valueOf(sensorEvent.values[0]));
                    if((int)sensorEvent.values[0] < 0) {
                        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                        temperatureAlert.setText(String.valueOf("!!!--FREEZE ALERT--!!!"));
                    }
                    else if ((int)sensorEvent.values[0] > 50)
                    {
                        getWindow().getDecorView().setBackgroundColor(Color.RED);
                        temperatureAlert.setText(String.valueOf("!--Very HOT--!"));
                    }
                    else
                    {
                        temperatureAlert.setText(String.valueOf("Safe Temperature"));
                    }

                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };
            senManTemperature.registerListener(temperatureSensorListner, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
        {
            temperatureAlert.setText(String.valueOf(""));
            Toast.makeText(this, "Ambient Temperature sensor not found!", Toast.LENGTH_SHORT).show();
        }

        senManPressure = (SensorManager) getSystemService(SENSOR_SERVICE);
        pressureSensor = senManPressure.getDefaultSensor(Sensor.TYPE_PRESSURE);
        pressureText = (TextView) findViewById(R.id.pressureValue);
        pressureText.setText(String.valueOf("Sensor not found!"));
        if(pressureSensor != null)
        {
            SensorEventListener pressureSensorListner = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    pressureText.setText(String.valueOf(sensorEvent.values[0]));
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };
            senManPressure.registerListener(pressureSensorListner, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
        {
            Toast.makeText(this, "Pressure sensor not found!", Toast.LENGTH_SHORT).show();
        }

        senManMotion = (SensorManager) getSystemService(SENSOR_SERVICE);
        motionText = (TextView) findViewById(R.id.motionValue);
        motionText.setText(String.valueOf("Sensor not found!"));
        motionSensor = senManMotion.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(motionSensor != null)
        {
            SensorEventListener motionSensorListner = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    motionText.setText(String.valueOf(sensorEvent.values[0]));
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };
            senManMotion.registerListener(motionSensorListner, motionSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
        {
            Toast.makeText(this, "Step Counter not found!", Toast.LENGTH_SHORT).show();
        }

        senManStepDetect = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepDetectText = (TextView) findViewById(R.id.stepDetectValue);
        stepDetectText.setText(String.valueOf("Sensor not found!"));
        stepBonusText = (TextView) findViewById(R.id.stepBonus);
        stepDetectSensor = senManStepDetect.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if(motionSensor != null)
        {
            SensorEventListener stepDetectSensorListner = new SensorEventListener() {
                int stepDetectCount = 0;
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    stepDetectCount += sensorEvent.values.length;
                    stepDetectText.setText(String.valueOf(stepDetectCount));
                    if((stepDetectCount == 25) | (stepDetectCount == 26) | (stepDetectCount == 27) | (stepDetectCount == 28) | (stepDetectCount == 29))
                    {
                        stepBonusText.setText(String.valueOf("Bonus fact 1:    Experts suggest walking 6,000 steps a day to improve health and 10,000 steps a day to lose weight"));
                    }
                    if((stepDetectCount == 50) | (stepDetectCount == 51) | (stepDetectCount == 52) | (stepDetectCount == 53) | (stepDetectCount == 54))
                    {
                        stepBonusText.setText(String.valueOf("Bonus fact 2:    It would take, on average, 1 hour and 43 minutes of walking to burn off a 540-calorie Big Mac"));
                    }
                    if((stepDetectCount == 75) | (stepDetectCount == 76) | (stepDetectCount == 77) | (stepDetectCount == 78) | (stepDetectCount == 79))
                    {
                        stepBonusText.setText(String.valueOf("Fun fact 1:    All truly great thoughts are conceived while walking.- Friedrich Nietzsche, Twilight of the Idols"));
                    }
                    if((stepDetectCount == 100) | (stepDetectCount == 101) | (stepDetectCount == 102) | (stepDetectCount == 103) | (stepDetectCount == 104))
                    {
                        stepBonusText.setText(String.valueOf("Bonus fact 3:    A 20-minute walk, or about 2,000 steps, equal a mile"));
                    }
                    if((stepDetectCount == 125) | (stepDetectCount == 126) | (stepDetectCount == 127) | (stepDetectCount == 128) | (stepDetectCount == 129))
                    {
                        stepBonusText.setText(String.valueOf("Bonus fact 4:    Brisk walking helps reduce body fat, lower blood pressure, and increase high-density lipoprotein"));
                    }
                    if((stepDetectCount == 150) | (stepDetectCount == 151) | (stepDetectCount == 152) | (stepDetectCount == 153) | (stepDetectCount == 154))
                    {
                        stepBonusText.setText(String.valueOf("Fun fact 2:    My grandmother started walking five miles a day when she was sixty. She's ninety-seven now, and we don't know where the heck she is. -Ellen DeGeneres"));
                    }
                    if((stepDetectCount == 500) | (stepDetectCount == 501) | (stepDetectCount == 502) | (stepDetectCount == 503) | (stepDetectCount == 504))
                    {
                        stepBonusText.setText(String.valueOf("---Tested on Samsung Galaxy S8 plus---          Thank you Malvika!!!"));
                    }
                    if((stepDetectCount == 525) | (stepDetectCount == 526) | (stepDetectCount == 527) | (stepDetectCount == 528) | (stepDetectCount == 529))
                    {
                        stepBonusText.setText(String.valueOf("VEGETA is the King of all Sayians and is the coolest character in the history of Anime world!"));
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };
            senManStepDetect.registerListener(stepDetectSensorListner, stepDetectSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
        {
            Toast.makeText(this, "Step Detector not found!", Toast.LENGTH_SHORT).show();
        }

        senManSigMotion = (SensorManager) getSystemService(SENSOR_SERVICE);
        sigMotionSensor = senManSigMotion.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        final Context context1 = this;
        if(sigMotionSensor != null)
        {
            SensorEventListener sigMotionSensorListner = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    Toast.makeText(context1, "Significant Motion detected!!", Toast.LENGTH_LONG).show();
                    //stepDetectText.setText(String.valueOf(sensorEvent.values[0]));
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };
            senManSigMotion.registerListener(sigMotionSensorListner, sigMotionSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
        {
            Toast.makeText(this, "Significant Motion detector not found!", Toast.LENGTH_SHORT).show();
        }

        accTextX = (TextView) findViewById(R.id.accelerationXTextView);
        accTextY = (TextView) findViewById(R.id.accelerationYTextView);
        accTextZ = (TextView) findViewById(R.id.accelerationZTextView);
        senManAccel = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelSensor = senManAccel.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Create a listener
        SensorEventListener accelSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // More code goes here
                accTextX.setText(String.valueOf(sensorEvent.values[2]));
                accTextY.setText(String.valueOf(sensorEvent.values[1]));
                accTextZ.setText(String.valueOf(sensorEvent.values[0]));

//                if(sensorEvent.values[2] > 0.5f) { // anticlockwise
//                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
//                } else if(sensorEvent.values[2] < -0.5f) { // clockwise
//                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
//                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        // Register the listener
        senManAccel.registerListener(accelSensorListener, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
