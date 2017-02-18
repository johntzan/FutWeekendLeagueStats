package com.futchampionsstats.main;

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

import com.futchampionsstats.R;
import com.futchampionsstats.Utils.Constants;
import com.futchampionsstats.adapters.PastWlsListAdapter;
import com.futchampionsstats.databinding.FragmentViewPastWlsBinding;
import com.futchampionsstats.models.AllWeekendLeagues;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ViewPastWLsFragment extends Fragment {

    public static final String TAG = ViewPastWLsFragment.class.getSimpleName();

    private OnViewPastWLsFragmentInteractionListener mListener;
    private AllWeekendLeagues allWeekendLeagues;
    private PastWlsListAdapter mAdapter;
    private PastWlsListAdapter.RecyclerItemClickListener listener;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView pastWlsList;

    private FragmentViewPastWlsBinding binding;


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

        pastWlsList = (RecyclerView) binding.getRoot().findViewById(R.id.past_wls_list);

        return binding.getRoot();
    }

    private void setupAdapter(){
        mLayoutManager = new LinearLayoutManager(getActivity());

        pastWlsList.setLayoutManager(mLayoutManager);
        pastWlsList.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new PastWlsListAdapter(getActivity(), allWeekendLeagues.getAllWeekendLeagues());
        pastWlsList.setAdapter(mAdapter);
        setRecyclerAssignmentListener();
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

    public class ViewPastWLsHandlers{

        public void onClick(View view){

            Bundle b = new Bundle();
            switch(view.getId()) {
                case R.id.back_btn:
                    b.putString(Constants.BACK_BTN, Constants.BACK_BTN);
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
            setupAdapter();
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
