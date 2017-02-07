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

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.futchampionsstats.R;
import com.futchampionsstats.Utils.Constants;
import com.futchampionsstats.databinding.FragmentWlBinding;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.WeekendLeague;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class WLFragment extends Fragment {

    public static final String TAG = WLFragment.class.getSimpleName();

    private OnNewWLFragmentInteractionListener mListener;

    private WeekendLeague weekendLeague;
    FragmentWlBinding binding;

    public WLFragment() {
        // Required empty public constructor
    }


    public static WLFragment newInstance(WeekendLeague wl) {
        WLFragment fragment = new WLFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** On Create, check if there is a current WL in shared prefs, if yes, set it to weekend league object for use
         * If no, create new Weekend League object for use, and save to shared prefs as current.
         *
         * */
        weekendLeague = new WeekendLeague();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(Constants.CURRENT_WL, null);
        Type type = new TypeToken<WeekendLeague>() {}.getType();
        WeekendLeague wl = gson.fromJson(json, type);

        if(wl!=null && wl.getWeekendLeague()!=null){
            Log.d(TAG, "onCreate: " + new Gson().toJson(wl.getWeekendLeague()));
            weekendLeague = wl;
        }
        else{
            Log.d(TAG, "onCreate wl: null");
        }
        if(weekendLeague.getWeekendLeague()==null){
            ArrayList<Game> new_wl = new ArrayList<>();
            weekendLeague.setWeekendLeague(new_wl);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wl, container, false);
        WLFragmentHandlers handlers = new WLFragmentHandlers();

        binding.setHandlers(handlers);
        binding.setWeekendLeague(weekendLeague);
        return binding.getRoot();
    }

    public class WLFragmentHandlers{

        public void wlFragmentClick(View view){

            final Bundle b = new Bundle();
            switch(view.getId()) {
                case R.id.new_game_btn:
                    if(weekendLeague.getWeekendLeague().size()<40){
                        b.putString(Constants.NEW_GAME, Constants.NEW_GAME);
                    }else{
                        b.putSerializable(Constants.SAVE_WL_TO_DATA_FROM_CREATE, weekendLeague);
                    }
                    break;
                case R.id.view_games_btn:
                    b.putSerializable(Constants.VIEW_GAMES, weekendLeague);
                    break;
                case R.id.back_btn:
                    b.putSerializable(Constants.BACK_BTN, Constants.BACK_BTN);
                    break;
                case R.id.new_wl:
                    b.putSerializable(Constants.NEW_WL, weekendLeague);
                    break;
                case R.id.save_wl_btn:
                    b.putSerializable(Constants.SAVE_WL_TO_DATA, weekendLeague);
                    break;

            }
            if(mListener!=null) mListener.onNewWLFragmentInteraction(b);
        }
    }

    public void setNewGame(Game game){
        if(weekendLeague.getWeekendLeague()!=null) {

            Answers.getInstance().logCustom(new CustomEvent("Saved New Game"));
            weekendLeague.getWeekendLeague().add(game);

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            Gson gson = new Gson();

            SharedPreferences.Editor editor = sharedPrefs.edit();
            String json2 = gson.toJson(weekendLeague);

            editor.putString(Constants.CURRENT_WL, json2);
            editor.apply();

        }
    }

    public void clearWeekendLeague(WeekendLeague wl){
        Log.d(TAG, "clearWeekendLeague: ");
        wl.clearWL();
        ArrayList<Game> new_wl = new ArrayList<>();
        weekendLeague.setWeekendLeague(new_wl);
        binding.setWeekendLeague(weekendLeague);


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(Constants.CURRENT_WL, null);
        editor.apply();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewWLFragmentInteractionListener) {
            mListener = (OnNewWLFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(Constants.CURRENT_WL, null);
        Type type = new TypeToken<WeekendLeague>() {}.getType();
        WeekendLeague wl = gson.fromJson(json, type);

        if(wl!=null && wl.getWeekendLeague()!=null){
            Log.d(TAG, "onResume: " + new Gson().toJson(wl.getWeekendLeague()));
            weekendLeague = wl;
            binding.setWeekendLeague(weekendLeague);
        }
        else{
            Log.d(TAG, "onResume: null");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnNewWLFragmentInteractionListener {
        void onNewWLFragmentInteraction(Bundle args);
    }
}
