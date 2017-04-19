package com.futchampionsstats.ui.past_wls.view_past_wls.selected;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futchampionsstats.R;
import com.futchampionsstats.databinding.FragmentViewSelectedWlBinding;
import com.futchampionsstats.models.WeekendLeague;

public class ViewSelectedWLFragment extends Fragment implements ViewSelectedWLContract.View{


    private OnViewSelectedWLFragmentInteractionListener mListener;

    private ViewSelectedWLContract.Presenter mPresenter;
    private FragmentViewSelectedWlBinding binding;

    public ViewSelectedWLFragment() {
        // Required empty public constructor
    }


    public static ViewSelectedWLFragment newInstance() {
        ViewSelectedWLFragment fragment = new ViewSelectedWLFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_selected_wl, container, false);

        ViewSelectedWLFragmentHandlers viewSelectedWLFragmentHandlers = new ViewSelectedWLFragmentHandlers();
        binding.setHandlers(viewSelectedWLFragmentHandlers);

        return binding.getRoot();
    }

    @Override
    public void setPresenter(ViewSelectedWLContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showWeekendLeague(WeekendLeague weekendLeague) {
        binding.setWeekendLeague(weekendLeague);
    }

    @Override
    public void showWeekendLeagueGames(WeekendLeague weekendLeague) {
        if(mListener!=null) mListener.onViewSelectedWLViewGames(weekendLeague);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    public class ViewSelectedWLFragmentHandlers{

        public void viewPastWLGamesBtnClick(View view){
            mPresenter.getWeekendLeagueGames();
        }
        public void onBackBtnClick(View view){
            if(mListener!=null) mListener.onViewSelectedWLBackBtnClick();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnViewSelectedWLFragmentInteractionListener) {
            mListener = (OnViewSelectedWLFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnViewSelectedWLFragmentInteractionListener {
        void onViewSelectedWLViewGames(WeekendLeague weekendLeague);
        void onViewSelectedWLBackBtnClick();
    }
}
