package com.futchampionsstats.ui.leaderboards;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.futchampionsstats.R;
import com.futchampionsstats.adapters.UserProfileRankAdapter;
import com.futchampionsstats.databinding.FragmentUserProfileLeaderboardsBinding;
import com.futchampionsstats.models.leaderboards.User;

public class UserProfileLeaderboardsFragment extends Fragment {


    private OnUserProfileLeaderboardsInteractionListener mListener;
    private FragmentUserProfileLeaderboardsBinding mBinding;

    private RecyclerView ranksView;
    private UserProfileRankAdapter ranksAdapter;
    private LinearLayoutManager mLayoutManager;

    private User user;

    public UserProfileLeaderboardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile_leaderboards, container, false);
        mBinding = DataBindingUtil.bind(view);
        mBinding.setUser(user);
        mBinding.setHandlers(new UserProfileHandlers());

        ranksView = mBinding.userRanks;
        TextView console = mBinding.userConsole;
        if(user.getConsole().equals("Xbox One")){
            console.setTextColor(getResources().getColor(R.color.xbox_green));
        }
        else{
            console.setTextColor(getResources().getColor(R.color.ps_blue));
        }
        setupAdapter(user);

        Answers.getInstance().logCustom(new CustomEvent("Searching for a user on leaderboards"));

        return view;
    }

    public void setupAdapter(User user){
        mLayoutManager = new LinearLayoutManager(getActivity());

        ranksView.setLayoutManager(mLayoutManager);
        ranksView.setItemAnimator(new DefaultItemAnimator());

        ranksAdapter= new UserProfileRankAdapter(user.getRankings());
        ranksView.setAdapter(ranksAdapter);
    }

    public class UserProfileHandlers{
        public void goBack(View v){
            getActivity().onBackPressed();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserProfileLeaderboardsInteractionListener) {
            mListener = (OnUserProfileLeaderboardsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserProfileLeaderboardsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnUserProfileLeaderboardsInteractionListener {
    }
}
