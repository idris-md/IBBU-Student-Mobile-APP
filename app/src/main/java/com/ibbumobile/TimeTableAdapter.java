package com.ibbumobile;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibbumobile.model.TimeTable;

import java.util.ArrayList;


class TimeTableAdapter extends BaseAdapter {

    Context context;
    ArrayList<TimeTable> mTimeTables;

    public TimeTableAdapter(@NonNull Context context, ArrayList<TimeTable> timeTables) {
        this.context = context;
        this.mTimeTables = timeTables;
    }

    @Override
    public int getCount() {
        return mTimeTables.size();
    }

    @Override
    public TimeTable getItem(int position) {
        return mTimeTables.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mTimeTables.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                    Activity.LAYOUT_INFLATER_SERVICE
            );

            convertView = layoutInflater.inflate(R.layout.list_item_timetable, null);

        }

        TimeTable timeTable = mTimeTables.get(position);

        ImageView imgView = convertView.findViewById(R.id.imageViewT);
        TextView txtRoom = convertView.findViewById(R.id.room);
        TextView txtLecturer = convertView.findViewById(R.id.lecturer);
        TextView txtTime = convertView.findViewById(R.id.time);
        TextView txtCode = convertView.findViewById(R.id.code);

        String cCode = timeTable.getCourseCode().substring(0, 3);

        //Drawable drawable = getIConID(cCode);

        imgView.setImageResource(getIConID(cCode));
        txtRoom.setText(timeTable.getRoom());
        txtLecturer.setText(timeTable.getTeacherName());

        txtCode.setText(timeTable.getCourseCode());
        txtTime.setText("" + timeTable.getTime() + ":00 AM");

        return convertView;
    }

    private int getIConID(String course) {

        int courseIconID = context.getResources().getIdentifier(course.toLowerCase(), "drawable", "com.ibbumobile");

        return courseIconID;

    }

}
