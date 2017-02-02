package com.futchampionsstats.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futchampionsstats.R;
import com.futchampionsstats.Utils.Constants;
import com.futchampionsstats.databinding.FragmentPastWlBinding;
import com.futchampionsstats.models.AllWeekendLeagues;
import com.futchampionsstats.models.WeekendLeague;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PastWLFragment extends Fragment {

    public static final String TAG = PastWLFragment.class.getSimpleName();

    private OnPastWLFragmentInteractionListener mListener;
    private FragmentPastWlBinding binding;
    private AllWeekendLeagues allWeekendLeagues;

    public PastWLFragment() {
        // Required empty public constructor
    }


    public static PastWLFragment newInstance(String param1, String param2) {
        PastWLFragment fragment = new PastWLFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allWeekendLeagues = new AllWeekendLeagues();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(Constants.ALL_WLS, null);
        Type type = new TypeToken<AllWeekendLeagues>() {}.getType();
        AllWeekendLeagues all_wl = gson.fromJson(json, type);

        if(all_wl!=null && all_wl.getAllWeekendLeagues()!=null){
            Log.d(TAG, "onCreate: " + new Gson().toJson(all_wl.getAllWeekendLeagues()));
            allWeekendLeagues = all_wl;
        }
        else{
            Log.d(TAG, "onCreate wl: null");
        }
        if(allWeekendLeagues.getAllWeekendLeagues()==null){
            ArrayList<WeekendLeague> new_wls = new ArrayList<>();
            allWeekendLeagues.setAllWeekendLeagues(new_wls);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_past_wl, container, false);
        PastWLFragmentHandlers handlers = new PastWLFragmentHandlers();

        binding.setHandlers(handlers);
        binding.setAllWeekendLeagues(allWeekendLeagues);
        return binding.getRoot();
    }


    public class PastWLFragmentHandlers {

        public void pastWLFragmentClick(View view) {

            final Bundle b = new Bundle();
            switch (view.getId()) {
                case R.id.back_btn:
                    b.putSerializable(Constants.BACK_BTN, Constants.BACK_BTN);
                    break;
                case R.id.delete_wl_btn:
                    b.putSerializable(Constants.DELETE_WLS, allWeekendLeagues);
                    break;
            }
            if (mListener != null) mListener.onPastWLFragmentInteraction(b);
        }

    }


    public void clearAllWeekendLeague(AllWeekendLeagues allWeekendLeagues) {
        Log.d(TAG, "clearWeekendLeague: ");
        allWeekendLeagues.clearPastWLs();
        ArrayList<WeekendLeague> new_wls = new ArrayList<>();
        allWeekendLeagues.setAllWeekendLeagues(new_wls);
        binding.setAllWeekendLeagues(allWeekendLeagues);


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(Constants.ALL_WLS, null);
        editor.apply();
    }


        @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPastWLFragmentInteractionListener) {
            mListener = (OnPastWLFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPastWLFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPastWLFragmentInteractionListener {
        void onPastWLFragmentInteraction(Bundle args);
    }
}
