package csci4176.toptentoday;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Downloads and returns json from a url
 */


class JSONDownloadTask extends AsyncTask<URL, Void, JSONObject> {

    interface OnDownloadCompleted{
        void updateList(JSONObject result);
    }

    private OnDownloadCompleted listener;

    public JSONDownloadTask(OnDownloadCompleted listener){
        this.listener = listener;
    }

    protected JSONObject doInBackground(URL... urls) {

        JSONObject js;
        try {
            URL url;
            HttpURLConnection conn;
            url = urls[0];
            conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() != 200) {
                throw new IOException(conn.getResponseMessage());
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            js = new JSONObject(sb.toString());
        } catch (IOException | JSONException e) {
            return null;
        }
        return js;
    }

    protected void onPostExecute(JSONObject result) {
        listener.updateList(result);
    }
}
