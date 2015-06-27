package ca.davidvuong.fitchallenge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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
import android.content.Intent;
import android.widget.Toast;

public class ActivitySelection extends ActionBarActivity {

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
        findTask = new FindOthers(userName, "run");
        findTask.execute(this);
    }

    public class FindOthers extends AsyncTask<Context, Void, String> implements LocationListener {
        private ProgressDialog dialog = new ProgressDialog(ActivitySelection.this);

        private double longitude;
        private double latitude;
        private String activity;
        private String user;
        private LocationManager locationManager;

        FindOthers(String username, String chosenActivity)   {
            user = username;
            activity = chosenActivity;
        }

        @Override
        protected void onPreExecute()   {
            this.dialog.setMessage("Wait bitches");
                this.dialog.show();
            }

        protected String doInBackground(Context... params) {
            locationManager = (LocationManager) params[0].getSystemService(Context.LOCATION_SERVICE);
            Looper.prepare();
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
            Looper.loop();
            return "yay";
        }

        protected void onPostExecute(final String result)   {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Log.d("location", "longitude: " + longitude);
            Log.d("location", "latitude: " + latitude);
            Context context = getApplicationContext();
            Toast.makeText(context, "Location updated", Toast.LENGTH_SHORT);
        }

        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Log.d("location:", "it changed");
            Looper.myLooper().quit();
            }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}

    }
}
