package com.futchampionsstats.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futchampionsstats.R;
import com.futchampionsstats.Utils.Constants;
import com.futchampionsstats.adapters.SquadListAdapter;
import com.futchampionsstats.databinding.FragmentMySquadsBinding;
import com.futchampionsstats.models.Squad;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MySquadsFragment extends Fragment {

    public static final String TAG = MySquadsFragment.class.getSimpleName();

    private OnMySquadsFragmentInteractionListener mListener;
    private ArrayList<Squad> mySquads;
    private SquadListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView squadsList;

    private FragmentMySquadsBinding binding;

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
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_squads, container, false);
        MySquadsHandlers handlers = new MySquadsHandlers();
        binding.setHandlers(handlers);

        squadsList = (RecyclerView) binding.getRoot().findViewById(R.id.squads_list);

        return binding.getRoot();
    }

    private void setupAdapter(){
        mLayoutManager = new LinearLayoutManager(getActivity());

        squadsList.setLayoutManager(mLayoutManager);
        squadsList.setItemAnimator(new DefaultItemAnimator());

        OnItemTouchListener itemTouchListener = new OnItemTouchListener() {

            @Override
            public void onEditClick(View view, int position) {
                Log.d(TAG, "onEdit: " + position);
                Bundle b = new Bundle();
                b.putInt(Constants.EDIT_SQUAD_INDEX, position);
                b.putSerializable(Constants.EDIT_SQUAD, mySquads.get(position));
                if(mListener!=null) mListener.onMySquadsFragmentInteraction(b);
            }

            @Override
            public void onDeleteClick(View view, int position) {
                Log.d(TAG, "onDelete: " + position);
                deleteSquad(position);
            }
        };

        mAdapter = new SquadListAdapter(getActivity(), mySquads, itemTouchListener);
        squadsList.setAdapter(mAdapter);
    }

    public class MySquadsHandlers{

        public void onClick(View view){

            Bundle b = new Bundle();
            switch(view.getId()) {
                case R.id.back_btn:
                    b.putString(Constants.BACK_BTN, Constants.BACK_BTN);
                    break;
                case R.id.new_squad_btn:
                    Log.d(TAG, "onClick: new squad");
                    b.putString(Constants.NEW_SQUAD, Constants.NEW_SQUAD);
                    break;
                case R.id.squads_info_btn:
                    SweetAlertDialog pDialog = new SweetAlertDialog(view.getContext(), SweetAlertDialog.NORMAL_TYPE);
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
                    break;
            }
            if(mListener!=null) mListener.onMySquadsFragmentInteraction(b);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(Constants.SAVED_SQUADS, null);
        Type type = new TypeToken<ArrayList<Squad>>() {}.getType();
        ArrayList<Squad> squads = gson.fromJson(json, type);

        if(squads!=null){
            Log.d(TAG, "onResume viewSquads: " + new Gson().toJson(squads));
            mySquads = squads;
            binding.setSquads(mySquads);

            setupAdapter();
        }

    }

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
        mySquads = saved_squads;
        binding.setSquads(mySquads);
        mAdapter.updateList(mySquads);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        String json2 = gson.toJson(saved_squads);
        Log.d(TAG, "saveSquad: " + saved_squads);

        editor.putString(Constants.SAVED_SQUADS, json2);
        editor.apply();
    }

    public void saveEditSquad(Squad squad, int squad_index){

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(Constants.SAVED_SQUADS, null);
        Type type = new TypeToken<ArrayList<Squad>>() {}.getType();
        ArrayList<Squad> saved_squads = gson.fromJson(json, type);

        if(saved_squads!=null){
            saved_squads.set(squad_index, squad);
        }

        mySquads = saved_squads;
        binding.setSquads(mySquads);
        mAdapter.updateList(mySquads);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        String json2 = gson.toJson(saved_squads);
        Log.d(TAG, "editSquad: " + saved_squads);

        editor.putString(Constants.SAVED_SQUADS, json2);
        editor.apply();
    }

    public void deleteSquad(int squad_index){

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(Constants.SAVED_SQUADS, null);
        Type type = new TypeToken<ArrayList<Squad>>() {}.getType();
        ArrayList<Squad> saved_squads = gson.fromJson(json, type);

        if(saved_squads!=null){
            saved_squads.remove(squad_index);
        }

        mySquads = saved_squads;
        binding.setSquads(mySquads);
        mAdapter.removeFromList(squad_index);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        String json2 = gson.toJson(saved_squads);
        Log.d(TAG, "editSquad: " + saved_squads);

        editor.putString(Constants.SAVED_SQUADS, json2);
        editor.apply();
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
        public void onEditClick(View view, int position);
        public void onDeleteClick(View view, int position);
    }

    public interface OnMySquadsFragmentInteractionListener {
        void onMySquadsFragmentInteraction(Bundle args);
    }
}
