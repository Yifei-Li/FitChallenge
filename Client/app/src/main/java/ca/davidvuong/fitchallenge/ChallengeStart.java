package ca.davidvuong.fitchallenge;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.app.Activity;
import android.hardware.Sensor;

import org.w3c.dom.Text;

import java.text.DecimalFormat;


public class ChallengeStart extends Activity implements SensorEventListener {

    private int duration;
    private double stepLength;
    private TextView infoView;
    private int iniCount = -1;

    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;
    private  Sensor mStepDetectorSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_start);

        Intent i = getIntent();
        duration = i.getIntExtra("time",0) * 1000 * 60;
        stepLength = i.getDoubleExtra("length",70);
        Log.d("duration",String.valueOf(duration));
        Log.d("Length",String.valueOf(stepLength));

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        //Start timer
        final TextView minsDisp = (TextView)findViewById(R.id.mins);
        final TextView secsDisp = (TextView)findViewById(R.id.secs);
        infoView = (TextView)findViewById(R.id.textView1);
        new CountDownTimer(duration,1000){

            public void onTick(long millisTillFinished){
                long seconds = millisTillFinished/1000;
                int minutes;
                if(seconds >= 60){
                    minutes = (int)(seconds / 60);
                    seconds = seconds - (minutes * 60);
                }else{
                    minutes = 0;
                }
                minsDisp.setText("" + minutes);
                secsDisp.setText("" + seconds);
            }
            public void onFinish() {
                secsDisp.setText("0");
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_challenge_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSensorChanged(SensorEvent event)
    {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if(values.length > 0){
            value = (int)values[0];
            iniCount = (iniCount == -1)? value: iniCount;
            value -= iniCount;
        }

        if(sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            double dist = value * stepLength / 100;
            DecimalFormat df = new DecimalFormat("#.#");
            infoView.setText(df.format(dist));
        }else if(sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
            //infoView.setText("" + value);
        }

    }

    protected  void onResume(){
        super.onResume();

        mSensorManager.registerListener(this, mStepCounterSensor,

                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mStepDetectorSensor,

                SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this, mStepCounterSensor);
        mSensorManager.unregisterListener(this, mStepDetectorSensor);
    }

    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }
}
