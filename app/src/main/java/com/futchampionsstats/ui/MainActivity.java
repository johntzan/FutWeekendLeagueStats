package com.futchampionsstats.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.futchampionsstats.Injection;
import com.futchampionsstats.R;
import com.futchampionsstats.models.AllWeekendLeagues;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.WeekendLeague;
import com.futchampionsstats.ui.mysquads.MySquadsFragment;
import com.futchampionsstats.ui.mysquads.MySquadsPresenter;
import com.futchampionsstats.ui.pastwls.PastWLFragment;
import com.futchampionsstats.ui.pastwls.PastWLViewGameFragment;
import com.futchampionsstats.ui.pastwls.ViewPastWLGamesFragment;
import com.futchampionsstats.ui.pastwls.ViewPastWLsFragment;
import com.futchampionsstats.ui.pastwls.ViewSelectedWLFragment;
import com.futchampionsstats.ui.wl.WeekendLeagueDetailFragment;
import com.futchampionsstats.ui.wl.WeekendLeagueDetailPresenter;
import com.futchampionsstats.ui.wl.edit_game.EditGameFragment;
import com.futchampionsstats.ui.wl.edit_game.EditGamePresenter;
import com.futchampionsstats.ui.wl.new_game.NewGameFragment;
import com.futchampionsstats.ui.wl.new_game.NewGamePresenter;
import com.futchampionsstats.ui.wl.view_games.ViewGamesFragment;
import com.futchampionsstats.ui.wl.view_games.ViewGamesPresenter;
import com.futchampionsstats.utils.Constants;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements WeekendLeagueDetailFragment.OnNewWLFragmentInteractionListener, NewGameFragment.OnNewGameFragmentInteractionListener,
        ViewGamesFragment.OnViewGamesFragmentInteractionListener, EditGameFragment.OnEditGameFragmentInteractionListener,
        PastWLFragment.OnPastWLFragmentInteractionListener, MySquadsFragment.OnMySquadsFragmentInteractionListener,
        ViewPastWLsFragment.OnViewPastWLsFragmentInteractionListener, ViewPastWLGamesFragment.OnViewPastWLGamesFragmentInteractionListener,
        PastWLViewGameFragment.OnPastWLViewGameFragmentInteractionListener, ViewSelectedWLFragment.OnViewSelectedWLFragmentInteractionListener
{

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final String SELECTED_ITEM = "arg_selected_item";

    private BottomNavigationView mBottomNav;
    private int mSelectedItem;

    private WeekendLeagueDetailFragment weekendLeagueDetailFragment;
    private NewGameFragment newGameFragment;
    private ViewGamesFragment viewGamesFragment;
    private EditGameFragment editGameFragment;
    private PastWLFragment pastWLFragment;
    private MySquadsFragment mySquadsFragment;
    private ViewPastWLsFragment viewPastWLsFragment;
    private ViewPastWLGamesFragment viewPastWLGamesFragment;
    private PastWLViewGameFragment pastWLViewGameFragment;
    private ViewSelectedWLFragment viewSelectedWLFragment;

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

        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate: savedInstanceState!=null");
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 1);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            Log.d(TAG, "onCreate: savedInstanceState==null");
            selectedItem = mBottomNav.getMenu().getItem(1);
            selectedItem.setChecked(true);

            weekendLeagueDetailFragment = new WeekendLeagueDetailFragment();
            newGameFragment = new NewGameFragment();
            viewGamesFragment = new ViewGamesFragment();
            editGameFragment = new EditGameFragment();
            pastWLFragment = new PastWLFragment();
            mySquadsFragment = new MySquadsFragment();
            viewPastWLsFragment = new ViewPastWLsFragment();
            viewPastWLGamesFragment = new ViewPastWLGamesFragment();
            pastWLViewGameFragment = new PastWLViewGameFragment();
            viewSelectedWLFragment = new ViewSelectedWLFragment();
        }

        new WeekendLeagueDetailPresenter(Injection.provideWeekendLeagueRepository(getApplicationContext()), weekendLeagueDetailFragment);

        selectFragment(selectedItem);

    }

    private void selectFragment(MenuItem item) {
        Log.d(TAG, "selectFragment: " + item.getTitle().toString());
        Fragment frag = null;
        String tag = "";
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.menu_past_wl:
                if(pastWLFragment!=null){
                    frag = pastWLFragment;
                }
                else{
                    pastWLFragment = new PastWLFragment();
                    frag = pastWLFragment;
                }
                tag = "past_weekend_league_frag";
                break;
            case R.id.menu_current_wl:
                if(weekendLeagueDetailFragment !=null){
                    frag = weekendLeagueDetailFragment;
                }
                else{
                    weekendLeagueDetailFragment = new WeekendLeagueDetailFragment();
                    frag = weekendLeagueDetailFragment;
                }
                tag = "weekend_league_frag";
                break;
            case R.id.menu_my_squads:

                if(mySquadsFragment!=null){
                    frag = mySquadsFragment;
                }
                else{
                    mySquadsFragment = new MySquadsFragment();
                    frag = mySquadsFragment;
                }
                tag = "my_squads_frag";
                new MySquadsPresenter(Injection.provideSquadsRepository(getApplicationContext()), mySquadsFragment);
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
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // removes the existing fragment calling onDestroy

            ft.replace(R.id.container, fragment);
            if (tag != null) ft.addToBackStack(tag);
            ft.commit();
        }
    }

    /**
     *  **************************** Weekend League Fragment interactions ****************************
     */

    @Override
    public void onWLNewGameInteraction() {
        newGameFragment = new NewGameFragment();
        new NewGamePresenter(Injection.provideSquadsRepository(getApplicationContext()),
                Injection.provideWeekendLeagueRepository(getApplicationContext()),
                newGameFragment);

        displayFragment(newGameFragment, "new_game_frag");
    }

    @Override
    public void onWLViewGamesInteraction() {
        viewGamesFragment = new ViewGamesFragment();
        new ViewGamesPresenter(Injection.provideWeekendLeagueRepository(getApplicationContext()), viewGamesFragment);

        displayFragment(viewGamesFragment, "view_games_frag");
    }

    /**
     *  **************************** NewGameFragment Interactions ****************************
     */

    @Override
    public void onNewGameBackPressed() {
        onBackPressed();
    }

    @Override
    public void onNewGameSaved() {
        onBackPressed();
    }

    /**
     * **************************** ViewGamesFragment Interactions ****************************
     */

    @Override
    public void showEditGame(Game game, int position) {

        editGameFragment = new EditGameFragment();
        new EditGamePresenter(Injection.provideWeekendLeagueRepository(getApplicationContext()), game, editGameFragment, position);

        displayFragment(editGameFragment, "edit_game_frag");
    }

    @Override
    public void onViewGamesBackPressed() {
        onBackPressed();
    }

    /**
     * **************************** EditGamesFragment Interactions ****************************
     */

    @Override
    public void onEditGameSaved() {
        onBackPressed();
    }

    @Override
    public void onEditGameBackPressed() {
        onBackPressed();
    }

    /**
     *  **************************** Past WL Interactions ****************************
     */

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

            if(args.containsKey(Constants.VIEW_PAST_WLS)){

                    viewPastWLsFragment = new ViewPastWLsFragment();
                    displayFragment(viewPastWLsFragment, "view_past_wls");

            }
        }
    }

    @Override
    public void onViewPastWLsFragmentInteraction(Bundle args) {
        if(args!=null){
            if(args.containsKey(Constants.BACK_BTN)){
                onBackPressed();
            }
            if(args.containsKey(Constants.VIEW_WL)){
                WeekendLeague weekendLeague = (WeekendLeague) args.getSerializable(Constants.VIEW_WL);

                Bundle b = new Bundle();
                b.putSerializable(Constants.VIEW_SELECTED_PAST_WL, weekendLeague);
                viewSelectedWLFragment = new ViewSelectedWLFragment();
                viewSelectedWLFragment.setArguments(b);
                displayFragment(viewSelectedWLFragment, "view_selected_past_wl");

            }
            if(args.containsKey(Constants.VIEW_PAST_WL_GAME)){
                Game game = (Game) args.getSerializable(Constants.VIEW_PAST_WL_GAME);

                Bundle b = new Bundle();
                b.putSerializable(Constants.VIEW_PAST_WL_GAME, game);
                pastWLViewGameFragment = new PastWLViewGameFragment();
                pastWLViewGameFragment.setArguments(b);
                displayFragment(pastWLViewGameFragment, "view_past_wl_game");
            }

        }
    }

    @Override
    public void onViewPastWLGamesFragmentInteraction(Bundle args) {
        if(args!=null){
            if(args.containsKey(Constants.BACK_BTN)){
                onBackPressed();
            }
            if(args.containsKey(Constants.VIEW_PAST_WL_GAME)){
                Game game = (Game) args.getSerializable(Constants.VIEW_PAST_WL_GAME);

                Bundle b = new Bundle();
                b.putSerializable(Constants.VIEW_PAST_WL_GAME, game);
                pastWLViewGameFragment = new PastWLViewGameFragment();
                pastWLViewGameFragment.setArguments(b);
                displayFragment(pastWLViewGameFragment, "view_past_wl_game");

            }
        }
    }

    @Override
    public void onPastWLViewGameFragmentInteraction(Bundle args) {
        if(args!=null){
            if(args.containsKey(Constants.BACK_BTN)){
                onBackPressed();
            }
        }
    }

    @Override
    public void onViewSelectedWLFragmentInteraction(Bundle args) {
        if(args!=null){
            if(args.containsKey(Constants.BACK_BTN)){
                onBackPressed();
            }
            if(args.containsKey(Constants.VIEW_PAST_WL_GAMES)){
                WeekendLeague weekendLeague = (WeekendLeague) args.getSerializable(Constants.VIEW_PAST_WL_GAMES);
                Bundle b = new Bundle();
                b.putSerializable(Constants.VIEW_PAST_WL_GAMES, weekendLeague);
                viewPastWLGamesFragment = new ViewPastWLGamesFragment();
                viewPastWLGamesFragment.setArguments(b);
                displayFragment(viewPastWLGamesFragment, "view_past_wl_games");
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

        if(weekendLeagueDetailFragment !=null && weekendLeagueDetailFragment.isVisible()){
            //do nothing
        }
        else{
            MenuItem homeItem = mBottomNav.getMenu().getItem(1);
            if (mSelectedItem != homeItem.getItemId()) {
                Log.d(TAG, "onBackPressed: selectedItem!=homeItem");
                // select home item

                if((pastWLFragment!=null && pastWLFragment.isVisible()) ||
                        mySquadsFragment!=null && mySquadsFragment.isVisible()){
                    Log.d(TAG, "onBackPressed: pastWl || my squads is visible");
                    super.onBackPressed();
                    selectFragment(homeItem);
                }
                else if(viewPastWLsFragment!=null && viewPastWLsFragment.isVisible()){
                    if(viewPastWLsFragment.handleBackPress()==0){
                        super.onBackPressed();
                    }
                    else{
                        //handled in fragment, hiding searchview because its visible, instead of going back
                    }
                }
                else{
                    Log.d(TAG, "onBackPressed: not pastWl || mysquads");
                    super.onBackPressed();
                }
            } else {
                Log.d(TAG, "onBackPressed: selectedItem == homeItem");
                super.onBackPressed();
            }
        }


    }
}
