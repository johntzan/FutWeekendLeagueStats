package com.futchampionsstats.ui.leaderboards;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.futchampionsstats.FutChampsApplication;
import com.futchampionsstats.R;
import com.futchampionsstats.databinding.FragmentLeaderboardsBinding;
import com.futchampionsstats.models.leaderboards.SearchSuggestions;
import com.futchampionsstats.models.leaderboards.User;
import com.futchampionsstats.ui.leaderboards.consoles_fragments.PS4LeaderboardsFragment;
import com.futchampionsstats.ui.leaderboards.consoles_fragments.PS4LeaderboardsPresenter;
import com.futchampionsstats.ui.leaderboards.consoles_fragments.XboxLeaderboardsPresenter;
import com.futchampionsstats.ui.leaderboards.consoles_fragments.XboxOneLeaderboardsFragment;
import com.futchampionsstats.utils.Utils;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public class LeaderboardsFragment extends Fragment implements LeaderboardsContract.View{

    private OnLeaderboardsInteractionListener mListener;
    public static final String TAG = LeaderboardsFragment.class.getSimpleName();
    private LeaderboardsContract.Presenter mPresenter;

    private ViewPager mViewPager;
    private FragmentPagerAdapter adapterViewPager;

    private PS4LeaderboardsFragment ps4LeaderboardsFragment;
    private PS4LeaderboardsPresenter ps4LeaderboardsPresenter;

    private XboxOneLeaderboardsFragment xboxOneLeaderboardsFragment;
    private XboxLeaderboardsPresenter xboxLeaderboardsPresenter;

    private FragmentLeaderboardsBinding mLeaderboardsBinding;

    private FloatingSearchView floatingSearchView;
    private FloatingActionButton floatingActionButton;


    public LeaderboardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboards, container, false);
        mLeaderboardsBinding = DataBindingUtil.bind(view);

        LeaderboardHandlers handlers = new LeaderboardHandlers();
        mLeaderboardsBinding.setHandlers(handlers);

        setupViewPager();

        floatingSearchView = mLeaderboardsBinding.searchUsersEdit;
        floatingActionButton = mLeaderboardsBinding.searchUserFab;

        setupSearchView();

        Answers.getInstance().logCustom(new CustomEvent("Viewing Leaderboards"));
        isConnectedToInternet();

        return view;
    }

    public class LeaderboardHandlers{

        public void viewSearch(View v){
            floatingActionButton.setVisibility(View.GONE);
            floatingSearchView.setVisibility(View.VISIBLE);
            floatingSearchView.setSearchFocused(true);
            floatingSearchView.setSearchText("");
        }

    }

    private void setupViewPager(){

        mViewPager = mLeaderboardsBinding.leaderboardsViewpager;

        mViewPager.setOffscreenPageLimit(2);
        adapterViewPager = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapterViewPager);

        TabLayout tabLayout = mLeaderboardsBinding.slidingTabs;
        tabLayout.setupWithViewPager(mViewPager);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //temporary solution to error on loading leaderboards, problem with storing month/ region state for resume
//                if(position==0){
//                    if(ps4LeaderboardsPresenter!=null){
//                        ps4LeaderboardsPresenter.start();
//                    }
//                }
//                if(position==1){
//                    if(xboxLeaderboardsPresenter!=null){
//                        xboxLeaderboardsPresenter.start();
//                    }
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupSearchView(){

        floatingSearchView.setHintTextColor(getResources().getColor(R.color.davy_grey));

        floatingSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                floatingSearchView.clearQuery();
                floatingSearchView.setSearchText("");
                floatingSearchView.setViewTextColor(getResources().getColor(R.color.grey));
                floatingSearchView.setQueryTextColor(getResources().getColor(R.color.grey));

            }

            @Override
            public void onFocusCleared() {

            }
        });
        floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                //get suggestions based on newQuery
                Log.d(TAG, "onSearchTextChanged new: " + newQuery);

                if(newQuery.length()>2){
                    floatingSearchView.showProgress();
                    mPresenter.getSearchSuggestions(newQuery);
                }

                if(newQuery.length()==0){
                    floatingSearchView.hideProgress();
                }

            }
        });

        floatingSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                floatingSearchView.clearQuery();
                floatingSearchView.hideProgress();
                floatingActionButton.setVisibility(View.VISIBLE);
                floatingSearchView.setVisibility(View.GONE);
            }
        });

        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                floatingSearchView.setSearchFocused(false);
                mPresenter.getResultFromSuggestion(searchSuggestion.getBody());
                if(mListener!=null) mListener.showLoadingIndicator();

            }

            @Override
            public void onSearchAction(String currentQuery) {

            }
        });

        floatingSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {

            @Override public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, SearchSuggestion item, int itemPosition) {
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.grey));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15.f);
            }
        });

    }


    @Override
    public void setPresenter(@NonNull LeaderboardsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showSearchSuggestions(List<SearchSuggestions> suggestions) {
        floatingSearchView.swapSuggestions(suggestions);
        floatingSearchView.hideProgress();
    }

    @Override
    public void showUserProfile(User user) {
        if(mListener!=null) mListener.goToUserProfileLeaderboards(user);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;
        private String tabTitles[] = new String[] { "PS4", "Xbox One" };

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if(ps4LeaderboardsFragment==null){
                        ps4LeaderboardsFragment = new PS4LeaderboardsFragment();
                    }
                    if(ps4LeaderboardsPresenter==null){
                        ps4LeaderboardsPresenter = new PS4LeaderboardsPresenter((
                                (FutChampsApplication) getContext().getApplicationContext()).getServiceComponent().getService(),
                                ps4LeaderboardsFragment);
                    }
                    return ps4LeaderboardsFragment;

                case 1:
                    if(xboxOneLeaderboardsFragment==null){
                        xboxOneLeaderboardsFragment = new XboxOneLeaderboardsFragment();
                    }
                    if(xboxLeaderboardsPresenter==null){
                        xboxLeaderboardsPresenter = new XboxLeaderboardsPresenter((
                                (FutChampsApplication) getContext().getApplicationContext()).getServiceComponent().getService(),
                                xboxOneLeaderboardsFragment);
                    }
                    return xboxOneLeaderboardsFragment;
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }


    public int handleBackPress(){
        if(floatingSearchView!=null && floatingActionButton!=null){
            if(floatingSearchView.getVisibility() == View.VISIBLE){
                Log.d(TAG, "handleBackPress: searchview visible clear and hide, show button");
                floatingSearchView.clearQuery();
                floatingSearchView.hideProgress();
                floatingActionButton.setVisibility(View.VISIBLE);
                floatingSearchView.setVisibility(View.GONE);
                return 1;
            }
            else{
                Log.d(TAG, "handleBackPress: searchview not visible");
                return 0;
            }
        }
        else{
            return 0;
        }

    }

    private void isConnectedToInternet(){
        if (getContext()!=null && !FutChampsApplication.getInstance(getContext()).isInternetAvailable()){
            Utils.showNoConnectionDialog(getContext());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if(mPresenter!=null){
            mPresenter.start();
        }
        else{
            mPresenter = new LeaderboardsPresenter((
                    (FutChampsApplication) getContext().getApplicationContext()).getServiceComponent().getService(),
                    this);
            mPresenter.start();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLeaderboardsInteractionListener) {
            mListener = (OnLeaderboardsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLeaderboardsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnLeaderboardsInteractionListener {
        void goToUserProfileLeaderboards(User user);
        void showLoadingIndicator();
    }
}
