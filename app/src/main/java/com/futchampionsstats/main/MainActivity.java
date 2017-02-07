package com.futchampionsstats.main;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.futchampionsstats.R;
import com.futchampionsstats.Utils.Constants;
import com.futchampionsstats.Utils.Utils;
import com.futchampionsstats.models.AllWeekendLeagues;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.WeekendLeague;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnMainFragmentInteractionListener,
        WLFragment.OnNewWLFragmentInteractionListener, NewGameFragment.OnNewGameFragmentInteractionListener,
        ViewGamesFragment.OnViewGamesFragmentInteractionListener, EditGameFragment.OnEditGameFragmentInteractionListener,
        PastWLFragment.OnPastWLFragmentInteractionListener
{

    public static final String TAG = MainActivity.class.getSimpleName();

    private MainActivityFragment mMainFragment;
    private WLFragment wlFragment;
    private NewGameFragment newGameFragment;
    private ViewGamesFragment viewGamesFragment;
    private EditGameFragment editGameFragment;
    private PastWLFragment pastWLFragment;

    private WeekendLeague weekendLeague;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        weekendLeague = new WeekendLeague();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(Constants.CURRENT_WL, null);
        Type type = new TypeToken<WeekendLeague>() {}.getType();
        WeekendLeague wl = gson.fromJson(json, type);

        if(wl!=null && wl.getWeekendLeague()!=null){
            Log.d(TAG, "onCreate: " + new Gson().toJson(wl.getWeekendLeague()));
            weekendLeague = wl;
        }
        if(weekendLeague.getWeekendLeague()==null){
            ArrayList<Game> games = new ArrayList<>();
            weekendLeague.setWeekendLeague(games);

            Log.d(TAG, "onCreate: weekendleague null");
        }
        if (savedInstanceState == null) {
            mMainFragment = new MainActivityFragment();
            wlFragment = new WLFragment();
            newGameFragment = new NewGameFragment();
            viewGamesFragment = new ViewGamesFragment();
            editGameFragment = new EditGameFragment();
            pastWLFragment = new PastWLFragment();
        }

        displayFragment(mMainFragment, "main frag");

    }

    public void displayFragment(Fragment fragment, String tag) {

        Log.d(TAG + "fragment is null?", String.valueOf(fragment == null));
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // removes the existing fragment calling onDestroy

            ft.replace(R.id.container, fragment);
            if (tag != null) ft.addToBackStack(tag);
            ft.commit();
        }
    }

    @Override
    public void onMainFragmentInteraction(Bundle args) {
        if (args != null) {
            if(args.containsKey(Constants.WL)){
                if(wlFragment!=null){
                    displayFragment(wlFragment, "weekend_league_frag");
                }
                else{
                    wlFragment = new WLFragment();
                    displayFragment(wlFragment, "weekend_league_frag");
                }
            }
            if(args.containsKey(Constants.PAST_WL)){
                Log.d(TAG, "onMainFragmentInteraction: past wls");
                if(pastWLFragment!=null){
                    displayFragment(pastWLFragment, "past_weekend_league_frag");
                }
                else{
                    pastWLFragment = new PastWLFragment();
                    displayFragment(pastWLFragment, "past_weekend_league_frag");
                }
            }
        }
    }

    @Override
    public void onNewWLFragmentInteraction(Bundle args) {
        if (args != null) {
            if(args.containsKey(Constants.NEW_GAME)){
                newGameFragment = new NewGameFragment();
                displayFragment(newGameFragment, "new_game_frag");
            }
            if(args.containsKey(Constants.VIEW_GAMES)){

                WeekendLeague weekendLeague = (WeekendLeague) args.getSerializable(Constants.VIEW_GAMES);
                Bundle b = new Bundle();
                b.putSerializable(Constants.VIEW_GAMES, weekendLeague);
                if(viewGamesFragment!=null){
                    viewGamesFragment.setArguments(b);
                    displayFragment(viewGamesFragment, "view_games_frag");
                }
            }
            if(args.containsKey(Constants.BACK_BTN)){
                onBackPressed();
            }
            if(args.containsKey(Constants.SAVE_WL_TO_DATA_FROM_CREATE)){
                final WeekendLeague weekendLeague = (WeekendLeague) args.getSerializable(Constants.SAVE_WL_TO_DATA_FROM_CREATE);
                Log.d(TAG, "onNewWLFragmentInteraction save wl: " + new Gson().toJson(weekendLeague));

                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Weekend League finished?");
                pDialog.setContentText("No more games left this weekend, would you like to save and start a new Weekend League?");
                pDialog.setConfirmText("Yes");
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Log.d(TAG, "onClick: yes");
                        if(wlFragment!=null && weekendLeague!=null){

                            sweetAlertDialog
                                    .setTitleText("Saved!")
                                    .setContentText("Your Weekend League has been saved!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(null)
                                    .showCancelButton(false)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                            String date = new SimpleDateFormat("MM-dd-yyyy", Locale.US).format(new Date());
                            Log.d(TAG, "onClick: Current time => " + date);

                            weekendLeague.setDateOfWL(date);
                            saveWeekendLeague(weekendLeague);
                            wlFragment.clearWeekendLeague(weekendLeague);
                        }
                        else{
                            Log.d(TAG, "onNewWLFragmentInteraction save wl: nulls");
                        }

                    }
                });
                pDialog.setCancelText("No");
                pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Log.d(TAG, "onClick: no");
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                pDialog.setCancelable(true);
                pDialog.setCanceledOnTouchOutside(true);
                pDialog.show();
            }
            if(args.containsKey(Constants.SAVE_WL_TO_DATA)){
                final WeekendLeague weekendLeague = (WeekendLeague) args.getSerializable(Constants.SAVE_WL_TO_DATA);
                Log.d(TAG, "onNewWLFragmentInteraction save wl: " + new Gson().toJson(weekendLeague));

                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Save this Weekend League?");
                pDialog.setContentText("Are you sure you would like to save this Weekend League? Doing so will clear out current Weekend League.");
                pDialog.setConfirmText("Yes");
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Log.d(TAG, "onClick: yes");
                        if(wlFragment!=null && weekendLeague!=null){
                            String date = new SimpleDateFormat("MM-dd-yyyy", Locale.US).format(new Date());
                            Log.d(TAG, "onClick: Current time => " + date);

                            sweetAlertDialog
                                    .setTitleText("Saved!")
                                    .setContentText("Your Weekend League has been saved!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(null)
                                    .showCancelButton(false)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                            weekendLeague.setDateOfWL(date);
                            saveWeekendLeague(weekendLeague);
                            wlFragment.clearWeekendLeague(weekendLeague);
                        }
                        else{
                            Log.d(TAG, "onNewWLFragmentInteraction save wl: nulls");
                        }
                    }
                });
                pDialog.setCancelText("No");
                pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Log.d(TAG, "onClick: no");
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                pDialog.setCancelable(true);
                pDialog.setCanceledOnTouchOutside(true);
                pDialog.show();


            }
            if(args.containsKey(Constants.NEW_WL)){
                final WeekendLeague weekendLeague = (WeekendLeague) args.getSerializable(Constants.NEW_WL);
                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Clear this Weekend League?");
                pDialog.setContentText("Are you sure you would like to delete this Weekend League data? Doing so will create a new Weekend League.");
                pDialog.setConfirmText("Yes");
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Log.d(TAG, "onClick: yes");
                        if(wlFragment!=null && weekendLeague!=null){
                            sweetAlertDialog
                                    .setTitleText("Cleared!")
                                    .setContentText("Weekend League data has been cleared!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(null)
                                    .showCancelButton(false)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            wlFragment.clearWeekendLeague(weekendLeague);
                        }
                        else{
                            Log.d(TAG, "onNewWLFragmentInteraction new wl: nulls");
                        }
                    }
                });
                pDialog.setCancelText("No");
                pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Log.d(TAG, "onClick: no");
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                pDialog.setCancelable(true);
                pDialog.setCanceledOnTouchOutside(true);
                pDialog.show();

            }

        }
        else{
            Log.d(TAG, "onNewWLFragmentInteraction: args null");
        }
    }

    @Override
    public void onNewGameFragmentInteraction(Bundle args) {
        if(args!=null){
            if(args.containsKey(Constants.NEW_GAME)){
                Game new_game = (Game) args.getSerializable(Constants.NEW_GAME);
                if(wlFragment!=null){
                    wlFragment.setNewGame(new_game);


                    onBackPressed();
                }
            }
            if(args.containsKey(Constants.BACK_BTN)){
                onBackPressed();
            }

        }
    }

    @Override
    public void OnViewGamesFragmentInteractionListenerInteraction(Bundle args) {
        if(args!=null){
            if(args.containsKey(Constants.VIEW_GAME)){
                Game game = (Game) args.getSerializable(Constants.VIEW_GAME);
                int pos = args.getInt(Constants.VIEW_GAME_POS, -1);
                editGameFragment = new EditGameFragment();
                    if(pos!=-1){
                        Bundle b = new Bundle();
                        b.putInt(Constants.VIEW_GAME_POS, pos);
                        b.putSerializable(Constants.VIEW_GAME, game);
                        editGameFragment.setArguments(b);
                        displayFragment(editGameFragment, "edit_game_frag");
                    }

            }
            if(args.containsKey(Constants.BACK_BTN)){
                onBackPressed();
            }
        }
    }

    @Override
    public void onEditGameFragmentInteraction(Bundle args) {
        if(args!=null){
            if(args.containsKey(Constants.BACK_BTN)){
                onBackPressed();
                Utils.hideSoftInput(this);
            }
            if(args.containsKey(Constants.SAVE_GAME)){
                Utils.hideSoftInput(this);

                Game game = (Game) args.getSerializable(Constants.SAVE_GAME);
                int pos = args.getInt(Constants.SAVE_GAME_POS, -1);

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                Gson gson = new Gson();
                String json = sharedPrefs.getString(Constants.CURRENT_WL, null);
                Type type = new TypeToken<WeekendLeague>() {}.getType();
                WeekendLeague wl = gson.fromJson(json, type);

                if(wl!=null){
                    Log.d(TAG, "onResume savegame: " + new Gson().toJson(wl.getWeekendLeague().size()));
                    weekendLeague = wl;
                }
                Log.d(TAG, "onEditGameFragmentInteraction: " + pos + " size:" + weekendLeague.getWeekendLeague().size());
                if(game!=null && pos !=-1){
                    try{
                        if(pos != weekendLeague.getWeekendLeague().size()){
                            weekendLeague.getWeekendLeague().set(pos, game);
                        }
                    }
                    catch (IndexOutOfBoundsException e){
                        Log.d(TAG, "onEditGameFragmentInteraction: " + e);
                    }

                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    String json2 = gson.toJson(weekendLeague);

                    editor.putString(Constants.CURRENT_WL, json2);
                    editor.apply();

                }
                else{
                    Toast.makeText(this, "An Error occurred saving your changes. Please try again.", Toast.LENGTH_SHORT).show();
                }

                onBackPressed();
            }
        }
    }

    private void saveWeekendLeague(WeekendLeague wl){
        if(wl.getWeekendLeague()!=null) {

            Answers.getInstance().logCustom(new CustomEvent("Save Weekend League"));

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            Gson gson = new Gson();
            String json = sharedPrefs.getString(Constants.ALL_WLS, null);
            Type type = new TypeToken<AllWeekendLeagues>() {}.getType();
            AllWeekendLeagues all_wls = gson.fromJson(json, type);

            if(all_wls!=null && all_wls.getAllWeekendLeagues()!=null){
                ArrayList<WeekendLeague> curr_wls = all_wls.getAllWeekendLeagues();
                curr_wls.add(wl);

                all_wls.setAllWeekendLeagues(curr_wls);
            }
            else{
                all_wls = new AllWeekendLeagues();
                ArrayList<WeekendLeague> wls = new ArrayList<>();
                wls.add(wl);

                all_wls.setAllWeekendLeagues(wls);
            }


            SharedPreferences.Editor editor = sharedPrefs.edit();
            String json2 = gson.toJson(all_wls);
            Log.d(TAG, "saveWeekendLeague: " + json2);

            editor.putString(Constants.ALL_WLS, json2);
            editor.apply();

        }
    }

    @Override
    public void onPastWLFragmentInteraction(Bundle args) {
        if(args!=null){
            if(args.containsKey(Constants.BACK_BTN)){
                onBackPressed();
            }
            if(args.containsKey(Constants.DELETE_WLS)){
                final AllWeekendLeagues allWeekendLeagues= (AllWeekendLeagues) args.getSerializable(Constants.DELETE_WLS);
                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Delete Past Weekend Leagues?");
                pDialog.setContentText("Are you sure you would like to delete all your past Weekend League Data?");
                pDialog.setConfirmText("Yes");
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Log.d(TAG, "onClick: yes");
                        if(pastWLFragment!=null && allWeekendLeagues!=null){
                            sweetAlertDialog
                                    .setTitleText("Deleted!")
                                    .setContentText("All previous Weekend League data has been deleted!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(null)
                                    .showCancelButton(false)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            pastWLFragment.clearAllWeekendLeague(allWeekendLeagues);
                        }
                        else{
                            Log.d(TAG, "onNewWLFragmentInteraction new wl: nulls");
                        }
                    }
                });
                pDialog.setCancelText("No");
                pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Log.d(TAG, "onClick: no");
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                pDialog.setCancelable(true);
                pDialog.setCanceledOnTouchOutside(true);
                pDialog.show();
            }
        }
    }
}
