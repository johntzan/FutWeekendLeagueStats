package com.futchampionsstats.ui.wl;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futchampionsstats.R;
import com.futchampionsstats.databinding.FragmentWlBinding;
import com.futchampionsstats.models.WeekendLeague;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.google.common.base.Preconditions.checkNotNull;

public class WeekendLeagueDetailFragment extends Fragment implements WeekendLeagueDetailContract.View{

    public static final String TAG = WeekendLeagueDetailFragment.class.getSimpleName();

    private OnNewWLFragmentInteractionListener mListener;
    private WeekendLeagueDetailContract.Presenter mPresenter;
    private FragmentWlBinding mWlBinding;

    public WeekendLeagueDetailFragment() {
        // Required empty public constructor
    }


    public static WeekendLeagueDetailFragment newInstance(WeekendLeague wl) {
        WeekendLeagueDetailFragment fragment = new WeekendLeagueDetailFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wl, container, false);

        mWlBinding = DataBindingUtil.bind(view);
        WLFragmentHandlers handlers = new WLFragmentHandlers();

        mWlBinding.setHandlers(handlers);

        return view;
    }

    public class WLFragmentHandlers{

        public void viewGamesButtonClick(View v){
            if(mListener!=null) mListener.onWLViewGamesInteraction();
        }

        public void infoBtnClick(View v){
            if(mListener!=null) mListener.goToMainAppInfo();
        }

        public void saveWeekendLeagueButtonClick(View v){
            showSaveWLDialog();
        }
        public void clearWeekendLeagueButtonClick(View v){
            showClearWLDialog();
        }
    }

    @Override
    public void setPresenter(@NonNull WeekendLeagueDetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showCurrentWeekendLeague(WeekendLeague weekendLeague) {
        mWlBinding.setWeekendLeague(weekendLeague);
    }

    @Override
    public void showEmptyWeekendLeague(WeekendLeague weekendLeague) {
        mWlBinding.setWeekendLeague(weekendLeague);
    }

    @Override
    public void showSaveWLDialog() {

        SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Save this Weekend League?");
        pDialog.setContentText("Are you sure you would like to save this Weekend League? Doing so will clear out current Weekend League.");
        pDialog.setConfirmText("Yes");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Log.d(TAG, "onClick: yes");
                    String date = new SimpleDateFormat("MM-dd-yyyy", Locale.US).format(new Date());
                    Log.d(TAG, "onClick: Current time => " + date);

                    sweetAlertDialog
                            .setTitleText("Saved!")
                            .setContentText("Your Weekend League has been saved!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(null)
                            .showCancelButton(false)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                    mPresenter.saveWeekendLeague(date);
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

    @Override
    public void showNewGame() {
            if(mListener!=null) mListener.onWLNewGameInteraction();
    }

    @Override
    public void showSaveWlFromNewGameDialog() {
        SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Weekend League finished?");
        pDialog.setContentText("No more games left this weekend, would you like to save and start a new Weekend League?");
        pDialog.setConfirmText("Yes");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Log.d(TAG, "onClick: yes");

                    sweetAlertDialog
                            .setTitleText("Saved!")
                            .setContentText("Your Weekend League has been saved!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(null)
                            .showCancelButton(false)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                    String date = new SimpleDateFormat("MM-dd-yyyy", Locale.US).format(new Date());
                    Log.d(TAG, "onClick: Current time => " + date);

                    mPresenter.saveWeekendLeague(date);


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

    @Override
    public void showClearWLDialog() {
        SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Clear this Weekend League?");
        pDialog.setContentText("Are you sure you would like to delete this Weekend League data? Doing so will create a new Weekend League.");
        pDialog.setConfirmText("Yes");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Log.d(TAG, "onClick: yes");
                    sweetAlertDialog
                            .setTitleText("Cleared!")
                            .setContentText("Weekend League data has been cleared!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(null)
                            .showCancelButton(false)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    mPresenter.clearWeekendLeague();
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

    @Override
    public boolean isActive() {
        return isAdded();
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mWlBinding.setPresenter(mPresenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        checkNotNull(mPresenter).start();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnNewWLFragmentInteractionListener {

        void onWLNewGameInteraction();
        void onWLViewGamesInteraction();
        void goToMainAppInfo();

    }
}
