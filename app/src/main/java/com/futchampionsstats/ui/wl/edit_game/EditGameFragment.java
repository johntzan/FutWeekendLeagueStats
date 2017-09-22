package com.futchampionsstats.ui.wl.edit_game;


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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.futchampionsstats.R;
import com.futchampionsstats.databinding.FragmentEditGameBinding;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.utils.Utils;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.google.common.base.Preconditions.checkNotNull;

public class EditGameFragment extends Fragment implements EditGameContract.View{

    public static final String TAG = EditGameFragment.class.getSimpleName();
    private static OnEditGameFragmentInteractionListener mListener;

    private FragmentEditGameBinding binding;
    private static boolean warningShown = false;
    List<String> formations;

    private EditGameContract.Presenter mPresenter;

    public EditGameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_game, container, false);
        EditGameFragment.EditGameHandlers handlers = new EditGameFragment.EditGameHandlers();

        binding.setHandlers(handlers);

        setupGameDisconnectCheckbox();

        String[] myResArray = getResources().getStringArray(R.array.formations_array);
        formations = Arrays.asList(myResArray);

        return binding.getRoot();
    }

    @Override
    public void setPresenter(@NonNull EditGameContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setGameForEdit(Game game) {
        binding.setGame(game);
        Log.d(TAG, "Game: " + new Gson().toJson(game));
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
    public void setFormationSpinner(int oppFormationIndex) {

        MaterialSpinner opponentFormationSpinner = (MaterialSpinner) binding.editOpponentFormationEdit;
        opponentFormationSpinner.setItems(formations);
        opponentFormationSpinner.setSelectedIndex(oppFormationIndex);

        opponentFormationSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int pos, long id, Object item) {
                Log.d(TAG, "User onItemSelected: " + item.toString());
                mPresenter.setOpponentFormation(item.toString());
            }
        });
    }

    @Override
    public void setSquadToggle(int squadToggle) {

        ToggleButton eplToggle = binding.eplToggle;
        ToggleButton serieaToggle = binding.serieaToggle;
        ToggleButton bundesToggle = binding.bundesToggle;
        ToggleButton laligaToggle = binding.laligaToggle;
        ToggleButton ligue1Toggle = binding.ligue1Toggle;
        ToggleButton hybridToggle = binding.hybridToggle;
        ToggleButton otherToggle = binding.otherToggle;
        ToggleButton oneNationToggle = binding.oneNationToggle;

        switch(squadToggle){
            case 0:
                eplToggle.setChecked(true);
                break;
            case 1:
                serieaToggle.setChecked(true);
                break;
            case 2:
                bundesToggle.setChecked(true);
                break;
            case 3:
                laligaToggle.setChecked(true);
                break;
            case 4:
                ligue1Toggle.setChecked(true);
                break;
            case 5:
                hybridToggle.setChecked(true);
                break;
            case 6:
                otherToggle.setChecked(true);
                break;
            case 7:
                oneNationToggle.setChecked(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void saveGameSuccess() {
        if(mListener!=null) mListener.onEditGameSaved();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void getOppFormationIndex(Game game) {
        int index = 0;
        for (int i = 0; i < formations.size(); i++) {
            if(formations.get(i).equals(game.getOpp_formation())){
                index = i;
            }
        }
        Log.d(TAG, "getFormationIndex opp: " + formations.get(index));

        setFormationSpinner(index);
    }

    public static class EditGameHandlers {

        public void goBack(View view){
            Utils.hideKeyboard(view.getContext(), view.getWindowToken());
            if(mListener!=null) mListener.onEditGameBackPressed();
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

    }

    private void setupGameDisconnectCheckbox(){
        CheckBox mDisconnectCheckbox = binding.editGameDisconnectCheck;

        mDisconnectCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        checkNotNull(mPresenter).start();

        View content = getActivity().findViewById(R.id.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) content.findViewById(R.id.navigation);
        if(bottomNavigationView!=null){
            bottomNavigationView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEditGameFragmentInteractionListener) {
            mListener = (OnEditGameFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEditGameFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        View content = getActivity().findViewById(R.id.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) content.findViewById(R.id.navigation);
        if(bottomNavigationView!=null){
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    public interface OnEditGameFragmentInteractionListener {
        void onEditGameSaved();
        void onEditGameBackPressed();
    }

}
