package com.ibbumobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class GuestFragment extends Fragment implements View.OnClickListener {

    TextView txtDay;
    TextView txtDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_guest, null);

//        LinearLayout timetable = view.findViewById(R.id.timetable);
//        timetable.setOnClickListener(this);
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

        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String today = calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR);
        txtDate.setText(today);
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
//            case R.id.timetable:
//                Intent timeTIntent = new Intent(getContext(), TimeTableActivity.class);
//                startActivity(timeTIntent);
//                break;
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
}
