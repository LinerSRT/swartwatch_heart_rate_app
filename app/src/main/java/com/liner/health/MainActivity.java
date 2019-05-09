
package com.liner.health;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorMgr;
    Sensor heartRate;
    private TextView heart_rate;
    private ImageView mImageView;
    private Animation mHeartAnim;
    float x1,x2,y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        heart_rate = (TextView) findViewById(R.id.heart_rate);
        sensorMgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        heartRate = sensorMgr.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mImageView = (ImageView) findViewById(R.id.heart_img);
        mHeartAnim = AnimationUtils.loadAnimation(this, R.anim.heart_rate_animation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorMgr.registerListener(this, heartRate, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMgr.unregisterListener(this);
        mImageView.clearAnimation();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_HEART_RATE){
            if (SensorManager.SENSOR_STATUS_NO_CONTACT  == sensorEvent.accuracy ){
                ArcProgress progress = (ArcProgress) findViewById(R.id.arc);
                TextView heart_rate = (TextView) findViewById(R.id.heart_rate);
                progress.setBottomText("Нет контакта");
                progress.setProgress(0);
                heart_rate.setVisibility(View.INVISIBLE);
                mImageView.clearAnimation();
                heart_rate.setText(" ");
            } else {
                getHeartRate(sensorEvent);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {


    }

    private void getHeartRate(SensorEvent sensorEvent){
        TextView heart_rate = (TextView) findViewById(R.id.heart_rate);
        float[] values = sensorEvent.values;
        float data = values[0];
        ArcProgress progress = (ArcProgress) findViewById(R.id.arc);
        if (java.lang.Math.round(data) >= 160){
            heart_rate.setVisibility(View.VISIBLE);
            progress.setBottomText("Опасно для жизни");
        } else if (java.lang.Math.round(data) >= 140) {
            heart_rate.setVisibility(View.VISIBLE);
            progress.setBottomText("Высокая нагрузка");
        } else if (java.lang.Math.round(data) >= 130) {
            heart_rate.setVisibility(View.VISIBLE);
            progress.setBottomText("Средняя нагрузка");
        } else if (java.lang.Math.round(data) >= 120) {
            heart_rate.setVisibility(View.VISIBLE);
            progress.setBottomText("Легкая нагрузка");
        } else if (java.lang.Math.round(data) >= 95) {
            heart_rate.setVisibility(View.VISIBLE);
            progress.setBottomText("Ходьба");
        } else if (java.lang.Math.round(data) >=80) {
            heart_rate.setVisibility(View.VISIBLE);
            progress.setBottomText("Умеренный шаг");
        } else if (java.lang.Math.round(data) >= 50) {
            heart_rate.setVisibility(View.VISIBLE);
            progress.setBottomText("Спокойствие");
        } else if (java.lang.Math.round(data) <= 50) {
            progress.setBottomText(" ");
        }  else if (java.lang.Math.round(data) == 0) {
            progress.setBottomText("Измерение");
            heart_rate.setVisibility(View.INVISIBLE);
        } else {

        }
        progress.setProgress(java.lang.Math.round(data));
        heart_rate.setText(Integer.toString(java.lang.Math.round(data)));
        mImageView.startAnimation(mHeartAnim);
        Log.d("HRM", "Heart rate is " + Integer.toString(java.lang.Math.round(data)));
        Intent intent = new Intent("com.sinsoft.action.health.heartrate");
        intent.putExtra("heart_rate", ((int) data) + "");
        // on this after develop finish
        //Settings.System.putInt(getContentResolver(), "heart_rate", (int) data);
        return;

    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(x1 > x2){
                Intent i = new Intent(MainActivity.this, settings.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
            break;
        }
        return false;
    }
}
