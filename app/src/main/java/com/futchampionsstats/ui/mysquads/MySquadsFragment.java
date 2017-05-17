package com.futchampionsstats.ui.mysquads;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.futchampionsstats.R;
import com.futchampionsstats.adapters.SquadListAdapter;
import com.futchampionsstats.databinding.FragmentMySquadsBinding;
import com.futchampionsstats.models.Squad;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MySquadsFragment extends Fragment implements MySquadsContract.View{

    public static final String TAG = MySquadsFragment.class.getSimpleName();

    private OnMySquadsFragmentInteractionListener mListener;
    private SquadListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView squadsList;

    private FragmentMySquadsBinding mMySquadsBinding;
    private MySquadsContract.Presenter mPresenter;

    public MySquadsFragment() {
        // Required empty public constructor
    }


    public static MySquadsFragment newInstance() {
        MySquadsFragment fragment = new MySquadsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMySquadsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_squads, container, false);
        MySquadsHandlers handlers = new MySquadsHandlers();
        mMySquadsBinding.setHandlers(handlers);

        squadsList = (RecyclerView) mMySquadsBinding.getRoot().findViewById(R.id.squads_list);

        return mMySquadsBinding.getRoot();
    }

    private void setupAdapter(final ArrayList<Squad> squads){
        mLayoutManager = new LinearLayoutManager(getActivity());

        squadsList.setLayoutManager(mLayoutManager);
        squadsList.setItemAnimator(new DefaultItemAnimator());

        OnItemTouchListener itemTouchListener = new OnItemTouchListener() {

            @Override
            public void onEditClick(View view, int position) {
                Log.d(TAG, "onEdit: " + position);
                mPresenter.setSquadForEdit(position);
            }

            @Override
            public void onDeleteClick(View view, int position) {
                Log.d(TAG, "onDelete: " + position);
                mPresenter.deleteSquad(position);
            }
        };

        mAdapter = new SquadListAdapter(getActivity(), squads, itemTouchListener);
        squadsList.setAdapter(mAdapter);
    }

    public class MySquadsHandlers{

        public void infoBtnClick(View v){
            showMySquadsInfoDialog();
        }
        public void newSquadBtnClick(View v){
            showAddNewSquadDialog(v);
        }

    }

    @Override
    public void setPresenter(MySquadsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setSquads(ArrayList<Squad> squads) {
        mMySquadsBinding.setEmptySquads(false);
        setupAdapter(squads);
    }

    @Override
    public void showEmptySquads() {
        mMySquadsBinding.setEmptySquads(true);
    }

    @Override
    public void showEditSquad(Squad edit_squad, final int squad_index) {

        if (edit_squad != null) {

            final AlertDialog add_new_squad_dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            // Get the layout inflater
            LayoutInflater inflater = LayoutInflater.from(getContext());

            View new_squad_dialog = inflater.inflate(R.layout.add_new_squad_dialog, null);

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setCancelable(true);
            builder.setView(new_squad_dialog);
            builder.create();
            add_new_squad_dialog = builder.show();

            TextView dialog_title = (TextView) new_squad_dialog.findViewById(R.id.add_new_squad_title);
            dialog_title.setText(getActivity().getString(R.string.edit_squad));

            final MaterialEditText squad_name = (MaterialEditText) new_squad_dialog.findViewById(R.id.squad_name_edit);
            final MaterialEditText squad_team_rating = (MaterialEditText) new_squad_dialog.findViewById(R.id.squad_team_rating_edit);
            Button add_squad_btn = (Button) new_squad_dialog.findViewById(R.id.new_squad_dialog_add);
            add_squad_btn.setText(getActivity().getString(R.string.confirm_edit));
            Button cancel_btn = (Button) new_squad_dialog.findViewById(R.id.new_squad_dialog_cancel);

            final MaterialSpinner squadFormationSpinner = (MaterialSpinner) new_squad_dialog.findViewById(R.id.squad_formation_edit);
            String[] myResArray = getResources().getStringArray(R.array.formations_array);
            final List<String> formations = Arrays.asList(myResArray);

            squadFormationSpinner.setItems(formations);

            int edit_formation_index = 0;

            for (int i = 0; i < formations.size(); i++) {
                if (formations.get(i).equals(edit_squad.getFormation())) {
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
                    if (squad_name.getText().toString().length() < 1) {
                        squad_name.setError("Please fill out this field!");
                    } else if (squad_team_rating.getText().toString().length() < 1) {
                        squad_team_rating.setError("Please fill out this field!");
                    } else {
                        new_squad.setName(squad_name.getText().toString());
                        new_squad.setTeam_rating(squad_team_rating.getText().toString());
                        new_squad.setFormation(formations.get(squadFormationSpinner.getSelectedIndex()));

                        mPresenter.editSquad(new_squad, squad_index);
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

    @Override
    public void showMySquadsInfoDialog() {

        SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("My Squads Info");
        pDialog.setContentText("Create new squads, Edit by clicking on the squad and Delete by swiping on the squad and pressing delete.");
        pDialog.setConfirmText("Close");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(true);
        pDialog.show();

    }

    @Override
    public void showAddNewSquadDialog(View view) {

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

                    mPresenter.addNewSquad(new_squad);

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
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //set presenter to binding -- not needed for now

    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMySquadsFragmentInteractionListener) {
            mListener = (OnMySquadsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMySquadsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnItemTouchListener {
         void onEditClick(View view, int position);
         void onDeleteClick(View view, int position);
    }

    public interface OnMySquadsFragmentInteractionListener {

    }
}
