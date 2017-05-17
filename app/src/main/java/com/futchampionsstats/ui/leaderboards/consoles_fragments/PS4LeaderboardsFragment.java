package com.futchampionsstats.ui.leaderboards.consoles_fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futchampionsstats.R;
import com.futchampionsstats.adapters.LeaderboardsAdapter;
import com.futchampionsstats.databinding.FragmentLeaderboardsPs4Binding;
import com.futchampionsstats.models.leaderboards.Top100;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Arrays;

public class PS4LeaderboardsFragment extends Fragment implements PS4LeaderboardsContract.View{


    private PS4LeaderboardsContract.Presenter mPresenter;
    private RecyclerView ps4LeaderboardsView;
    private LeaderboardsAdapter leaderboardsAdapter;
    private LinearLayoutManager mLayoutManager;

    private FragmentLeaderboardsPs4Binding mBinding;


    public PS4LeaderboardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboards_ps4, container, false);
        mBinding = DataBindingUtil.bind(view);

        mPresenter.start();

        mBinding.setHandlers(new PS4LeaderboardsHandlers());
        ps4LeaderboardsView = mBinding.ps4LeaderboardsView;

        return view;
    }

    public class PS4LeaderboardsHandlers{

    }

    public void setupAdapter(Top100 top100){
        mLayoutManager = new LinearLayoutManager(getActivity());

        ps4LeaderboardsView.setLayoutManager(mLayoutManager);
        ps4LeaderboardsView.setItemAnimator(new DefaultItemAnimator());

        leaderboardsAdapter = new LeaderboardsAdapter(top100.getConsole());
        ps4LeaderboardsView.setAdapter(leaderboardsAdapter);
    }

    public void setupSpinners(final String[] months){
        final String[] regions = new String[]{"All", "Americas", "Europe", "Rest of World"};
        final MaterialSpinner regionSelector = mBinding.regionSelector;
        regionSelector.setItems(Arrays.asList(regions));
        regionSelector.setSelectedIndex(0);

        final MaterialSpinner monthSelector = mBinding.monthSelector;
        monthSelector.setItems(Arrays.asList(months));
        monthSelector.setSelectedIndex(months.length-1);

        monthSelector.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                mPresenter.getPS4Leaderboards(months[position], regions[regionSelector.getSelectedIndex()]);
            }
        });

        regionSelector.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                mPresenter.getPS4Leaderboards(months[monthSelector.getSelectedIndex()], regions[position]);
            }
        });

    }


    @Override
    public void setPresenter(PS4LeaderboardsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        mBinding.ps4LeaderboardsLoading.setVisibility(View.VISIBLE);
        mBinding.ps4LeaderboardsView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        mBinding.ps4LeaderboardsLoading.setVisibility(View.GONE);
        mBinding.ps4LeaderboardsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPS4Leaderboards(Top100 top100) {
        setupAdapter(top100);
    }

    @Override
    public void setMonths(String[] months) {
        setupSpinners(months);
    }

    @Override
    public void showError() {
        Snackbar.make(mBinding.getRoot(), R.string.error_loading, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.error_retry, retryClickListener)
                .show(); // Don’t forget to show!
    }

    View.OnClickListener retryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Do something here
            mPresenter.start();
        }
    };

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
