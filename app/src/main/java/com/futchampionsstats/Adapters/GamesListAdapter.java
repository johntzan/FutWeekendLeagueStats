package com.futchampionsstats.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futchampionsstats.R;
import com.futchampionsstats.models.Game;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.R.attr.data;

/**
 * Created by yiannitzan on 1/9/17.
 */

public class GamesListAdapter extends RecyclerView.Adapter<GamesListAdapter.ViewHolder> {

    private static final String TAG = GamesListAdapter.class.getSimpleName();
    private ArrayList<Game> data;
    private Context context;
    private LayoutInflater mInflater; // Stores the layout inflater

    public GamesListAdapter(Context c, ArrayList<Game> d) {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (data.get(position).getClass() == Game.class) {
            Game item = data.get(position);
            if(item.isPenalties()){
                holder.game_score.setText("(" + item.getUser_pen_score() + ")" + item.getUser_goals() + " : " + item.getOpp_goals() +"(" + item.getOpp_pen_score() + ")");
            }
            else{
                holder.game_score.setText(item.getUser_goals() + " : " + item.getOpp_goals());
            }

            if(item.getOpp_name()!=null){
                holder.opponents_name.setText(item.getOpp_name());
            }
            else{
                holder.opponents_name.setText("Opponent");
            }

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView game_score;
        TextView opponents_name;

        public ViewHolder(View v) {
            super(v);

            game_score = (TextView) v.findViewById(R.id.game_score);
            opponents_name = (TextView) v.findViewById(R.id.opp_txt);

        }

    }

    public interface GamesListAdapterListener {
        public void onGameItemClick(int position, Game item);
    }
    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        GestureDetector mGestureDetector;
        private OnItemClickListener mListener;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
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

