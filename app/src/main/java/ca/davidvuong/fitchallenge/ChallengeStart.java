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


public class ChallengeStart extends Activity implements SensorEventListener {

    private int duration;
    private TextView infoView;

    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;
    private  Sensor mStepDetectorSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_start);

        Intent i = getIntent();
        duration = i.getIntExtra("time",0) * 10000;
        Log.d("duration",String.valueOf(duration));

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        //Start timer
        final TextView timeDisp = (TextView)findViewById(R.id.textView3);
        infoView = (TextView)findViewById(R.id.textView8);
        new CountDownTimer(duration,1000){

            public void onTick(long millisTillFinished){
                timeDisp.setText("" + millisTillFinished/1000);
            }
            public void onFinish() {
                timeDisp.setText("done");
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
        }

        if(sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            infoView.setText("Step Counter Detected " + value);
        }else if(sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
            infoView.setText("Step Counter Detected " + value);
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
