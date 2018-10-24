package com.ibbumobile;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * {@link StudentsAdapter} exposes a list of weather forecasts
 * from a {@link Cursor} to a {@link android.widget.ListView}.
 */
public class StudentsAdapter extends CursorAdapter {

    /**
     * Cache of the children views for a forecast list item.
     */

    public static class ViewHolder {

        //    public final ImageView imageView;
        public final TextView txtName;
        public final TextView txtMat;
        public final TextView txtMail;
        public final TextView txtLevel;


        public ViewHolder(View view) {

            txtName = (TextView) view.findViewById(R.id.txtName);
            txtMat = (TextView) view.findViewById(R.id.txtMat);
            txtMail = (TextView) view.findViewById(R.id.txtMail);
            txtLevel = (TextView) view.findViewById(R.id.txtLevel);


        }
    }

    public StudentsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int layoutId = R.layout.student_layout;

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String name = cursor.getString(StudentsFragment.COL_STUDENT_FIRSTNAME);
        String mail = cursor.getString(StudentsFragment.COL_STUDENT_MATRIC);
        String mat = cursor.getString(StudentsFragment.COL_STUDENT_EMAIL);
        String level = cursor.getString(StudentsFragment.COL_STUDENT_LEVEL);

        viewHolder.txtName.setText(name);
        viewHolder.txtLevel.setText(level);
        viewHolder.txtMat.setText(mail);
        viewHolder.txtMail.setText(mat);


    }

}