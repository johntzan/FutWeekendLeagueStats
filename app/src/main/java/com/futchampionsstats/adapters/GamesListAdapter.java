package com.futchampionsstats.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.futchampionsstats.R;
import com.futchampionsstats.models.Game;

import java.util.ArrayList;

/**
 * Created by yiannitzan on 1/9/17.
 */

public class GamesListAdapter extends RecyclerView.Adapter<GamesListAdapter.ViewHolder> implements Filterable{

    private static final String TAG = GamesListAdapter.class.getSimpleName();
    private ArrayList<Game> data;
    private ArrayList<Game> fullData;
    private Context context;
    private LayoutInflater mInflater; // Stores the layout inflater

    private CustomListAdapterFilter adapterFilter;

    public GamesListAdapter(Context c, ArrayList<Game> d) {
        context = c;
        data = d;
        fullData = data;
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

            holder.game_no.setText("#" + String.valueOf(position+1));
            if(item.getGame_disconnected()!=null && item.getGame_disconnected()){
                holder.game_score.setText("D/C");
            }
            else if(item.getUser_goals()!=null && item.getOpp_goals()!=null){
                if(item.isPenalties()){
                    holder.game_score.setText("(" + item.getUser_pen_score() + ")" + item.getUser_goals() + " : " + item.getOpp_goals() +"(" + item.getOpp_pen_score() + ")");
                }
                else{
                    holder.game_score.setText(item.getUser_goals() + " : " + item.getOpp_goals());
                }
            }

            if(item.getUser_won()!=null){
                if(item.getUser_won()){
                    holder.win_or_lose.setText(R.string.win_txt);
                    holder.win_or_lose.setTextColor(holder.ctx.getResources().getColor(R.color.dark_cyan));
                }
                else{
                    holder.win_or_lose.setText(R.string.loss_txt);
                    holder.win_or_lose.setTextColor(holder.ctx.getResources().getColor(R.color.scarlet));
                }
            }

            String opp_name = (item.getOpp_name()!=null ? item.getOpp_name() : holder.ctx.getResources().getString(R.string.opponent) )
                    +
                    (item.getOpp_team_name()!=null ? "/" + item.getOpp_team_name() : "" );


            holder.opponents_name.setText(opp_name);


        }

    }

    public void setData(ArrayList<Game> newData){
        data = newData;
        notifyDataSetChanged();
    }

    public Game getGameFromPosition(int position){
        return data.get(position);
    }

    @Override
    public Filter getFilter() {
        if (adapterFilter == null) adapterFilter = new CustomListAdapterFilter();
        return adapterFilter;
    }

    // Class enabling the filtering of this adapter
    private class CustomListAdapterFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() >= 1) {
                data = searchGames(constraint);

                if (data != null) {
                    // The API successfully returned results.
                    results.values = data;
                    results.count = data.size();
                }
            } else {
                data = new ArrayList<>();
                results.values = data;
                results.count = data.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            notifyDataSetChanged();

        }
    }

    private ArrayList<Game> searchGames(CharSequence constraint){

        ArrayList<Game> results = new ArrayList<>();
        if(constraint.toString().length()>0){

            String searchString = constraint.toString();
            for (Game game : fullData){

                String oppName = (game.getOpp_name()!=null ? game.getOpp_name() : "")
                        + " " +
                        (game.getOpp_team_name()!=null ? game.getOpp_team_name() : "");

                if(oppName.toLowerCase().contains(searchString.toLowerCase())){
                        results.add(game);
                }

            }
        }
        return results;
    }

    public ArrayList<Game> getData() {
        return data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView game_score;
        TextView opponents_name;
        TextView win_or_lose;
        TextView game_no;
        Context ctx;

        public ViewHolder(View v) {
            super(v);

            game_score = (TextView) v.findViewById(R.id.game_score);
            opponents_name = (TextView) v.findViewById(R.id.opp_txt);
            win_or_lose = (TextView) v.findViewById(R.id.win_or_lose_txt);
            game_no = (TextView) v.findViewById(R.id.game_no);

            ctx = v.getContext();

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

