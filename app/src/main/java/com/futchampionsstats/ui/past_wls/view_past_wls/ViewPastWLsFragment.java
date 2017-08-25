package com.futchampionsstats.ui.past_wls.view_past_wls;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.futchampionsstats.FutChampsApplication;
import com.futchampionsstats.R;
import com.futchampionsstats.adapters.GamesListAdapter;
import com.futchampionsstats.adapters.PastWlsListAdapter;
import com.futchampionsstats.databinding.FragmentViewPastWlsBinding;
import com.futchampionsstats.models.AllWeekendLeagues;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.WeekendLeague;
import com.futchampionsstats.utils.Utils;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;


public class ViewPastWLsFragment extends Fragment implements ViewPastWLsContract.View{

    public static final String TAG = ViewPastWLsFragment.class.getSimpleName();

    private OnViewPastWLsFragmentInteractionListener mListener;

    private RecyclerView pastWlsList;
    private PastWlsListAdapter mAdapter;
    private PastWlsListAdapter.RecyclerItemClickListener listener;
    private LinearLayoutManager mLayoutManager;

    private RecyclerView pastWlsAllGamesList;
    private GamesListAdapter mGamesListAdapter;
    private GamesListAdapter.RecyclerItemClickListener mGamesListAdapterClickListener;
    private LinearLayoutManager mGamesLayoutManager;

    private FragmentViewPastWlsBinding binding;

    FloatingSearchView searchView;
    ImageView searchIcon;

    private ViewPastWLsContract.Presenter mPresenter;


