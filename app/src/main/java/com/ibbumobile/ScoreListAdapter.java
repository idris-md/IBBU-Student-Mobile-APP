package com.ibbumobile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout;

import java.util.List;

public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.MyViewHolder> {
    private Context context;
    private List<Score> scoreList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView code, unit, grade;
        public ImageView thumbnail;
        public RelativeLayout viewBackground;
        public LinearLayout viewForeground;

        public MyViewHolder(View view) {
            super(view);
            code = view.findViewById(R.id.code);
            unit = view.findViewById(R.id.unit);
            grade = view.findViewById(R.id.grade);
            thumbnail = view.findViewById(R.id.thumbnail);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }

    }

    public ScoreListAdapter(Context context, List<Score> cartList) {
        this.context = context;
        this.scoreList = cartList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_score, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Score item = scoreList.get(position);
        holder.code.setText(item.getCourse());
        holder.unit.setText(String.valueOf(item.getLoad()) + " unit(s)");

        holder.grade.setText("Grade: " + item.getGrade());

        String cCode = item.getCourse().substring(0, 3);

        holder.thumbnail.setImageResource(getIConID(cCode));

//        Glide.with(context)
//                .load(item.getThumbnail())
//                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    public Score getItem(int position) {
        return scoreList.get(position);
    }

    public void removeItem(int position) {
        scoreList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Score item, int position) {
        scoreList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    private int getIConID(String course) {

        int courseIconID = context.getResources().getIdentifier(course.toLowerCase(), "drawable", "com.ibbumobile");

        return courseIconID;

    }
}