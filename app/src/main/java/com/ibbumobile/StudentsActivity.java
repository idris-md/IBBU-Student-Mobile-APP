package com.ibbumobile;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.ibbumobile.model.Student;

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
import java.util.List;

public class StudentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

//    class Task extends AsyncTask<ListView, Void, ListAdapter> {
//
//
//        String data;
//
//        public Task(Context context) {
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected ListAdapter doInBackground(ListView... listViews) {
//            try {
//
//                List<Student> students = null;
//
//                URL url = new URL("");
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                InputStream inputStream = urlConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//                String line = "";
//
//                while (line != null) {
//                    line = bufferedReader.readLine();
//                    data = data + line;
//                }
//
//                /////////////////// Convert From JSON to Java Object
//                JSONObject jsonObject = new JSONObject(data);
//                JSONArray jsonArray = jsonObject.getJSONArray("students");
//
//                for (int i = 0; i < jsonArray.length(); i++) {
//
//                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//
//                    final String firstName = jsonObject.getString("firstName");
//                    final String lastName = jsonObject.getString("lastName");
//                    final String otherName = jsonObject.getString("otherName");
//                    final String matNum = jsonObject.getString("matNum");
//                    final String dept = jsonObject.getString("dept");
//                    final String gender = jsonObject.getString("gender");
//                    final String email = jsonObject.getString("email");
//                    final String level = jsonObject.getString("level");
//
//                    students.add(new Student(firstName, lastName, otherName, matNum, dept, gender, email,level));
//
//                }
//
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(ListAdapter listAdapter) {
//            super.onPostExecute(listAdapter);
//        }
//
//    }
}
