package com.ibbumobile;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * {@link NewsAdapter} exposes a list of weather forecasts
 * from a {@link Cursor} to a {@link android.widget.ListView}.
 */
public class NewsAdapter extends CursorAdapter {

    /**
     * Cache of the children views for a forecast list item.
     */
    Context mContext;

    public static class ViewHolder {

        //    public final ImageView imageView;
        //    public final TextView txtHeadLine;
        public final TextView txtIntro;
        public final ImageView imageView;


        public ViewHolder(View view) {
            //       imageView = (ImageView) view.findViewById(R.id.imageView);
            //   txtHeadLine = (TextView) view.findViewById(R.id.txtHeadLine);
            txtIntro = (TextView) view.findViewById(R.id.txtIntro);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }

    public NewsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int layoutId = R.layout.list_item_news;
        mContext = context;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String intro = cursor.getString(NewsFragment.COL_NEWS_CONTENT).substring(0, 50);
        viewHolder.txtIntro.setText(intro);

        String headLine = cursor.getString(NewsFragment.COL_NEWS_TITLE);
        viewHolder.txtIntro.setText(headLine);

        String photo = cursor.getString(NewsFragment.COL_NEWS_PHOTO);

        viewHolder.imageView.setImageResource(getIConID(photo));

        viewHolder.txtIntro.setText(headLine);

    }

    private int getIConID(String course) {

        int courseIconID = mContext.getResources().getIdentifier(course.toLowerCase(), "drawable", "com.ibbumobile");

        return courseIconID;

    }

}