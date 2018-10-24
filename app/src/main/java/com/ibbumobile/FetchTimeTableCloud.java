package com.ibbumobile;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.ibbumobile.model.TimeTable;

public class FetchTimeTableCloud extends AsyncTask<Void, Void, Void> {

    String data = "";

    @Override
    protected Void doInBackground(Void... voids) {

        try {

            URL url = new URL("htpp://");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";

            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String code = jsonObject.get("c_Cod").toString();
                String day = jsonObject.get("day").toString();
                int time = Integer.parseInt(jsonObject.get("time").toString());
                int per = Integer.parseInt(jsonObject.get("per").toString());
                String lecturer = jsonObject.get("staff").toString();
                String venue = jsonObject.get("room").toString();

                TimeTable timeTable = new TimeTable(code, day, time, per, lecturer,venue);


            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
