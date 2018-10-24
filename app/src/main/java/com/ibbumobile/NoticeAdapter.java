package com.ibbumobile;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class NoticeAdapter extends CursorAdapter {

    public static class ViewHolder {

        public final TextView noticeTitle;
        public final TextView txtContent;
        public final TextView txtDate;

        public ViewHolder(View view) {

            noticeTitle =  view.findViewById(R.id.noticeTitle);
            txtContent =  view.findViewById(R.id.txtContent);
            txtDate =  view.findViewById(R.id.txtDate);

        }
    }

    public NoticeAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int layoutId = R.layout.list_item_notice;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String content = cursor.getString(NoticeFragment.COL_NOTICE_CONTENT);
        viewHolder.txtContent.setText(content);

        String date = cursor.getString(NoticeFragment.COL_NOTICE_DATE);
        viewHolder.txtDate.setText(date);

        String title = cursor.getString(NoticeFragment.COL_NOTICE_AUTHOR);
        viewHolder.noticeTitle.setText(title);

    }

}