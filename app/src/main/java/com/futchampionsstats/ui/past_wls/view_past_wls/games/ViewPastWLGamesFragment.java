package com.futchampionsstats.ui.past_wls.view_past_wls.games;

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
import com.futchampionsstats.adapters.GamesListAdapter;
import com.futchampionsstats.databinding.FragmentViewPastWlGamesBinding;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.WeekendLeague;

public class ViewPastWLGamesFragment extends Fragment implements ViewPastWLGamesContract.View{


    private OnViewPastWLGamesFragmentInteractionListener mListener;
    public static final String TAG = ViewPastWLGamesFragment.class.getSimpleName();

    FragmentViewPastWlGamesBinding binding;

    private GamesListAdapter mAdapter;
    private GamesListAdapter.RecyclerItemClickListener listener;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView gamesList;

    private ViewPastWLGamesContract.Presenter mPresenter;

    public ViewPastWLGamesFragment() {
        // Required empty public constructor
    }

    public static ViewPastWLGamesFragment newInstance() {
        ViewPastWLGamesFragment fragment = new ViewPastWLGamesFragment();
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
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_past_wl_games, container, false);
        ViewPastWLsGamesHandlers handlers = new ViewPastWLsGamesHandlers();
        binding.setHandlers(handlers);

        gamesList = (RecyclerView) binding.getRoot().findViewById(R.id.wl_games_list);

        return binding.getRoot();
    }

    @Override
    public void setPresenter(ViewPastWLGamesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showWeekendLeague(WeekendLeague weekendLeague) {
        binding.setWeekendLeague(weekendLeague);
        setupAdapter(weekendLeague);
    }

    @Override
    public void showWeekendLeagueGame(Game game) {
        if(mListener!=null) mListener.onViewPastWLGamesGoToGame(game);
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

    public class ViewPastWLsGamesHandlers{

        public void onBackBtnClick(View view){
            if(mListener!=null) mListener.onViewPastWLGamesBackBtnClick();
        }
    }

    private void setRecyclerAssignmentListener() {

        GamesListAdapter.RecyclerItemClickListener.OnItemClickListener itemClickListener = new GamesListAdapter.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "recyclerview onItemClick: " + position);
                mPresenter.getWeekendLeagueGameSelected(position);
            }
        };

        listener = new GamesListAdapter.RecyclerItemClickListener(getActivity(), itemClickListener);
        gamesList.addOnItemTouchListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
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
        void onViewPastWLGamesBackBtnClick();
        void onViewPastWLGamesGoToGame(Game game);
    }
}
