package com.futchampionsstats.main;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.futchampionsstats.R;
import com.futchampionsstats.Utils.Constants;
import com.futchampionsstats.Utils.Utils;
import com.futchampionsstats.databinding.FragmentNewGameBinding;
import com.futchampionsstats.models.Game;
import com.google.gson.Gson;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.ContentValues.TAG;


public class NewGameFragment extends Fragment {


    private static OnNewGameFragmentInteractionListener mListener;
    private static boolean warningShown = false;

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
        FragmentNewGameBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_game, container, false);
        NewGameHandlers handlers = new NewGameHandlers();
        Game mGame = new Game();
        binding.setGame(mGame);
        binding.setHandlers(handlers);

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

        @BindingAdapter({"UserFormationWatcher"})
        public static void userFormationWatcher(final Spinner spinner, final Game game) {
//            Log.d(TAG, "oppFormationWatcher: " + spinner.getSelectedItem().toString());
            if (game != null && spinner != null) {
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        Log.d(TAG, "User onItemSelected: " + item.toString());
                        game.setUser_formation(item.toString());
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

        }

        @BindingAdapter({"OppFormationWatcher"})
        public static void oppFormationWatcher(final Spinner spinner, final Game game) {
//            Log.d(TAG, "oppFormationWatcher: " + spinner.getSelectedItem().toString());
            if (game != null && spinner != null) {
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        Log.d(TAG, "Opp onItemSelected: " + item.toString());
                        game.setOpp_formation(item.toString());
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
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
        Spinner userFormationSpinner = (Spinner) view.findViewById(R.id.user_formation_edit);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> user_adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.formations_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        user_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        userFormationSpinner.setAdapter(user_adapter);

        Spinner opponentFormationSpinner = (Spinner) view.findViewById(R.id.opponent_formation_edit);
        ArrayAdapter<CharSequence> opponent_adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.formations_array, android.R.layout.simple_spinner_item);
        opponent_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opponentFormationSpinner.setAdapter(opponent_adapter);
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
