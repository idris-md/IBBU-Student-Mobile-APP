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

import com.ibbumobile.model.News;

import java.util.ArrayList;


class CustomNewsAdapter extends BaseAdapter {

    Context context;
    ArrayList<News> mNewsList;

    public CustomNewsAdapter(@NonNull Context context, ArrayList<News> newsList) {
        this.context = context;
        this.mNewsList = newsList;
    }

    @Override
    public int getCount() {
        return mNewsList.size();
    }

    @Override
    public News getItem(int position) {
        return mNewsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mNewsList.indexOf(getItem(position));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                    Activity.LAYOUT_INFLATER_SERVICE
            );

            convertView = layoutInflater.inflate(R.layout.list_item_news, null);

        }

        News news = mNewsList.get(position);

        ImageView imgView = convertView.findViewById(R.id.imageView);
        //TextView readmore = convertView.findViewById(R.id.readmore);
        TextView headline = convertView.findViewById(R.id.txtIntro);

        //imgView.setImageBitmap(Utils.getBitmapFromAsset(context, std.getMatNum().replaceAll("/", "") + "0", Utils.convertDPtoPx(60), Utils.convertDPtoPx(60)));

       // imgView.setImageBitmap(news.getBitmap());
        headline.setText(news.getHeadline());


        return convertView;
    }
}
