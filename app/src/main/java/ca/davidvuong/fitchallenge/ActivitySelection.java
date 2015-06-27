package ca.davidvuong.fitchallenge;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class ActivitySelection extends ActionBarActivity {

    private FindOthers findTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_selection);
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

    public void doRun(View view){
        findTask = new FindOthers("jason", "run", 100, 100);
        findTask.execute((Void) null);

    }

    public class FindOthers extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(ActivitySelection.this);

        private int longitude;
        private int latitude;
        private String activity;
        private String user;

        FindOthers(String username, String chosenActivity, int mlongitude, int mlatitude)   {
            user = username;
            activity = chosenActivity;
            longitude = mlongitude;
            latitude = mlatitude;
        }

        @Override
        protected void onPreExecute()   {
            this.dialog.setMessage("Wait bitches");
                this.dialog.show();
            }

        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            }   catch (InterruptedException e)  {
                return "ugh";
            }
            return "yay";
        }

        protected void onPostExecute(final String result)   {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Log.d("wtf", "done");
        }

    }
}
