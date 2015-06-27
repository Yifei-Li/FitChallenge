package ca.davidvuong.fitchallenge;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;


public class Connecting extends ActionBarActivity implements OnTaskCompleted{

    private AChallengerIsNear aChallengerIsNear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting);

        TextView display = (TextView)findViewById(R.id.display);
        Intent i = getIntent();
        String name = i.getStringExtra("name");

        Log.d("Names", name);

        display.setText("Challenging " + name + "...");
        aChallengerIsNear = new AChallengerIsNear(name, this);
        aChallengerIsNear.execute(this);
    }

    public void processFinish(String result) {
        //TODO: do something here
        Log.d("Result", result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connecting, menu);
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

    public class AChallengerIsNear extends AsyncTask<Context, Void, String> {
        private OnTaskCompleted listener;
        private String sendName;

        private BufferedReader in;
        String input = "";
        boolean isReady;

        AChallengerIsNear(String name, OnTaskCompleted listener)   {
            this.sendName = name;
            this.listener = listener;
            Log.d("Construct", "Constructed");
        }

        protected String doInBackground(Context... params) {
            Log.d("Background", "started");

            try {

                TCPClient sendData = new TCPClient("192.168.43.101", 1235, listener);

                sendData.connectToServer();
                in = sendData.getBufferReaderInstance();
                //TODO: sendData.sendMessage();
                Log.d("Challenge", "Ready to send");
                sendData.sendMessage(sendName);
                Log.d("Challenge", "Sent " + sendName);

                try {

                    while (true) {

                        isReady = in.ready();
                        if (isReady){
                            input = in.readLine();
                            sendData.close();
                            return input;
                        }
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


            return null; //TODO: return string deliminated by ;
        }

        protected void onPostExecute(final String result)   {
            listener.processFinish(result);
        }

    }
}
