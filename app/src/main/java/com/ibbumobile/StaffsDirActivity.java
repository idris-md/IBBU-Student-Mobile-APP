package com.ibbumobile;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

public class StaffsDirActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staffs_dir);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    class Task extends AsyncTask<ListView, Void, ListAdapter> {

        public Task(Context context) {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ListAdapter doInBackground(ListView... listViews) {
            return null;
        }

        @Override
        protected void onPostExecute(ListAdapter listAdapter) {
            super.onPostExecute(listAdapter);
        }


    }


}
