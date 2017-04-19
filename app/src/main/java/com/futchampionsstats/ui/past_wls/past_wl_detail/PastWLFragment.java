package com.futchampionsstats.ui.past_wls.past_wl_detail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futchampionsstats.R;
import com.futchampionsstats.databinding.FragmentPastWlBinding;
import com.futchampionsstats.models.AllWeekendLeagues;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PastWLFragment extends Fragment implements PastWLDetailContract.View{

    public static final String TAG = PastWLFragment.class.getSimpleName();

    private OnPastWLFragmentInteractionListener mListener;
    private FragmentPastWlBinding binding;

    private PastWLDetailContract.Presenter mPresenter;

    public PastWLFragment() {
        // Required empty public constructor
    }


    public static PastWLFragment newInstance(String param1, String param2) {
        PastWLFragment fragment = new PastWLFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_past_wl, container, false);
        PastWLFragmentHandlers handlers = new PastWLFragmentHandlers();

        binding.setHandlers(handlers);
        return binding.getRoot();
    }


    public class PastWLFragmentHandlers {

        public void deleteAllWLsClick(View view){
            showClearAllWLsDialog(view);
        }

        public void goToPastWeekendLeagues(View view){
            if (mListener != null) mListener.goToPastWeekendLeaguesList();
        }

    }

    @Override
    public void setPresenter(PastWLDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showClearAllWLsDialog(View v) {
        SweetAlertDialog pDialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Delete Past Weekend Leagues?");
        pDialog.setContentText("Are you sure you would like to delete all your past Weekend League Data?");
        pDialog.setConfirmText("Yes");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Log.d(TAG, "onClick: yes");

                mPresenter.clearAllWeekendLeagues();
                sweetAlertDialog
                        .setTitleText("Deleted!")
                        .setContentText("All previous Weekend League data has been deleted!")
                        .setConfirmText("OK")
                        .setConfirmClickListener(null)
                        .showCancelButton(false)
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

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
    public void showPastWeekendLeagues(AllWeekendLeagues allWeekendLeagues) {
        binding.setAllWeekendLeagues(allWeekendLeagues);
    }

    @Override
    public void showEmptyWeekendLeagues(AllWeekendLeagues allWeekendLeagues) {
        binding.setAllWeekendLeagues(allWeekendLeagues);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPastWLFragmentInteractionListener) {
            mListener = (OnPastWLFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPastWLFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPastWLFragmentInteractionListener {
        void goToPastWeekendLeaguesList();
    }
}
