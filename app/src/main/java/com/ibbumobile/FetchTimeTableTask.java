package com.ibbumobile;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ibbumobile.model.TimeTable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Eidris on 3/16/2018.
 */

public class FetchTimeTableTask extends AsyncTask<ListView, Void, ListAdapter> {

    ListAdapter mAdapter;
    WeakReference<ListView> mWeakListViewReference;
    Context mContext;
    FrameLayout mLayoutLoading;
    FrameLayout mLayoutNothing;
    private String mDay;

    FetchTimeTableTask(Context context, FrameLayout loading, FrameLayout nothing,String day) {

        this.mContext = context;
        this.mLayoutLoading = loading;
        this.mDay = day;
        this.mLayoutNothing = nothing;
    }

    @Override
    protected void onPreExecute() {
        mLayoutNothing.setVisibility(View.GONE);
        mLayoutLoading.setVisibility(View.VISIBLE);
    }


    @Override
    protected ListAdapter doInBackground(ListView... params) {

        if (params == null || params.length == 0) {
            return null;
        }

        ListView listView = (ListView) params[0];
        mWeakListViewReference = new WeakReference<ListView>(listView);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(mContext);
        databaseAccess.open();
        final ArrayList<TimeTable> nList = new ArrayList<>(databaseAccess.getTimeTables(mDay));
        databaseAccess.close();

        mAdapter = new TimeTableAdapter(mContext, nList);

        return mAdapter;
    }

    @Override
    protected void onPostExecute(ListAdapter listAdapter) {

        ListView listView = mWeakListViewReference.get();

        if (listAdapter.isEmpty()) {
            mLayoutLoading.setVisibility(View.GONE);
            mLayoutNothing.setVisibility(View.VISIBLE);
        }else {
            listView.setAdapter(listAdapter);
            mLayoutLoading.setVisibility(View.GONE);
            mLayoutNothing.setVisibility(View.GONE);
        }

    }


}
