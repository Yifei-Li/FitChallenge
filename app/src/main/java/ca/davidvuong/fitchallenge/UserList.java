package ca.davidvuong.fitchallenge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserList extends ActionBarActivity implements OnTaskCompleted{

    private ConnectMe connectMe;
    String[] newArray = new String[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        Intent i = getIntent();
        String input = i.getStringExtra("names");
        String userName = i.getStringExtra("userName");


        String[] array = input.split(";", 10);
        newArray = Arrays.copyOfRange(array, 1, 8);
        List<String> list = new ArrayList<String>(Arrays.asList(newArray));
        GridView grid = (GridView) findViewById(R.id.gridview);
        grid.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, list));

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                connectMe.cancel(true);
                String name = newArray[position];
                Intent intent = new Intent(getApplicationContext(),Connecting.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

        connectMe = new ConnectMe(this);
        connectMe.execute(this);
    }

    public void processFinish(String output)   {
        //TODO: do something after the connection is established
        Log.d("Received challenge", output);
        if (Arrays.asList(newArray).contains(output)) {
            //TODO: Go to battle view

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
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


    public class ConnectMe extends AsyncTask<Context, Void, String> {
        private OnTaskCompleted listener;

        private BufferedReader in;
        String input = "";
        boolean isReady;
        TCPClient sendData;

        private LocationManager locationManager;

        ConnectMe(OnTaskCompleted listener)   {
            this.listener = listener;
        }

        protected String doInBackground(Context... params) {
            //TODO:assemble string here
            //TODO: send TCP

            try {

                sendData = new TCPClient("192.168.43.101", 1235, listener);

                sendData.connectToServer();
                in = sendData.getBufferReaderInstance();
                //TODO: sendData.sendMessage();

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

        @Override
        protected void onCancelled() {
            try {
                sendData.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
