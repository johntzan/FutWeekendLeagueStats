package com.futchampionsstats.ui.pastwls;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.futchampionsstats.R;
import com.futchampionsstats.adapters.GamesListAdapter;
import com.futchampionsstats.adapters.PastWlsListAdapter;
import com.futchampionsstats.databinding.FragmentViewPastWlsBinding;
import com.futchampionsstats.models.AllWeekendLeagues;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.WeekendLeague;
import com.futchampionsstats.utils.Constants;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class ViewPastWLsFragment extends Fragment {

    public static final String TAG = ViewPastWLsFragment.class.getSimpleName();

    private OnViewPastWLsFragmentInteractionListener mListener;
    private AllWeekendLeagues allWeekendLeagues;
    private ArrayList<Game> allGames;

    private PastWlsListAdapter mAdapter;
    private PastWlsListAdapter.RecyclerItemClickListener listener;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView pastWlsList;

    private RecyclerView pastWlsAllGamesList;
    private GamesListAdapter mGamesListAdapter;
    private GamesListAdapter.RecyclerItemClickListener mGamesListAdapterClickListener;
    private LinearLayoutManager mGamesLayoutManager;

    private FragmentViewPastWlsBinding binding;

    FloatingSearchView searchView;
    ImageView searchIcon;


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

        allGames = new ArrayList<>();

        searchView = (FloatingSearchView) binding.getRoot().findViewById(R.id.search_games_edit);
        searchIcon = (FloatingActionButton) binding.getRoot().findViewById(R.id.search_icon);

        pastWlsList = (RecyclerView) binding.getRoot().findViewById(R.id.past_wls_list);
        pastWlsList.setVisibility(View.GONE);
        pastWlsAllGamesList = (RecyclerView) binding.getRoot().findViewById(R.id.past_wls_all_games_list);
        pastWlsAllGamesList.setVisibility(View.GONE);

        setupSearchView();

        return binding.getRoot();
    }

    private void setupSearchView(){

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

    }

    private void setupAdapter(){
        mLayoutManager = new LinearLayoutManager(getActivity());

        pastWlsList.setLayoutManager(mLayoutManager);
        pastWlsList.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new PastWlsListAdapter(getActivity(), allWeekendLeagues.getAllWeekendLeagues());
        pastWlsList.setAdapter(mAdapter);
        if(allWeekendLeagues!=null && allWeekendLeagues.getAllWeekendLeagues().size()>0){
            pastWlsList.setVisibility(View.VISIBLE);
        }
        setRecyclerAssignmentListener();
    }

    private void setupAllGamesAdapter(){
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
                Bundle b = new Bundle();
                b.putInt(Constants.VIEW_WL_POS, position);
                b.putSerializable(Constants.VIEW_WL, allWeekendLeagues.getAllWeekendLeagues().get(position));
                if(mListener!=null) mListener.onViewPastWLsFragmentInteraction(b);
            }
        };

        listener = new PastWlsListAdapter.RecyclerItemClickListener(getActivity(), itemClickListener);
        pastWlsList.addOnItemTouchListener(listener);
    }

    private void setGamesRecyclerListener() {

        GamesListAdapter.RecyclerItemClickListener.OnItemClickListener itemClickListener = new GamesListAdapter.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle b = new Bundle();
                if(searchView.getVisibility() == View.VISIBLE){
                    Game viewGame = mGamesListAdapter.getGameFromPosition(position);
                    b.putSerializable(Constants.VIEW_PAST_WL_GAME, viewGame);
                }
                else{
                    b.putSerializable(Constants.VIEW_PAST_WL_GAME, allGames.get(position));
                }
                if(mListener!=null) mListener.onViewPastWLsFragmentInteraction(b);

            }
        };

        mGamesListAdapterClickListener = new GamesListAdapter.RecyclerItemClickListener(getActivity(), itemClickListener);
        pastWlsAllGamesList.addOnItemTouchListener(mGamesListAdapterClickListener);
    }

    public class ViewPastWLsHandlers{

        public void onClick(View view){

            Bundle b = new Bundle();
            switch(view.getId()) {
                case R.id.back_btn:
                    b.putString(Constants.BACK_BTN, Constants.BACK_BTN);
                    break;
                case R.id.view_all_wls_btn:
                    //setup/reset search
                    searchIcon.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                    searchView.setSearchFocused(false);
                    searchView.setSearchText("");
                    mGamesListAdapter.setData(allGames);

                    pastWlsList.setVisibility(View.VISIBLE);
                    pastWlsAllGamesList.setVisibility(View.GONE);
                    break;
                case R.id.view_all_games_btn:
                    searchIcon.setVisibility(View.VISIBLE);
                    pastWlsAllGamesList.setVisibility(View.VISIBLE);
                    mGamesListAdapter.setData(allGames);
                    pastWlsList.setVisibility(View.GONE);
                    break;
                case R.id.search_icon:
                    searchIcon.setVisibility(View.GONE);
                    searchView.setVisibility(View.VISIBLE);
                    searchView.setSearchFocused(true);
                    searchView.setSearchText("");
                    break;
            }
            if(mListener!=null) mListener.onViewPastWLsFragmentInteraction(b);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        allWeekendLeagues = new AllWeekendLeagues();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(Constants.ALL_WLS, null);
        Type type = new TypeToken<AllWeekendLeagues>() {}.getType();
        AllWeekendLeagues all_wl = gson.fromJson(json, type);

        if(all_wl!=null && all_wl.getAllWeekendLeagues()!=null){
            Log.d(TAG, "onResume viewWls: " + new Gson().toJson(all_wl.getAllWeekendLeagues()));
            allWeekendLeagues = all_wl;
            binding.setAllWeekendLeagues(allWeekendLeagues);
            updateAllGamesList(allWeekendLeagues);
            setupAdapter();
        }

        if(searchView!=null){
            searchView.setVisibility(View.GONE);
            searchView.setSearchFocused(false);
            searchView.setSearchText("");
        }
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

    private void updateAllGamesList(AllWeekendLeagues all_wls){
        ArrayList<Game> temp = new ArrayList<>();
        for(WeekendLeague weekendLeague : all_wls.getAllWeekendLeagues()){
            for(Game game : weekendLeague.getWeekendLeague()){
                temp.add(game);
            }
        }
        if(temp.size()>allGames.size()){
            allGames = temp;
            setupAllGamesAdapter();
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
        void onViewPastWLsFragmentInteraction(Bundle args);
    }
}