    public ViewPastWLsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_past_wls, container, false);
        ViewPastWLsHandlers handlers = new ViewPastWLsHandlers();
        binding.setHandlers(handlers);

        searchView = (FloatingSearchView) binding.getRoot().findViewById(R.id.search_games_edit);
        searchIcon = (FloatingActionButton) binding.getRoot().findViewById(R.id.search_icon);

        pastWlsList = (RecyclerView) binding.getRoot().findViewById(R.id.past_wls_list);
        pastWlsList.setVisibility(View.GONE);

        pastWlsAllGamesList = (RecyclerView) binding.getRoot().findViewById(R.id.past_wls_all_games_list);
        pastWlsAllGamesList.setVisibility(View.GONE);

        if(mPresenter!=null){
            mPresenter.start();
        }
        else{
            mPresenter = new ViewPastWLsPresenter(((FutChampsApplication) getContext().getApplicationContext()).getWeekendLeagueRepository().getWeekendLeagueRepository(),
                    this);
            mPresenter.start();
        }

        return binding.getRoot();
    }

    @Override
    public void setPresenter(@NonNull ViewPastWLsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showPastWeekendLeagues(AllWeekendLeagues allWeekendLeagues) {
        binding.setAllWeekendLeagues(allWeekendLeagues);

        setupAdapter(allWeekendLeagues);

        if(searchView!=null){
        searchView.setVisibility(View.GONE);
        searchView.setSearchFocused(false);
        searchView.setSearchText("");
        }
    }

    @Override
    public void showEmptyWeekendLeagues(AllWeekendLeagues allWeekendLeagues) {
        binding.setAllWeekendLeagues(allWeekendLeagues);
    }

    @Override
    public void showAllGamesView(ArrayList<Game> allGames) {
        setupAllGamesAdapter(allGames);
        setupSearchView(allGames);
    }

    @Override
    public void showWeekendLeagueDetail(WeekendLeague weekendLeague, int position) {
        if(mListener!=null) mListener.onViewWeekendLeagueDetail(weekendLeague);
    }

    @Override
    public void showGameDetail(Game game, int position) {
        if(mListener!=null) mListener.onViewPastWLGame(game);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void setupSearchView(final ArrayList<Game> allGames){

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                //get suggestions based on newQuery
                Log.d(TAG, "onSearchTextChanged new: " + newQuery);

                if(newQuery.length()>0){
                    mGamesListAdapter.getFilter().filter(newQuery);
                }
                else{
                    mGamesListAdapter.setData(allGames);
                }
                //pass them on to the search view
//                searchView.swapSuggestions(newSuggestions);
            }
        });

        searchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                searchIcon.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.GONE);
            }
        });

        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                searchView.clearQuery();
                searchView.setSearchText("");
                searchView.setSearchFocused(true);
                searchView.setViewTextColor(getResources().getColor(R.color.grey));
                searchView.setQueryTextColor(getResources().getColor(R.color.grey));

            }

            @Override
            public void onFocusCleared() {

            }
        });

        searchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {

            @Override public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, SearchSuggestion item, int itemPosition) {
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.grey));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15.f);
            }
        });

    }

    private void setupAdapter(AllWeekendLeagues allWeekendLeagues){
        mLayoutManager = new LinearLayoutManager(getActivity());

        pastWlsList.setLayoutManager(mLayoutManager);
        pastWlsList.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new PastWlsListAdapter(getActivity(), allWeekendLeagues.getAllWeekendLeagues());
        pastWlsList.setAdapter(mAdapter);
        if(allWeekendLeagues.getAllWeekendLeagues().size()>0){
            pastWlsList.setVisibility(View.VISIBLE);
        }
        setRecyclerAssignmentListener();
    }

    private void setupAllGamesAdapter(ArrayList<Game> allGames){
        mGamesLayoutManager = new LinearLayoutManager(getActivity());

        pastWlsAllGamesList.setLayoutManager(mGamesLayoutManager);
        pastWlsAllGamesList.setItemAnimator(new DefaultItemAnimator());

        mGamesListAdapter = new GamesListAdapter(getActivity(), allGames);
        pastWlsAllGamesList.setAdapter(mGamesListAdapter);
        setGamesRecyclerListener();
    }

    private void setRecyclerAssignmentListener() {

        PastWlsListAdapter.RecyclerItemClickListener.OnItemClickListener itemClickListener = new PastWlsListAdapter.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "recyclerview onItemClick: " + position);
                mPresenter.getWeekendLeagueForDetail(position);
            }
        };

        listener = new PastWlsListAdapter.RecyclerItemClickListener(getActivity(), itemClickListener);
        pastWlsList.addOnItemTouchListener(listener);
    }

    private void setGamesRecyclerListener() {

        GamesListAdapter.RecyclerItemClickListener.OnItemClickListener itemClickListener = new GamesListAdapter.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if(searchView.getVisibility() == View.VISIBLE){
                    Game viewGame = mGamesListAdapter.getGameFromPosition(position);
                    if(mListener!=null) mListener.onViewPastWLGame(viewGame);
                }
                else{
                    mPresenter.getGameForDetail(position);
                }

            }
        };

        mGamesListAdapterClickListener = new GamesListAdapter.RecyclerItemClickListener(getActivity(), itemClickListener);
        pastWlsAllGamesList.addOnItemTouchListener(mGamesListAdapterClickListener);
    }

    public class ViewPastWLsHandlers{

        public void onBackBtnClick(View view){
            if(mListener!=null) mListener.onViewPastWLsBackBtnClick();
        }

        public void onAllWeekendLeaguesBtnClick(View view){
            searchIcon.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.GONE);
            searchView.setSearchFocused(false);
            searchView.setSearchText("");

//            mPresenter.getAllGames();

            pastWlsList.setVisibility(View.VISIBLE);
            pastWlsAllGamesList.setVisibility(View.GONE);
        }

        public void onAllGamesBtnClick(View view){
            searchIcon.setVisibility(View.VISIBLE);
//            mPresenter.getAllGames();
            pastWlsAllGamesList.setVisibility(View.VISIBLE);
            pastWlsList.setVisibility(View.GONE);
        }
        public void onSearchBtnClick(View view){
            Utils.openSoftInput(view.getContext(), view);

            pastWlsAllGamesList.setVisibility(View.VISIBLE);
            pastWlsList.setVisibility(View.GONE);

            searchIcon.setVisibility(View.GONE);
            searchView.setVisibility(View.VISIBLE);
            searchView.setSearchFocused(true);
            searchView.setSearchText("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public int handleBackPress(){
        if(searchView!=null && searchIcon!=null){
            if(searchView.getVisibility() == View.VISIBLE){
                searchView.setVisibility(View.GONE);
                searchIcon.setVisibility(View.VISIBLE);
                return 1;
            }
            else{
                return 0;
            }
        }
        else{
            return 0;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnViewPastWLsFragmentInteractionListener) {
            mListener = (OnViewPastWLsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnViewPastWLsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnViewPastWLsFragmentInteractionListener {
        void onViewPastWLGame(Game game);
        void onViewWeekendLeagueDetail(WeekendLeague weekendLeague);
        void onViewPastWLsBackBtnClick();
    }
}
