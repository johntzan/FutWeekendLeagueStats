package com.futchampionsstats.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.futchampionsstats.R;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.WeekendLeague;
import com.google.gson.Gson;

import java.util.ArrayList;


/**
 * Created by yiannitzan on 2/17/17.
 */

public class PastWlsListAdapter extends RecyclerView.Adapter<PastWlsListAdapter.ViewHolder> {

    private static final String TAG = GamesListAdapter.class.getSimpleName();
    private ArrayList<WeekendLeague> data;
    private Context context;
    private LayoutInflater mInflater; // Stores the layout inflater

    public PastWlsListAdapter(Context c, ArrayList<WeekendLeague> d) {
        context = c;
        data = d;
        // Stores inflater for use later
        if (context != null) mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public PastWlsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_wls_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        PastWlsListAdapter.ViewHolder vh = new PastWlsListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final PastWlsListAdapter.ViewHolder holder, final int position) {
        if (data.get(position).getClass() == WeekendLeague.class) {
            WeekendLeague item = data.get(position);
            Log.d(TAG, "onBindViewHolder: " + new Gson().toJson(item));
            if(item!=null && item.getWeekendLeague()!=null){
                holder.wl_date.setText(item.getDateOfWL());
                holder.games_played.setText(WeekendLeague.getTotalGamesPlayed(item));
                holder.games_won.setText(WeekendLeague.getWinTotal(item));
                holder.total_gs.setText(WeekendLeague.getTotalGoals(item)[0]);
                holder.total_gc.setText(WeekendLeague.getTotalGoals(item)[1]);
                holder.g_ratio.setText(WeekendLeague.getAvgGoalPerShotString(item));

            }


        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView wl_date;
        TextView games_played;
        TextView games_won;
        TextView total_gs;
        TextView total_gc;
        TextView g_ratio;

        Context ctx;

        public ViewHolder(View v) {
            super(v);

            wl_date = (TextView) v.findViewById(R.id.past_wl_date);
            games_played = (TextView) v.findViewById(R.id.past_wl_gp);
            games_won = (TextView) v.findViewById(R.id.past_wl_gw);
            total_gs = (TextView) v.findViewById(R.id.past_wl_total_gs);
            total_gc = (TextView) v.findViewById(R.id.past_wl_total_gc);
            g_ratio = (TextView) v.findViewById(R.id.past_wl_g_ratio);

            ctx = v.getContext();

        }

    }

    public interface PastWlsListAdapterListener {
        public void onPastWlItemClick(int position, Game item);
    }
    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        GestureDetector mGestureDetector;
        private PastWlsListAdapter.RecyclerItemClickListener.OnItemClickListener mListener;

        public RecyclerItemClickListener(Context context, PastWlsListAdapter.RecyclerItemClickListener.OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

        public interface OnItemClickListener {
            void onItemClick(View view, int position);
        }
    }
}
