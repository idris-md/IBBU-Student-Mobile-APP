package com.ibbumobile;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * {@link StaffsAdapter} exposes a list of weather forecasts
 * from a {@link Cursor} to a {@link android.widget.ListView}.
 */
public class StaffsAdapter extends CursorAdapter {

    /**
     * Cache of the children views for a forecast list item.
     */

    public static class ViewHolder {

        //    public final ImageView imageView;
        public final TextView txtName;
        public final TextView txtMat;
        public final TextView txtLevel;
        public final TextView txtMail;


        public ViewHolder(View view) {

            txtName = (TextView) view.findViewById(R.id.txtName);
            txtMat = (TextView) view.findViewById(R.id.txtMat);
            txtLevel = (TextView) view.findViewById(R.id.txtLevel);
            txtMail = (TextView) view.findViewById(R.id.txtMail);

        }
    }

    public StaffsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int layoutId = R.layout.staff_layout;

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String intro = cursor.getString(NewsFragment.COL_NEWS_CONTENT).substring(0, 50);
      //  viewHolder.txtIntro.setText(intro);

        String headLine = cursor.getString(NewsFragment.COL_NEWS_TITLE);
      //  viewHolder.txtHeadLine.setText(headLine);

    }

}