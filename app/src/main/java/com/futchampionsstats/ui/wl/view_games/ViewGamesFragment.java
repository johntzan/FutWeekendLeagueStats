package com.futchampionsstats.ui.wl.view_games;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futchampionsstats.R;
import com.futchampionsstats.adapters.GamesListAdapter;
import com.futchampionsstats.databinding.FragmentGamesListBinding;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.WeekendLeague;

import static com.google.common.base.Preconditions.checkNotNull;

public class ViewGamesFragment extends Fragment implements ViewGamesContract.View{

    public static final String TAG = ViewGamesFragment.class.getSimpleName();

    private OnViewGamesFragmentInteractionListener mListener;
    private GamesListAdapter mAdapter;
    private GamesListAdapter.RecyclerItemClickListener listener;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView gamesList;

    FragmentGamesListBinding binding;

    private ViewGamesContract.Presenter mPresenter;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_games_list, container, false);

        ViewGamesHandlers handlers = new ViewGamesHandlers();
        binding.setHandlers(handlers);

        gamesList = binding.gamesList;

        return binding.getRoot();
    }

    @Override
    public void setPresenter(@NonNull ViewGamesContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setGames(WeekendLeague weekendLeague) {
        binding.setEmptyWeekendLeague(false);
        binding.setWeekendLeague(weekendLeague);

        setupAdapter(weekendLeague);
    }

    @Override
    public void showEmptyWeekendLeague() {
        binding.setEmptyWeekendLeague(true);
    }

    @Override
    public void showEditGame(Game game, int position) {
        if(mListener!=null) mListener.showEditGame(game, position);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void setupAdapter(WeekendLeague weekendLeague){
        mLayoutManager = new LinearLayoutManager(getActivity());

        gamesList.setLayoutManager(mLayoutManager);
        gamesList.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new GamesListAdapter(getActivity(), weekendLeague.getWeekendLeague());
        gamesList.setAdapter(mAdapter);
        setRecyclerAssignmentListener();
    }

    public class ViewGamesHandlers{

        public void goBack(View view){
            if(mListener!=null) mListener.onViewGamesBackPressed();
        }
    }

    private void setRecyclerAssignmentListener() {

        GamesListAdapter.RecyclerItemClickListener.OnItemClickListener itemClickListener = new GamesListAdapter.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "recyclerview onItemClick: " + position);
                mPresenter.editGame(position);
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

        if(mPresenter!=null){
            mPresenter.updateGames();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnViewGamesFragmentInteractionListener {
        void showEditGame(Game game, int position);
        void onViewGamesBackPressed();
    }
}
