package com.futchampionsstats.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.futchampionsstats.R;
import com.futchampionsstats.main.MySquadsFragment;
import com.futchampionsstats.models.Squad;

import java.util.ArrayList;


/**
 * Created by yiannitzan on 2/8/17.
 */

public class SquadListAdapter extends RecyclerView.Adapter<SquadListAdapter.ViewHolder> {

    private static final String TAG = SquadListAdapter.class.getSimpleName();
    private ArrayList<Squad> data;
    private Context context;
    private LayoutInflater mInflater; // Stores the layout inflater
    private MySquadsFragment.OnItemTouchListener onItemTouchListener;

    public SquadListAdapter(Context c, ArrayList<Squad> d, MySquadsFragment.OnItemTouchListener onItemTouchListener) {
        context = c;
        data = d;
        this.onItemTouchListener = onItemTouchListener;
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

    public void updateList(ArrayList<Squad> update_data) {
        data = update_data;
        notifyDataSetChanged();
    }

    public void removeFromList(int position){
        data.remove(position);
        notifyDataSetChanged();
        notifyItemRemoved(position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.squad_row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (data.get(position).getClass() == Squad.class) {

            Squad squad = data.get(position);
            if(squad.getName()!=null){
                holder.squad_name.setText(squad.getName());
            }
            if(squad.getFormation()!=null){
                holder.formation.setText(squad.getFormation());
            }
            if(squad.getTeam_rating()!=null){
                holder.team_rating.setText(squad.getTeam_rating());
            }

            holder.squad_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemTouchListener.onEditClick(view, holder.getAdapterPosition());
                }
            });

            //set show mode.
            holder.swipe.setShowMode(SwipeLayout.ShowMode.PullOut);

            //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
            holder.swipe.addDrag(SwipeLayout.DragEdge.Left, holder.afterSwipeLayout);

            holder.swipe.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.

                }

                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    //when the BottomView totally show.
                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    //when user's hand released.
                    holder.delete_squad_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onItemTouchListener.onDeleteClick(view, holder.getAdapterPosition());
                        }
                    });
                }
            });

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView squad_name;
        TextView formation;
        TextView team_rating;
        Context ctx;
        SwipeLayout swipe;
        RelativeLayout afterSwipeLayout;
        Button delete_squad_btn;
        RelativeLayout squad_rl;



        public ViewHolder(View v) {
            super(v);

            squad_name = (TextView) v.findViewById(R.id.squad_name);
            formation = (TextView) v.findViewById(R.id.squad_formation);
            team_rating = (TextView) v.findViewById(R.id.squad_team_rating);
            ctx = v.getContext();
            swipe  = (SwipeLayout) v.findViewById(R.id.swipe);
            afterSwipeLayout = (RelativeLayout) v.findViewById(R.id.delete_squad_rl);
            squad_rl = (RelativeLayout) v.findViewById(R.id.squad_rl);
            delete_squad_btn = (Button) v.findViewById(R.id.delete_squad_btn);

        }

    }

}
