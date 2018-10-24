package com.ibbumobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MemberFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int NOTICE_LOADER = 9374;
    TextView txtDay;
    TextView txtDate;
    TextView txtNotice;
    TextView examCont;

    long diffInSec;

    private static final String[] NOTICE_COLUMNS = {
            DataContract.NoticeEntry.COLUMN_ID,
            DataContract.NoticeEntry.COLUMN_DATE,
            DataContract.NoticeEntry.COLUMN_AUTHOR,
            DataContract.NoticeEntry.COLUMN_CONTENT
    };

    static final int COL_NOTICE_ID = 0;
    static final int COL_NOTICE_DATE = 1;
    static final int COL_NOTICE_AUTHOR = 2;
    static final int COL_NOTICE_CONTENT = 3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        displayExamDayCount();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_member, null);

        LinearLayout timetable = view.findViewById(R.id.timetable);
        timetable.setOnClickListener(this);
        LinearLayout campusmap = view.findViewById(R.id.campus_map);
        campusmap.setOnClickListener(this);
        LinearLayout noticeboard = view.findViewById(R.id.forecast);
        noticeboard.setOnClickListener(this);
        LinearLayout people = view.findViewById(R.id.people);
        people.setOnClickListener(this);
        LinearLayout report = view.findViewById(R.id.report);
        report.setOnClickListener(this);
        LinearLayout news = view.findViewById(R.id.news);
        news.setOnClickListener(this);
        LinearLayout gpa = view.findViewById(R.id.gpa);
        gpa.setOnClickListener(this);
        LinearLayout profile = view.findViewById(R.id.profile);
        profile.setOnClickListener(this);
        LinearLayout photo = view.findViewById(R.id.photo);
        photo.setOnClickListener(this);

        txtDate = view.findViewById(R.id.txtDate);
        txtDay = view.findViewById(R.id.txtDay);
        txtNotice = view.findViewById(R.id.txtNotice);
        examCont = view.findViewById(R.id.examDayCount);

        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String today = calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR);
        txtDate.setText(today);

        examCont.setText(diffInSec + "");


        switch (day) {

            case Calendar.MONDAY:
                txtDay.setText("Monday");
                break;
            case Calendar.TUESDAY:
                txtDay.setText("Tuesday");
                break;
            case Calendar.WEDNESDAY:
                txtDay.setText("Wednesday");
                break;
            case Calendar.THURSDAY:
                txtDay.setText("Thursday");
                break;
            case Calendar.FRIDAY:
                txtDay.setText("Friday");
                break;
            case Calendar.SATURDAY:
                txtDay.setText("Saturday");
                break;
            case Calendar.SUNDAY:
                txtDay.setText("Sunday");
                break;

            default:
                break;

        }

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.news:
                Intent newsIntent = new Intent(getContext(), NewsInfo.class);
                startActivity(newsIntent);
                break;
            case R.id.timetable:
                Intent timeTIntent = new Intent(getContext(), TimeTableActivity.class);
                startActivity(timeTIntent);
                break;
            case R.id.campus_map:
                Intent mapIntent = new Intent(getContext(), MapActivity.class);
                startActivity(mapIntent);
                break;
            case R.id.report:
                Intent reportIntent = new Intent(getContext(), ReportActivity.class);
                startActivity(reportIntent);
                break;
            case R.id.forecast:
                Intent noticeIntent = new Intent(getContext(), ForecastActivity.class);
                startActivity(noticeIntent);
                break;
            case R.id.people:
                Intent peopleIntent = new Intent(getContext(), PeopleActivity.class);
                startActivity(peopleIntent);
                break;
            case R.id.gpa:
                Intent gpaIntent = new Intent(getContext(), calculator.class);
                startActivity(gpaIntent);
                break;
            case R.id.photo:
                Intent photoIntent = new Intent(getContext(), PhotosActivity.class);
                startActivity(photoIntent);
                break;
            case R.id.profile:
                Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                startActivity(profileIntent);
                break;

        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(NOTICE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String sortOrder = DataContract.NoticeEntry.COLUMN_DATE + " DESC LIMIT 1";

        return new CursorLoader(getActivity(),
                DataContract.NoticeEntry.CONTENT_URI,
                NOTICE_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            String content = data.getString(MemberFragment.COL_NOTICE_CONTENT);
            txtNotice.setText(content);
        } else {
            txtNotice.setText("No Content Found");

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    void displayExamDayCount() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String examStart = sharedPreferences.getString("examStart", null);

        if (examStart != null) {

            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {

                Calendar today = Calendar.getInstance();

                Date startDate = outputFormat.parse(today.get(Calendar.YEAR) + 1 + "-" + today.get(Calendar.MONTH) + "-" + today.get(Calendar.DAY_OF_MONTH));
                Date endDate = outputFormat.parse("2018-09-12");

                long diffInMs = startDate.getTime() - endDate.getTime();

                 diffInSec = TimeUnit.MILLISECONDS.toDays(diffInMs);

                Toast.makeText(getContext(), diffInMs + "", Toast.LENGTH_SHORT).show();

            } catch (ParseException e) {
                e.printStackTrace();
            }

//            String count = String.valueOf(today.until(examDate, ChronoUnit.DAYS));
//            ChronoUnit.DAYS.between(today, examDate);
        } else {

        }


    }

}
