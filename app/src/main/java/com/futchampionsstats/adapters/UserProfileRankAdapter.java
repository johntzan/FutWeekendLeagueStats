package com.futchampionsstats.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futchampionsstats.R;
import com.futchampionsstats.models.leaderboards.User;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by yiannitzan on 5/16/17.
 */

public class UserProfileRankAdapter extends RecyclerView.Adapter<UserProfileRankAdapter.ViewHolder> {

    List<User.Month> mData;

    public UserProfileRankAdapter(List<User.Month> d) {
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
    public UserProfileRankAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile_rank_layout, parent, false);
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

        Gson gson = new Gson();
        Log.d("UserRanks", "onBindViewHolder: " + gson.toJson(mData.get(position)));

        if(mData.get(position)!=null && mData.get(position).getMonth().size()==5){
            holder.month.setText(mData.get(position).getMonth().get(0));
            holder.overall.setText(mData.get(position).getMonth().get(1));
            holder.region.setText(mData.get(position).getMonth().get(2));
            holder.wins.setText(mData.get(position).getMonth().get(3).replaceAll("\\(.*\\)", "").trim());
            holder.skill.setText(mData.get(position).getMonth().get(4).replaceAll("\\(.*\\)", "").trim());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView month;
        TextView overall;
        TextView region;
        TextView wins;
        TextView skill;
        LinearLayout item_background;

        public ViewHolder(View v) {
            super(v);

            month = (TextView) v.findViewById(R.id.month);
            overall= (TextView) v.findViewById(R.id.overall_rank);
            region = (TextView) v.findViewById(R.id.region_rank);
            wins = (TextView) v.findViewById(R.id.wins);
            skill = (TextView) v.findViewById(R.id.skill);
            item_background = (LinearLayout) v.findViewById(R.id.user_rank_item_layout);

        }
    }
}
