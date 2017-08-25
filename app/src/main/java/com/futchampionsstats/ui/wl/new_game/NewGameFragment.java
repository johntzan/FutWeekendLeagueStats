package com.futchampionsstats.ui.wl.new_game;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.futchampionsstats.FutChampsApplication;
import com.futchampionsstats.R;
import com.futchampionsstats.databinding.FragmentNewGameBinding;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.models.Squad;
import com.futchampionsstats.utils.Utils;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.google.common.base.Preconditions.checkNotNull;


public class NewGameFragment extends Fragment implements NewGameContract.View {

    public static final String TAG = NewGameFragment.class.getSimpleName();

    private static OnNewGameFragmentInteractionListener mListener;
    private static boolean warningShown = false;
    FragmentNewGameBinding binding;

    private static List<String> formations;

    private NewGameContract.Presenter mPresenter;

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
        binding.setHandlers(handlers);
        setupGameDisconnectCheckbox();

        if(mPresenter!=null) {
            mPresenter.start();
        }
        else{
            mPresenter = new NewGamePresenter( ( (FutChampsApplication) getContext().getApplicationContext()).getSquadRepositoryComponent().getSquadRepository(),
                 ((FutChampsApplication) getContext().getApplicationContext()).getWeekendLeagueRepository().getWeekendLeagueRepository(),
                 this);
            mPresenter.start();
        }

        return binding.getRoot();
    }

    @Override
    public void setPresenter(@NonNull NewGameContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setNewGameToView(Game game) {
        binding.setGame(game);
    }

    @Override
    public void setUserSquads(ArrayList<Squad> squads) {
        binding.setSquads(squads);

        MaterialSpinner userTeamUsed =  binding.userTeamUsed;
        if(squads.size()>0){
            final List<String> teams = new ArrayList<>();
            for (int i = 0; i < squads.size(); i++) {
                teams.add(squads.get(i).getName());
            }
            userTeamUsed.setItems(teams);
            userTeamUsed.setSelectedIndex(0);

            userTeamUsed.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    mPresenter.setUserTeamUsing(position);
                }
            });
        }
    }

    @Override
    public void setFormationSpinner() {
        String[] myResArray = getResources().getStringArray(R.array.formations_array);
        formations = Arrays.asList(myResArray);

        MaterialSpinner opponentFormationSpinner = binding.opponentFormationEdit;
        opponentFormationSpinner.setItems(formations);
        opponentFormationSpinner.setSelectedIndex(0);
        //default to first formation
        mPresenter.setOpponentFormation(formations.get(0));

        opponentFormationSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int pos, long id, Object item) {
                Log.d(TAG, "User onItemSelected: " + item.toString());
                mPresenter.setOpponentFormation(item.toString());
            }
        });
    }

    @Override
    public void showDisconnectWarning() {

        if (!warningShown) {

            SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);
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
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNewSquadDialog(View view) {

        final AlertDialog add_new_squad_dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        // Get the layout inflater
        LayoutInflater inflater = LayoutInflater.from(view.getContext());

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

                    mPresenter.saveNewSquad(new_squad);

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

    @Override
    public void saveGameSuccess() {
        if(mListener!=null) mListener.onNewGameSaved();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


    public static class NewGameHandlers {

        public void goBack(View view){
            Utils.hideKeyboard(view.getContext(), view.getWindowToken());
            if(mListener!=null) mListener.onNewGameBackPressed();
        }

        // TODO: 3/29/17 Make separate class for databinding adapters, put below inside
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

    }

    private void setupGameDisconnectCheckbox(){
        CheckBox mDisconnectCheckbox = binding.gameDisconnectCheck;

        mDisconnectCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    mPresenter.setDisconnected();
                }
                else{
                    //not checked
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.setPresenter(mPresenter);

    }

    @Override
    public void onResume() {
        super.onResume();

        View content = getActivity().findViewById(R.id.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) content.findViewById(R.id.navigation);
        if(bottomNavigationView!=null){
            bottomNavigationView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

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
        View content = getActivity().findViewById(R.id.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) content.findViewById(R.id.navigation);
        if(bottomNavigationView!=null){
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        mListener = null;
    }

    public interface OnNewGameFragmentInteractionListener {
        void onNewGameBackPressed();
        void onNewGameSaved();
    }
}
