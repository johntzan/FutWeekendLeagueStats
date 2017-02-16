package com.futchampionsstats.main;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.futchampionsstats.R;
import com.futchampionsstats.Utils.Constants;
import com.futchampionsstats.Utils.Utils;
import com.futchampionsstats.models.AllWeekendLeagues;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.Squad;
import com.futchampionsstats.models.WeekendLeague;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnMainFragmentInteractionListener,
        WLFragment.OnNewWLFragmentInteractionListener, NewGameFragment.OnNewGameFragmentInteractionListener,
        ViewGamesFragment.OnViewGamesFragmentInteractionListener, EditGameFragment.OnEditGameFragmentInteractionListener,
        PastWLFragment.OnPastWLFragmentInteractionListener, MySquadsFragment.OnMySquadsFragmentInteractionListener
{

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final String SELECTED_ITEM = "arg_selected_item";

    private BottomNavigationView mBottomNav;
    private int mSelectedItem;

    private MainActivityFragment mMainFragment;
    private WLFragment wlFragment;
    private NewGameFragment newGameFragment;
    private ViewGamesFragment viewGamesFragment;
    private EditGameFragment editGameFragment;
    private PastWLFragment pastWLFragment;
    private MySquadsFragment mySquadsFragment;

    private WeekendLeague weekendLeague;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        MenuItem selectedItem;

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

        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate: savedInstanceState!=null");
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 1);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            Log.d(TAG, "onCreate: savedInstanceState==null");
            selectedItem = mBottomNav.getMenu().getItem(1);
            selectedItem.setChecked(true);

            mMainFragment = new MainActivityFragment();
            wlFragment = new WLFragment();
            newGameFragment = new NewGameFragment();
            viewGamesFragment = new ViewGamesFragment();
            editGameFragment = new EditGameFragment();
            pastWLFragment = new PastWLFragment();
            mySquadsFragment = new MySquadsFragment();
        }

        selectFragment(selectedItem);

    }

    private void selectFragment(MenuItem item) {
        Log.d(TAG, "selectFragment: " + item.getTitle().toString());
        Fragment frag = null;
        String tag = "";
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.menu_past_wl:
                frag = pastWLFragment;
                tag = "past_weekend_league_frag";
                break;
            case R.id.menu_current_wl:
                frag = wlFragment;
                tag = "weekend_league_frag";
                break;
            case R.id.menu_my_squads:
                frag = mySquadsFragment;
                tag = "my_squads_frag";
                break;
        }

        // update selected item
        mSelectedItem = item.getItemId();

        // uncheck the other items.
        for (int i = 0; i< mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            menuItem.setChecked(false);
        }
        item.setChecked(true);

        if (frag != null) {

            if(frag.isAdded()){
                Log.d(TAG, "selectFragment: is added: " + tag);
                return;
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Log.d(TAG, "selectFragment: replacing: " + tag);
            ft.replace(R.id.container, frag, tag);
            ft.addToBackStack(tag);
            ft.commit();
        }
    }

    public void displayFragment(Fragment fragment, String tag) {

        Log.d(TAG + " fragment is null?", String.valueOf(fragment == null));
        if (fragment != null) {
//            if(fragment.isAdded()){
//                return;
//            }

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
            if(args.containsKey(Constants.MY_SQUADS)){
                if(mySquadsFragment!=null){
                    displayFragment(mySquadsFragment, "my_squads_frag");
                }
                else{
                    mySquadsFragment = new MySquadsFragment();
                    displayFragment(mySquadsFragment, "my_squads_frag");

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
                viewGamesFragment = new ViewGamesFragment();
                viewGamesFragment.setArguments(b);
                displayFragment(viewGamesFragment, "view_games_frag");

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

            if(args.containsKey(Constants.NEW_SQUAD)){

                // TODO: 2/10/17 Refactor - Separate to be used in multple fragments as method

                final AlertDialog add_new_squad_dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // Get the layout inflater
                LayoutInflater inflater = this.getLayoutInflater();

                View new_squad_dialog = inflater.inflate(R.layout.add_new_squad_dialog, null);

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setCancelable(true);
                builder.setView(new_squad_dialog);
                builder.create();
                add_new_squad_dialog = builder.show();

                final MaterialEditText squad_name = (MaterialEditText) new_squad_dialog.findViewById(R.id.squad_name_edit);
                final MaterialEditText squad_team_rating = (MaterialEditText) new_squad_dialog.findViewById(R.id.squad_team_rating_edit);
                Button add_squad_btn = (Button) new_squad_dialog.findViewById(R.id.new_squad_dialog_add);
                Button cancel_btn = (Button) new_squad_dialog.findViewById(R.id.new_squad_dialog_cancel);

                final MaterialSpinner squadFormationSpinner = (MaterialSpinner) new_squad_dialog.findViewById(R.id.squad_formation_edit);
                String[] myResArray = getResources().getStringArray(R.array.formations_array);
                final List<String> formations = Arrays.asList(myResArray);

                squadFormationSpinner.setItems(formations);
                squadFormationSpinner.setSelectedIndex(0);

                add_squad_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Squad new_squad = new Squad();
                        if(squad_name.getText().toString().length()<1){
                            squad_name.setError("Please fill out this field!");
                        }
                        else if(squad_team_rating.getText().toString().length()<1){
                            squad_team_rating.setError("Please fill out this field!");
                        }
                        else{
                            new_squad.setName(squad_name.getText().toString());
                            new_squad.setTeam_rating(squad_team_rating.getText().toString());
                            new_squad.setFormation(formations.get(squadFormationSpinner.getSelectedIndex()));

                            if(newGameFragment!=null){
                                newGameFragment.saveSquad(new_squad);
                            }

                            Log.d(TAG, "onClick new Squad: " + new Gson().toJson(new_squad));
                            add_new_squad_dialog.dismiss();
                        }

                    }
                });

                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        add_new_squad_dialog.dismiss();
                    }
                });



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

    @Override
    public void onMySquadsFragmentInteraction(Bundle args) {
        if(args!=null){
            if(args.containsKey(Constants.BACK_BTN)){
                onBackPressed();
            }
            if(args.containsKey(Constants.NEW_SQUAD)){

                final AlertDialog add_new_squad_dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // Get the layout inflater
                LayoutInflater inflater = this.getLayoutInflater();

                View new_squad_dialog = inflater.inflate(R.layout.add_new_squad_dialog, null);

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setCancelable(true);
                builder.setView(new_squad_dialog);
                builder.create();
                add_new_squad_dialog = builder.show();

                final MaterialEditText squad_name = (MaterialEditText) new_squad_dialog.findViewById(R.id.squad_name_edit);
                final MaterialEditText squad_team_rating = (MaterialEditText) new_squad_dialog.findViewById(R.id.squad_team_rating_edit);
                Button add_squad_btn = (Button) new_squad_dialog.findViewById(R.id.new_squad_dialog_add);
                Button cancel_btn = (Button) new_squad_dialog.findViewById(R.id.new_squad_dialog_cancel);

                final MaterialSpinner squadFormationSpinner = (MaterialSpinner) new_squad_dialog.findViewById(R.id.squad_formation_edit);
                String[] myResArray = getResources().getStringArray(R.array.formations_array);
                final List<String> formations = Arrays.asList(myResArray);

                squadFormationSpinner.setItems(formations);
                squadFormationSpinner.setSelectedIndex(0);

                add_squad_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Squad new_squad = new Squad();
                        if(squad_name.getText().toString().length()<1){
                            squad_name.setError("Please fill out this field!");
                        }
                        else if(squad_team_rating.getText().toString().length()<1){
                            squad_team_rating.setError("Please fill out this field!");
                        }
                        else{
                            new_squad.setName(squad_name.getText().toString());
                            new_squad.setTeam_rating(squad_team_rating.getText().toString());
                            new_squad.setFormation(formations.get(squadFormationSpinner.getSelectedIndex()));

                            if(mySquadsFragment!=null){
                                mySquadsFragment.saveSquad(new_squad);
                            }

                            Log.d(TAG, "onClick new Squad: " + new Gson().toJson(new_squad));
                            add_new_squad_dialog.dismiss();
                        }

                    }
                });

                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        add_new_squad_dialog.dismiss();
                    }
                });



            }
            if(args.containsKey(Constants.EDIT_SQUAD)){

                Squad edit_squad = (Squad) args.getSerializable(Constants.EDIT_SQUAD);
                final int squad_index = args.getInt(Constants.EDIT_SQUAD_INDEX);

                if(edit_squad!=null){

                    final AlertDialog add_new_squad_dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    // Get the layout inflater
                    LayoutInflater inflater = this.getLayoutInflater();

                    View new_squad_dialog = inflater.inflate(R.layout.add_new_squad_dialog, null);

                    // Inflate and set the layout for the dialog
                    // Pass null as the parent view because its going in the dialog layout
                    builder.setCancelable(true);
                    builder.setView(new_squad_dialog);
                    builder.create();
                    add_new_squad_dialog = builder.show();

                    TextView dialog_title = (TextView) new_squad_dialog.findViewById(R.id.add_new_squad_title);
                    dialog_title.setText("Edit Squad");

                    final MaterialEditText squad_name = (MaterialEditText) new_squad_dialog.findViewById(R.id.squad_name_edit);
                    final MaterialEditText squad_team_rating = (MaterialEditText) new_squad_dialog.findViewById(R.id.squad_team_rating_edit);
                    Button add_squad_btn = (Button) new_squad_dialog.findViewById(R.id.new_squad_dialog_add);
                    add_squad_btn.setText("Confirm");
                    Button cancel_btn = (Button) new_squad_dialog.findViewById(R.id.new_squad_dialog_cancel);

                    final MaterialSpinner squadFormationSpinner = (MaterialSpinner) new_squad_dialog.findViewById(R.id.squad_formation_edit);
                    String[] myResArray = getResources().getStringArray(R.array.formations_array);
                    final List<String> formations = Arrays.asList(myResArray);

                    squadFormationSpinner.setItems(formations);

                    int edit_formation_index = 0;

                    for (int i = 0; i < formations.size(); i++) {
                        if(formations.get(i).equals(edit_squad.getFormation())){
                            edit_formation_index = i;
                        }
                    }

                    squad_name.setText(edit_squad.getName());
                    squad_team_rating.setText(edit_squad.getTeam_rating());
                    squadFormationSpinner.setSelectedIndex(edit_formation_index);

                    add_squad_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Squad new_squad = new Squad();
                            if(squad_name.getText().toString().length()<1){
                                squad_name.setError("Please fill out this field!");
                            }
                            else if(squad_team_rating.getText().toString().length()<1){
                                squad_team_rating.setError("Please fill out this field!");
                            }
                            else{
                                new_squad.setName(squad_name.getText().toString());
                                new_squad.setTeam_rating(squad_team_rating.getText().toString());
                                new_squad.setFormation(formations.get(squadFormationSpinner.getSelectedIndex()));

                                if(mySquadsFragment!=null){
                                    mySquadsFragment.saveEditSquad(new_squad, squad_index);
                                    Log.d(TAG, "onClick edit Squad: " + new Gson().toJson(new_squad));
                                    add_new_squad_dialog.dismiss();
                                }


                            }

                        }
                    });

                    cancel_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            add_new_squad_dialog.dismiss();
                        }
                    });



                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {

        if(wlFragment!=null && wlFragment.isVisible()){
            //do nothing
        }
        else{
            MenuItem homeItem = mBottomNav.getMenu().getItem(1);
            if (mSelectedItem != homeItem.getItemId()) {
                // select home item
                super.onBackPressed();
                selectFragment(homeItem);
            } else {
                super.onBackPressed();
            }
        }


    }
}
