package com.futchampionsstats.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.futchampionsstats.R;
import com.futchampionsstats.Utils.Constants;
import com.futchampionsstats.Utils.Utils;
import com.futchampionsstats.databinding.FragmentNewGameBinding;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.Squad;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class NewGameFragment extends Fragment {

    public static final String TAG = NewGameFragment.class.getSimpleName();

    private static OnNewGameFragmentInteractionListener mListener;
    private static boolean warningShown = false;
    FragmentNewGameBinding binding;

    private static ArrayList<Squad> user_squads;
    private static List<String> formations;

    public NewGameFragment() {
        // Required empty public constructor
    }


    public static NewGameFragment newInstance(String param1, String param2) {
        NewGameFragment fragment = new NewGameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_game, container, false);
        NewGameHandlers handlers = new NewGameHandlers();
        Game mGame = new Game();
        user_squads = new ArrayList<>();

        binding.setGame(mGame);
        binding.setHandlers(handlers);
        binding.setSquads(user_squads);

        /** Spinner data **/
        setSpinnerData(binding.getRoot());

        return binding.getRoot();
    }

    public static class NewGameHandlers {

        public void onClick(View view){

            Bundle b = new Bundle();
            switch(view.getId()) {
                case R.id.back_btn:
                    Utils.hideKeyboard(view.getContext(), view.getWindowToken());
                    b.putSerializable(Constants.BACK_BTN, Constants.BACK_BTN);
                    break;
            }
            if(mListener!=null) mListener.onNewGameFragmentInteraction(b);
        }

        @BindingAdapter({"UserPossessionWatcher"})
        public static void userPossessionWatcher(EditText view, final Game game) {
            if (game != null && view != null) {
                view.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 0) {
                            int userPoss = Integer.parseInt(s.toString());
                            int oppPoss = Math.abs(userPoss - 100);
                            game.setOpp_possession(Integer.toString(oppPoss));
                        }
                    }
                });
            }

        }

        @BindingAdapter({"OppPossessionWatcher"})
        public static void oppPossessionWatcher(EditText view, final Game game) {
            if (game != null && view != null) {
                view.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 0) {
                            int oppPoss = Integer.parseInt(s.toString());
                            int userPoss = Math.abs(oppPoss - 100);
                            game.setUser_possession(Integer.toString(userPoss));
                        }
                    }
                });
            }

        }

        @BindingAdapter({"UserCreateNewTeamWatcher"})
        public static void userCreateNewTeamWatcher(final Button btn, final Game game) {
            if (game != null && btn != null) {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle b = new Bundle();
                        b.putString(Constants.NEW_SQUAD, Constants.NEW_SQUAD);
                        if(mListener!=null) mListener.onNewGameFragmentInteraction(b);

                    }
                });

            }
        }

        @BindingAdapter({"UserTeamSelectorWatcher"})
        public static void userTeamSelectorWatcher(final MaterialSpinner spinner, final Game game) {
//            Log.d(TAG, "oppFormationWatcher: " + spinner.getSelectedItem().toString());
            if (game != null && spinner != null) {

                if(user_squads!=null && user_squads.size()>0){
                    game.setUser_team(user_squads.get(spinner.getSelectedIndex()).getName());
                    game.setUser_team_rating(user_squads.get(spinner.getSelectedIndex()).getTeam_rating());
                    game.setUser_formation(user_squads.get(spinner.getSelectedIndex()).getFormation());
                }

                spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int pos, long id, Object item) {
                        Log.d(TAG, "User onItemSelected: " + item.toString());

                        game.setUser_team(user_squads.get(pos).getName());
                        game.setUser_team_rating(user_squads.get(pos).getTeam_rating());
                        game.setUser_formation(user_squads.get(pos).getFormation());
                    }
                });
            }

        }

        @BindingAdapter({"OppFormationWatcher"})
        public static void oppFormationWatcher(final MaterialSpinner spinner, final Game game) {
//            Log.d(TAG, "oppFormationWatcher: " + spinner.getSelectedItem().toString());
            if (game != null && spinner != null) {
                game.setOpp_formation(formations.get(spinner.getSelectedIndex()));

                spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int pos, long id, Object item) {
                        Log.d(TAG, "User onItemSelected: " + item.toString());
                        game.setOpp_formation(item.toString());
                    }
                });
            }

        }

        @BindingAdapter({"GameDisconnectWatcher"})
        public static void gameDisconnectWatcher(final CheckBox checkbox, final Game game) {

            if (game != null && checkbox != null) {
                checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(checkbox.isChecked()){
                            game.setGame_disconnected(true);
                            showDisconnectWarning(checkbox);
                        }
                        else{
                            game.setGame_disconnected(false);
                        }
                    }
                });
            }

        }

        @BindingAdapter({"OnGameFinished"})
        public static void onGameFinished(final Button btn, final Game game) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (game != null) {
                        Log.d(TAG, "onClick: " + new Gson().toJson(game));
                        String gameDataCheck = checkIfGameDataCorrect(game);
//                        String gameDataCheck = "finished";
                        game.setGame_id("wl_game");
                        if (gameDataCheck.equals("finished")) {
                            if(Integer.parseInt(game.getUser_goals()) > Integer.parseInt(game.getOpp_goals())){
                                game.setUser_won(true);
                            }
                            else{
                                game.setUser_won(false);
                            }
                            Log.d(TAG, "FinishedOnClick: " + new Gson().toJson(game));
                            Utils.hideKeyboard(btn.getContext(), btn.getWindowToken());
                            Bundle b = new Bundle();
                            b.putSerializable(Constants.NEW_GAME, game);
                            if(mListener!=null) mListener.onNewGameFragmentInteraction(b);
                        } else if(gameDataCheck.equals("finished in pens")){
                            if(Integer.parseInt(game.getUser_pen_score()) > Integer.parseInt(game.getOpp_pen_score())){
                                game.setUser_won(true);
                            }
                            else{
                                game.setUser_won(false);
                            }
                            Log.d(TAG, "FinishedOnClick: " + new Gson().toJson(game));
                            Bundle b = new Bundle();
                            b.putSerializable(Constants.NEW_GAME, game);
                            if(mListener!=null) mListener.onNewGameFragmentInteraction(b);
                        }
                        else if(gameDataCheck.equals("Disconnect")){
                            game.setUser_won(false);
                            Bundle b = new Bundle();
                            b.putSerializable(Constants.NEW_GAME, game);
                            if(mListener!=null) mListener.onNewGameFragmentInteraction(b);
                        }
                        else{
                            Toast.makeText(btn.getContext(), gameDataCheck, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });


        }


    }

    private static String checkIfGameDataCorrect(Game game){
        if(game.getGame_disconnected()){
            return "Disconnect";
        }
        else if(game.checkIfNotNull()){
            if(game.getUser_goals().equals(game.getOpp_goals())){
                if(!game.isPenalties()){
                    //game score is tie, but penalties not set, return error
                    return "Game score was a tie but no penalties were filled out!";
                }else{
                    //game score is tie, penalties are checked
                    if((game.getUser_pen_score()!=null && game.getOpp_pen_score()!=null) && !game.getUser_pen_score().equals(game.getOpp_pen_score())){
                     //pen scores do not equal eachother && are not null, meaning there is a winner
                        return "finished in pens";
                    } else{
                        //pens not filled in or equal
                        return "Penalties not correctly set!";
                    }
                }
            }
            else{
                //score indicates a winner
                return "finished";
            }

        }else{
            //values are not set
            return "One or more fields were missed! Please fill out all fields to continue!";
        }
    }

    private void setSpinnerData(View view){

        String[] myResArray = getResources().getStringArray(R.array.formations_array);
        formations = Arrays.asList(myResArray);

        MaterialSpinner opponentFormationSpinner = (MaterialSpinner) view.findViewById(R.id.opponent_formation_edit);
        opponentFormationSpinner.setItems(formations);
        opponentFormationSpinner.setSelectedIndex(0);
    }

    private void setUserTeamSpinner(View view){
        MaterialSpinner userTeamUsed = (MaterialSpinner) view.findViewById(R.id.user_team_used);
        List<String> teams = new ArrayList<>();
        for (int i = 0; i < user_squads.size(); i++) {
            teams.add(user_squads.get(i).getName());
        }
        userTeamUsed.setItems(teams);
        userTeamUsed.setSelectedIndex(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(Constants.SAVED_SQUADS, null);
        Type type = new TypeToken<ArrayList<Squad>>() {}.getType();
        user_squads = gson.fromJson(json, type);

        if (user_squads != null && user_squads.size()>0) {
            Log.d(TAG, "onResume viewSquads: " + new Gson().toJson(user_squads));
            binding.setSquads(user_squads);
            setUserTeamSpinner(binding.getRoot());
        }
    }

    private static void showDisconnectWarning(View v) {

        if (!warningShown) {

            SweetAlertDialog pDialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.NORMAL_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Disconnected from EA Servers?");
            pDialog.setContentText("If checked, this game's stats will not count towards averages and are not needed to complete this game but you may still add stats in for your own information.");
            pDialog.setConfirmText("OK");
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    Log.d(TAG, "onClick: ok");
                    warningShown = true;
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(true);
            pDialog.show();

        }
    }

    // TODO: 2/10/17 Refactor this into separate method where can be used here and MySquadsFragment
    public void saveSquad(Squad squad){

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(Constants.SAVED_SQUADS, null);
        Type type = new TypeToken<ArrayList<Squad>>() {}.getType();
        ArrayList<Squad> saved_squads = gson.fromJson(json, type);

        if(saved_squads!=null){
            saved_squads.add(squad);
        }
        else{
            saved_squads = new ArrayList<>();
            saved_squads.add(squad);
        }
        user_squads = saved_squads;
        binding.setSquads(user_squads);
        setUserTeamSpinner(binding.getRoot());

        SharedPreferences.Editor editor = sharedPrefs.edit();
        String json2 = gson.toJson(saved_squads);
        Log.d(TAG, "saveSquad: " + saved_squads);

        editor.putString(Constants.SAVED_SQUADS, json2);
        editor.apply();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewGameFragmentInteractionListener) {
            mListener = (OnNewGameFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNewGameFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnNewGameFragmentInteractionListener {
        void onNewGameFragmentInteraction(Bundle args);
    }
}
