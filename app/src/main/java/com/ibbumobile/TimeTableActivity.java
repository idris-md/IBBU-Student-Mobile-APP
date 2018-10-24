package com.ibbumobile;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Calendar;


public class TimeTableActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout mon, tue, wed, thu, fri;
    ListView mListView;
    FrameLayout mLayout;
    FrameLayout mLayoutNoLec;
    Dialog editDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mListView = findViewById(R.id.listview);

        mon = findViewById(R.id.monday);
        mon.setOnClickListener(this);
        tue = findViewById(R.id.tuesday);
        tue.setOnClickListener(this);
        wed = findViewById(R.id.wednesday);
        wed.setOnClickListener(this);
        thu = findViewById(R.id.thursday);
        thu.setOnClickListener(this);
        fri = findViewById(R.id.friday);
        fri.setOnClickListener(this);
        editDialog = new Dialog(this);


        mLayout = findViewById(R.id.frameHeader);
        mLayoutNoLec = findViewById(R.id.frameNoLec);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {

            case Calendar.MONDAY:
                activateDay(mon);
                break;

            case Calendar.TUESDAY:
                activateDay(tue);
                break;
            case Calendar.WEDNESDAY:
                activateDay(wed);
                break;
            case Calendar.THURSDAY:
                activateDay(thu);
                break;
            case Calendar.FRIDAY:
                activateDay(fri);
                break;
        }


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timetable, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.timetable_test:

                break;

            case R.id.timetable_home_work:

                break;
            case R.id.timetable_setting:

                break;
            case R.id.timetable_about:

                break;
            case R.id.edit:

                showEditT();

                break;

            default:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showEditT() {

        editDialog.setContentView(R.layout.edit_timetable);

        Button add = editDialog.findViewById(R.id.add);
        Button remove = editDialog.findViewById(R.id.remove);
        Button update = editDialog.findViewById(R.id.update);

        final LinearLayout selLayout = editDialog.findViewById(R.id.selLayout);

        final LinearLayout addLayout = editDialog.findViewById(R.id.addLayout);
        final LinearLayout removeLayout = editDialog.findViewById(R.id.removeLayout);
        final LinearLayout updateLayout = editDialog.findViewById(R.id.updateLayout);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selLayout.setVisibility(View.GONE);
                addLayout.setVisibility(View.VISIBLE);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selLayout.setVisibility(View.GONE);
                removeLayout.setVisibility(View.VISIBLE);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selLayout.setVisibility(View.GONE);
                updateLayout.setVisibility(View.VISIBLE);
            }
        });


        editDialog.setCancelable(true);
        editDialog.setCanceledOnTouchOutside(true);
        editDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editDialog.show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.monday:
                activateDay(mon);
                break;
            case R.id.tuesday:
                activateDay(tue);
                break;
            case R.id.wednesday:
                activateDay(wed);
                break;
            case R.id.thursday:
                activateDay(thu);
                break;
            case R.id.friday:
                activateDay(fri);
                break;
            default:
                break;
        }
    }

    private void activateDay(View view) {

        findViewById(R.id.monActive).setVisibility(View.INVISIBLE);
        findViewById(R.id.tueActive).setVisibility(View.INVISIBLE);
        findViewById(R.id.wedActive).setVisibility(View.INVISIBLE);
        findViewById(R.id.thuActive).setVisibility(View.INVISIBLE);
        findViewById(R.id.friActive).setVisibility(View.INVISIBLE);

        FetchTimeTableTask fetchTimeTableTask;

        switch (view.getId()) {

            case R.id.monday:

                fetchTimeTableTask = new FetchTimeTableTask(this, mLayout, mLayoutNoLec, "MON");
                fetchTimeTableTask.execute(mListView);
                findViewById(R.id.monActive).setVisibility(View.VISIBLE);

                break;
            case R.id.tuesday:

                fetchTimeTableTask = new FetchTimeTableTask(this, mLayout, mLayoutNoLec, "TUE");
                fetchTimeTableTask.execute(mListView);
                findViewById(R.id.tueActive).setVisibility(View.VISIBLE);
                break;
            case R.id.wednesday:

                fetchTimeTableTask = new FetchTimeTableTask(this, mLayout, mLayoutNoLec, "WED");
                fetchTimeTableTask.execute(mListView);
                findViewById(R.id.wedActive).setVisibility(View.VISIBLE);
                break;
            case R.id.thursday:

                fetchTimeTableTask = new FetchTimeTableTask(this, mLayout, mLayoutNoLec, "THUR");
                fetchTimeTableTask.execute(mListView);
                findViewById(R.id.thuActive).setVisibility(View.VISIBLE);
                break;
            case R.id.friday:

                fetchTimeTableTask = new FetchTimeTableTask(this, mLayout, mLayoutNoLec, "FRI");
                fetchTimeTableTask.execute(mListView);
                findViewById(R.id.friActive).setVisibility(View.VISIBLE);
                break;
            default:
                break;

        }
    }


}
