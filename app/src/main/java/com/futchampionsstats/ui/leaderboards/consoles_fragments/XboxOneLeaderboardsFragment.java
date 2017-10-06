package com.futchampionsstats.ui.leaderboards.consoles_fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futchampionsstats.FutChampsApplication;
import com.futchampionsstats.R;
import com.futchampionsstats.adapters.LeaderboardsAdapter;
import com.futchampionsstats.databinding.FragmentLeaderboardsXboxoneBinding;
import com.futchampionsstats.models.leaderboards.Top100;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

public class XboxOneLeaderboardsFragment extends Fragment implements XboxLeaderboardsContract.View{


    private XboxLeaderboardsContract.Presenter mPresenter;

    private RecyclerView xboxLeaderboardsView;
    private LeaderboardsAdapter leaderboardsAdapter;
    private LinearLayoutManager mLayoutManager;

    private FragmentLeaderboardsXboxoneBinding mBinding;

    public XboxOneLeaderboardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboards_xboxone, container, false);
        mBinding = DataBindingUtil.bind(view);

        if(mPresenter!=null){
            mPresenter.start();
        }
        else{
            mPresenter = new XboxLeaderboardsPresenter((
                    (FutChampsApplication) getContext().getApplicationContext()).getServiceComponent().getService(),
                    this);
            mPresenter.start();
        }

        mBinding.setHandlers(new XboxLeaderboardsHandlers());
        xboxLeaderboardsView = mBinding.xboxLeaderboardsView;

        return view;
    }

    public class XboxLeaderboardsHandlers{

    }

    public void setupAdapter(Top100 top100){
        mLayoutManager = new LinearLayoutManager(getActivity());

        xboxLeaderboardsView.setLayoutManager(mLayoutManager);
        xboxLeaderboardsView.setItemAnimator(new DefaultItemAnimator());

        leaderboardsAdapter = new LeaderboardsAdapter(top100.getConsole());
        xboxLeaderboardsView.setAdapter(leaderboardsAdapter);
    }

    public void setupSpinners(final String[] months){
        final String[] regions = new String[]{"Americas", "Europe", "Rest of World"};
        final MaterialSpinner regionSelector = mBinding.regionSelector;
        regionSelector.setItems(Arrays.asList(regions));
        regionSelector.setSelectedIndex(0);

        final MaterialSpinner monthSelector = mBinding.monthSelector;
        monthSelector.setItems(Arrays.asList(months));
        monthSelector.setSelectedIndex(months.length-1);

        monthSelector.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                mPresenter.getXboxLeaderboards(months[position], regions[regionSelector.getSelectedIndex()]);
            }
        });

        regionSelector.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                mPresenter.getXboxLeaderboards(months[monthSelector.getSelectedIndex()], regions[position]);
            }
        });

    }

    @Override
    public void setPresenter(@NonNull XboxLeaderboardsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showLoading() {
        mBinding.xboxLeaderboardsLoading.setVisibility(View.VISIBLE);
        mBinding.xboxLeaderboardsView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        mBinding.xboxLeaderboardsLoading.setVisibility(View.GONE);
        mBinding.xboxLeaderboardsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setXboxLeaderboards(Top100 top100) {
        setupAdapter(top100);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showError() {

    }

    @Override
    public void setMonths(String[] months) {
        setupSpinners(months);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
