package com.futchampionsstats.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.futchampionsstats.databinding.FragmentGamesListBinding;
import com.futchampionsstats.models.WeekendLeague;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ViewGamesFragment extends Fragment {

    public static final String TAG = ViewGamesFragment.class.getSimpleName();

    private OnViewGamesFragmentInteractionListener mListener;
    private WeekendLeague weekendLeague;
    private GamesListAdapter mAdapter;
    private GamesListAdapter.RecyclerItemClickListener listener;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView gamesList;

    FragmentGamesListBinding binding;

    public ViewGamesFragment() {
    }

    public static ViewGamesFragment newInstance() {
        ViewGamesFragment fragment = new ViewGamesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
//            weekendLeague = (WeekendLeague) getArguments().getSerializable(Constants.VIEW_GAMES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_games_list, container, false);
        ViewGamesHandlers handlers = new ViewGamesHandlers();
        binding.setHandlers(handlers);
//        binding.setWeekendLeague(weekendLeague);

        gamesList = (RecyclerView) binding.getRoot().findViewById(R.id.games_list);

//        if(weekendLeague.getWeekendLeague()!=null){
//            Log.d(TAG, "onCreateView: weekend league not null: " + weekendLeague.getWeekendLeague().size());
//
//        }

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

    public class ViewGamesHandlers{

        public void onClick(View view){

            Bundle b = new Bundle();
            switch(view.getId()) {
                case R.id.back_btn:
                    b.putString(Constants.BACK_BTN, Constants.BACK_BTN);
                    break;
            }
            if(mListener!=null) mListener.OnViewGamesFragmentInteractionListenerInteraction(b);
        }
    }

    private void setRecyclerAssignmentListener() {

        GamesListAdapter.RecyclerItemClickListener.OnItemClickListener itemClickListener = new GamesListAdapter.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "recyclerview onItemClick: " + position);
                Bundle b = new Bundle();
                b.putInt(Constants.VIEW_GAME_POS, position);
                b.putSerializable(Constants.VIEW_GAME, weekendLeague.getWeekendLeague().get(position));
                if(mListener!=null) mListener.OnViewGamesFragmentInteractionListenerInteraction(b);
            }
        };

        listener = new GamesListAdapter.RecyclerItemClickListener(getActivity(), itemClickListener);
        gamesList.addOnItemTouchListener(listener);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnViewGamesFragmentInteractionListener) {
            mListener = (OnViewGamesFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnViewGamesFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(Constants.CURRENT_WL, null);
        Type type = new TypeToken<WeekendLeague>() {}.getType();
        WeekendLeague wl = gson.fromJson(json, type);

        if(wl!=null && wl.getWeekendLeague()!=null){
            Log.d(TAG, "onResume viewGames: " + new Gson().toJson(wl.getWeekendLeague()));
            weekendLeague = wl;
            binding.setWeekendLeague(weekendLeague);
            setupAdapter();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnViewGamesFragmentInteractionListener {
        void OnViewGamesFragmentInteractionListenerInteraction(Bundle args);
    }
}
