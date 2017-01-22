package com.futchampionsstats.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.futchampionsstats.FutChampsApplication;
import com.futchampionsstats.R;
import com.futchampionsstats.Utils.Constants;
import com.futchampionsstats.databinding.FragmentWlBinding;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.WeekendLeague;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static android.content.ContentValues.TAG;


public class WLFragment extends Fragment {

    public static final String TAG = WLFragment.class.getSimpleName();

    private static OnNewWLFragmentInteractionListener mListener;

    private static WeekendLeague weekendLeague;
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

        if(wl!=null){
            Log.d(TAG, "onCreate: " + new Gson().toJson(wl.getWeekendLeague().size()));
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

    public static  class WLFragmentHandlers{

        public void wlFragmentClick(View view){

            final Bundle b = new Bundle();
            switch(view.getId()) {
                case R.id.new_game_btn:
                    b.putString(Constants.NEW_GAME, Constants.NEW_GAME);
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

            }
            if(mListener!=null) mListener.onNewWLFragmentInteraction(b);
        }

        @BindingAdapter({"GamesWonWatcher"})
        public static void gamesWonWatcher(TextView view, WeekendLeague wl){
            if(wl.getWeekendLeague()!=null){
                if(view!=null){
                    view.setText(wl.getWinTotal());
                }
            }
        }

        @BindingAdapter({"GamesPlayedWatcher"})
        public static void gamesPlayedWatcher(TextView view, WeekendLeague wl){
            if(wl.getWeekendLeague()!=null){
                if(view!=null){
                    view.setText(String.valueOf(wl.getWeekendLeague().size()));
                }
            }
        }

        @BindingAdapter({"GamesLeftWatcher"})
        public static void gamesLeftWatcher(TextView view, WeekendLeague wl){
            if(wl.getWeekendLeague()!=null){
                if(view!=null){
                    view.setText(String.valueOf((40- wl.getWeekendLeague().size()) ));
                }
            }
        }

        @BindingAdapter({"AvgShotsWatcher"})
        public static void avgShotsWatcher(TextView view, WeekendLeague wl){
            if(wl.getWeekendLeague()!=null){
                if(view!=null){

                    try{

                        ArrayList<Integer> shotsFor = new ArrayList<>();
                        ArrayList<Integer> shotsForOnGoal = new ArrayList<>();
                        for(Game game : wl.getWeekendLeague()){
                            if(game.getUser_shots()!=null || game.getUser_sog()!=null){
                                shotsFor.add(Integer.parseInt(game.getUser_shots()));
                                shotsForOnGoal.add(Integer.parseInt(game.getUser_sog()));
                            }
                        }
                        Double avgShotsFor = calculateAverage(shotsFor);
                        Double avgShotsOnG = calculateAverage(shotsForOnGoal);

                        view.setText(String.format(Locale.US, "%.2f", avgShotsFor) + "("+String.format(Locale.US, "%.2f", avgShotsOnG) + ")");
                    }
                    catch(NumberFormatException e){
                        Log.d(TAG, "avgShotsWatcher: " + e);
                    }
                }
            }
        }

        @BindingAdapter({"AvgPossWatcher"})
        public static void avgPossWatcher(TextView view, WeekendLeague wl){
            if(wl.getWeekendLeague()!=null){
                if(view!=null){
                    try{

                        ArrayList<Integer> possFor = new ArrayList<>();
                        ArrayList<Integer> possAgainst = new ArrayList<>();
                        for(Game game : wl.getWeekendLeague()){
                            if(game.getUser_possession()!=null || game.getOpp_possession()!=null){
                                possFor.add(Integer.parseInt(game.getUser_possession()));
                                possAgainst.add(Integer.parseInt(game.getOpp_possession()));
                            }
                        }
                        Double avgPossFor = calculateAverage(possFor);
                        Double avgPossAgainst= calculateAverage(possAgainst);

                        view.setText(String.format(Locale.US, "%.2f", avgPossFor) + "%("+String.format(Locale.US, "%.2f", avgPossAgainst) + "%)");
                    }
                    catch (NumberFormatException e){
                        Log.d(TAG, "avgPossWatcher: " +e);
                    }
                }
            }
        }

        @BindingAdapter({"AvgTacklesWatcher"})
        public static void avgTacklesWatcher(TextView view, WeekendLeague wl){
            if(wl.getWeekendLeague()!=null){
                if(view!=null){

                    try{

                        ArrayList<Integer> tacklesFor = new ArrayList<>();
                        ArrayList<Integer> tacklesAgainst = new ArrayList<>();
                        for(Game game : wl.getWeekendLeague()){
                            if(game.getUser_tackles()!=null || game.getOpp_tackles()!=null){
                                tacklesFor.add(Integer.parseInt(game.getUser_tackles()));
                                tacklesAgainst.add(Integer.parseInt(game.getOpp_tackles()));
                            }
                        }
                        Double avgTacklesFor = calculateAverage(tacklesFor);
                        Double avgTacklesAgainst= calculateAverage(tacklesAgainst);

                        view.setText(String.format(Locale.US, "%.2f", avgTacklesFor) + "("+String.format(Locale.US, "%.2f", avgTacklesAgainst) + ")");
                    }
                    catch(NumberFormatException e){
                        Log.d(TAG, "avgTacklesWatcher: " + e);
                    }
                }
            }
        }


        @BindingAdapter({"AvgShotsAgainstWatcher"})
        public static void avgShotsAgainstWatcher(TextView view, WeekendLeague wl){
            if(wl.getWeekendLeague()!=null){
                if(view!=null){
                    try{

                        ArrayList<Integer> shotsAgainstFor = new ArrayList<>();
                        ArrayList<Integer> shotsAgainstOnGoal = new ArrayList<>();
                        for(Game game : wl.getWeekendLeague()){
                            if(game.getOpp_shots()!=null || game.getOpp_sog()!=null){
                                shotsAgainstFor.add(Integer.parseInt(game.getOpp_shots()));
                                shotsAgainstOnGoal.add(Integer.parseInt(game.getOpp_sog()));
                            }
                        }
                        Double avgShotsAgainstFor = calculateAverage(shotsAgainstFor);
                        Double avgShotsAgainstOnG = calculateAverage(shotsAgainstOnGoal);

                        view.setText(String.format(Locale.US, "%.2f", avgShotsAgainstFor) + "("+String.format(Locale.US, "%.2f", avgShotsAgainstOnG) + ")");
                    }
                    catch(NumberFormatException e){
                        Log.d(TAG, "avgShotsAgainstWatcher: " + e);
                    }
                }
            }
        }

        @BindingAdapter({"AvgTeamRatingWatcher"})
        public static void avgTeamRatingWatcher(TextView view, WeekendLeague wl){
            if(wl.getWeekendLeague()!=null){
                if(view!=null){
                    try{

                        ArrayList<Integer> teamRating = new ArrayList<>();
                        for(Game game : wl.getWeekendLeague()){
                            if(game.getOpp_team_rating()!=null){
                                teamRating.add(Integer.parseInt(game.getOpp_team_rating()));
                            }
                        }
                        Double avgTeamRating = calculateAverage(teamRating);

                        view.setText(String.format(Locale.US, "%.2f", avgTeamRating));
                    }
                    catch(NumberFormatException e){
                        Log.d(TAG, "avgTeamRatingWatcher: "+ e);
                    }
                }
            }
        }

        @BindingAdapter({"AvgGoalsWatcher"})
        public static void avgGoalsWatcher(TextView view, WeekendLeague wl){
            if(wl.getWeekendLeague()!=null){
                if(view!=null){
                    try{

                        ArrayList<Integer> goalsFor = new ArrayList<>();
                        ArrayList<Integer> goalsAgainst = new ArrayList<>();
                        for(Game game : wl.getWeekendLeague()){
                            if(game.getUser_goals()!=null || game.getOpp_goals()!=null){
                                goalsFor.add(Integer.parseInt(game.getUser_goals()));
                                goalsAgainst.add(Integer.parseInt(game.getOpp_goals()));
                            }
                        }
                        Double avgGoalsFor = calculateAverage(goalsFor);
                        Double avgGoalsAgainst = calculateAverage(goalsAgainst);

                        view.setText(String.format(Locale.US, "%.2f", avgGoalsFor) + "("+String.format(Locale.US, "%.2f", avgGoalsAgainst) + ")");
                    }
                    catch (NumberFormatException e){
                        Log.d(TAG, "avgGoalsWatcher: " +e);
                    }
                }
            }
        }
    }

    private static double calculateAverage(List <Integer> marks) {
        Integer sum = 0;
        if(!marks.isEmpty()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }

    public void setNewGame(Game game){
        if(weekendLeague.getWeekendLeague()!=null) {
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

        if(wl!=null){
            Log.d(TAG, "onResume: " + new Gson().toJson(wl.getWeekendLeague().size()));
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
