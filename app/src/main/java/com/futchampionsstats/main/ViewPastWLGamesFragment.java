package com.futchampionsstats.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futchampionsstats.R;
import com.futchampionsstats.Utils.Constants;
import com.futchampionsstats.adapters.GamesListAdapter;
import com.futchampionsstats.databinding.FragmentViewPastWlGamesBinding;
import com.futchampionsstats.models.WeekendLeague;

public class ViewPastWLGamesFragment extends Fragment {


    private OnViewPastWLGamesFragmentInteractionListener mListener;
    public static final String TAG = ViewPastWLGamesFragment.class.getSimpleName();

    FragmentViewPastWlGamesBinding binding;

    private WeekendLeague weekendLeague;
    private GamesListAdapter mAdapter;
    private GamesListAdapter.RecyclerItemClickListener listener;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView gamesList;

    public ViewPastWLGamesFragment() {
        // Required empty public constructor
    }

    public static ViewPastWLGamesFragment newInstance(String param1, String param2) {
        ViewPastWLGamesFragment fragment = new ViewPastWLGamesFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weekendLeague = (WeekendLeague) getArguments().getSerializable(Constants.VIEW_PAST_WL_GAMES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_past_wl_games, container, false);
        ViewPastWLsGamesHandlers handlers = new ViewPastWLsGamesHandlers();
        binding.setHandlers(handlers);

        gamesList = (RecyclerView) binding.getRoot().findViewById(R.id.wl_games_list);
        binding.setWeekendLeague(weekendLeague);
        setupAdapter();

        return binding.getRoot();
    }

    private void setupAdapter(){
        mLayoutManager = new LinearLayoutManager(getActivity());

        gamesList.setLayoutManager(mLayoutManager);
        gamesList.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new GamesListAdapter(getActivity(), weekendLeague.getWeekendLeague());
        gamesList.setAdapter(mAdapter);
        setRecyclerAssignmentListener();
    }

    public class ViewPastWLsGamesHandlers{

        public void onClick(View view){

            Bundle b = new Bundle();
            switch(view.getId()) {
                case R.id.back_btn:
                    b.putString(Constants.BACK_BTN, Constants.BACK_BTN);
                    break;
            }
            if(mListener!=null) mListener.onViewPastWLGamesFragmentInteraction(b);
        }
    }

    private void setRecyclerAssignmentListener() {

        GamesListAdapter.RecyclerItemClickListener.OnItemClickListener itemClickListener = new GamesListAdapter.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "recyclerview onItemClick: " + position);
                Bundle b = new Bundle();
                b.putSerializable(Constants.VIEW_PAST_WL_GAME, weekendLeague.getWeekendLeague().get(position));
                if(mListener!=null) mListener.onViewPastWLGamesFragmentInteraction(b);
            }
        };

        listener = new GamesListAdapter.RecyclerItemClickListener(getActivity(), itemClickListener);
        gamesList.addOnItemTouchListener(listener);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnViewPastWLGamesFragmentInteractionListener) {
            mListener = (OnViewPastWLGamesFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnViewPastWLGamesFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnViewPastWLGamesFragmentInteractionListener {
        void onViewPastWLGamesFragmentInteraction(Bundle args);
    }
}
