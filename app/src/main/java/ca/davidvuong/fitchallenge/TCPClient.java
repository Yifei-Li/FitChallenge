package ca.davidvuong.fitchallenge;

import android.os.AsyncTask;
import android.util.Log;

import java.io.*;
import java.net.*;

/**
 * Created by David Vuong on 6/27/2015.
 */
public class TCPClient  {
    BufferedReader in;
    PrintWriter out;
    Socket _clientInstance;
    String _ipAddress;
    int _port;
    ListenForMessage t;
    OnTaskCompleted _listener;

    public TCPClient(String ipAddress, int port, OnTaskCompleted listener) {
        _ipAddress = ipAddress;
        _port = port;
        _listener = listener;
    }

    public class ListenForMessage extends AsyncTask<Void, Void, String> {
        String input = "";
        boolean isReady;
        OnTaskCompleted listener;

        public ListenForMessage(OnTaskCompleted t) {
            listener = t;
        }

        @Override
        protected String doInBackground(Void[] params) {
            try {

                while (true) {

                    isReady = in.ready();
                    if (isReady == true){
                        input = in.readLine();
                        Log.d("TEST", input);
                        return input;
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String param) {
            listener.processFinish(param);
        }
    }

    public void connectToServer() throws IOException {
        _clientInstance = new Socket(_ipAddress, _port);
        in = new BufferedReader( new InputStreamReader(_clientInstance.getInputStream()));

        t = new ListenForMessage(_listener);
        t.execute();

        out = new PrintWriter(_clientInstance.getOutputStream(), true);
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }
}
