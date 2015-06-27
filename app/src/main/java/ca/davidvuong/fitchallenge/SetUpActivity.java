package ca.davidvuong.fitchallenge;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.graphics.PorterDuff;


public class SetUpActivity extends ActionBarActivity {

    private int timeOption;
    private EditText stepLength;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        stepLength = (EditText)findViewById(R.id.editText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_up, menu);
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

    public void setTimeOption1(View view){
        timeOption = 1;
    }

    public void setTimeOption2(View view){  timeOption = 2; }

    public void setTimeOption3(View view){
        timeOption = 5;
    }

    public void onProceed(View view){
        Intent i = new Intent(getApplicationContext(),ChallengeStart.class);
        i.putExtra("time",timeOption);
        i.putExtra("length",Double.parseDouble(stepLength.getText().toString()));
        startActivity(i);
    }

    private void buttonPress(View view, int option){
            view.getBackground().setColorFilter(0xe0f47521,PorterDuff.Mode.SRC_ATOP);
            Button bt1;
            Button bt2;
            switch(option){
                case 1:
                    bt1 = (Button)findViewById(R.id.Button2);
                    bt2 = (Button)findViewById(R.id.Button5);
                    break;
                case 2:
                    bt1 = (Button)findViewById(R.id.Button1);
                    bt2 = (Button)findViewById(R.id.Button5);
                    break;
                case 5:
                    bt1 = (Button)findViewById(R.id.Button1);
                    bt2 = (Button)findViewById(R.id.Button2);
                    break;
            }
            //bt1.getBackground().clearColorFilter();
            //bt2.getBackground().clearColorFilter();

    }
}
