package com.futchampionsstats.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futchampionsstats.R;

import java.util.List;

/**
 * Created by yiannitzan on 5/4/17.
 */

public class LeaderboardsAdapter extends RecyclerView.Adapter<LeaderboardsAdapter.ViewHolder> {

    List<String[]> mData;

    public LeaderboardsAdapter(List<String[]> d) {
        mData = d;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public LeaderboardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_row_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(position%2==0){
            holder.item_background.setBackgroundColor(holder.item_background.getContext().getResources().getColor(R.color.white));
        }
        else{
            holder.item_background.setBackgroundColor(holder.item_background.getContext().getResources().getColor(R.color.light_grey));
        }
        String[] dataPosition = mData.get(position);
        holder.rank.setText(dataPosition[0]);
        holder.username.setText(dataPosition[1]);
        holder.wins.setText(dataPosition[2]);
        holder.skill.setText(dataPosition[3]);
        holder.region.setText(dataPosition[5]);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView rank;
        TextView username;
        TextView wins;
        TextView skill;
        TextView region;
        LinearLayout item_background;

        public ViewHolder(View v) {
            super(v);

            rank = (TextView) v.findViewById(R.id.rank);
            username = (TextView) v.findViewById(R.id.username);
            wins = (TextView) v.findViewById(R.id.wins);
            skill = (TextView) v.findViewById(R.id.skill);
            region = (TextView) v.findViewById(R.id.region);
            item_background = (LinearLayout) v.findViewById(R.id.item_background);
        }
    }
}
