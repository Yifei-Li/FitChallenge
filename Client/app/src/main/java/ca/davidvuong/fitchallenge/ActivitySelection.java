package ca.davidvuong.fitchallenge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;

public class ActivitySelection extends ActionBarActivity implements OnTaskCompleted{

    private String userName;
    private FindOthers findTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_selection);

        Intent i = getIntent();
        userName = i.getStringExtra("userName");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    public void processFinish(String output)   {
        Log.d("Async", output);
        Intent i = new Intent(getApplicationContext(),UserList.class);
        String pushy = output;
        String pushName = userName;
        i.putExtra("names",pushy);
        i.putExtra("userName", pushName);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_selection, menu);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void doRun(View view){
        findTask = new FindOthers(userName, "Running", this);
        findTask.execute(this);
    }

    public void startComp(View view){
        Intent i = new Intent(getApplicationContext(),SetUpActivity.class);
        startActivity(i);

    }

    public class FindOthers extends AsyncTask<Context, Void, String> /*implements LocationListener*/ {
        private ProgressDialog dialog = new ProgressDialog(ActivitySelection.this);
        private OnTaskCompleted listener;

        private BufferedReader in;
        String input = "";
        boolean isReady;

        //private double longitude;
        //private double latitude;
        private String activity;
        private String user;
        private LocationManager locationManager;

        FindOthers(String username, String chosenActivity, OnTaskCompleted listener)   {
            user = username;
            activity = chosenActivity;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute()   {
            this.dialog.setMessage("Wait bitches");
                this.dialog.show();
            }

        protected String doInBackground(Context... params) {
            /*locationManager = (LocationManager) params[0].getSystemService(Context.LOCATION_SERVICE);
            Looper.prepare();
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
            Looper.loop();*/

            WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();

            String myIP = String.format("%d.%d.%d.%d",
                    (ipAddress & 0xff),
                    (ipAddress >> 8 & 0xff),
                    (ipAddress >> 16 & 0xff),
                    (ipAddress >> 24 & 0xff));

            //Log.d("IPADDRESS", myIP);

            //TODO:assemble string here
            String send1 =  "INSERT INTO users values( '"+ user + "','" + activity + "'," + 0 + ",'"+myIP+"');";
            String send2 = "SELECT * FROM users;";
            //Log.d("sending string", send1);

            //TODO: send TCP

            try {

                TCPClient sendData = new TCPClient("192.168.43.101", 1235, listener);

                sendData.connectToServer();
                in = sendData.getBufferReaderInstance();
                sendData.sendMessage(send2);
                try     {
                    Thread.sleep(200);
                } catch (InterruptedException e)    {
                    return "nope";
                }

                sendData.sendMessage(send1);

                try {

                    while (true) {

                        isReady = in.ready();
                        if (isReady){
                            input = in.readLine();
                            Log.d("TEST", input);
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
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            /*Log.d("location", "longitude: " + longitude);
            Log.d("location", "latitude: " + latitude);
            Log.d("result", result);*/
            listener.processFinish(result);

        }

        /*public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Log.d("location:", "it changed");
            Log.d("location", "longitude: " + longitude);
            Log.d("location", "latitude: " + latitude);
            Looper.myLooper().quit();
            }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}*/

    }
}

