package com.futchampionsstats.ui.pastwls;

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
import com.futchampionsstats.utils.Constants;

public class ViewSelectedWLFragment extends Fragment {


    private OnViewSelectedWLFragmentInteractionListener mListener;
    private WeekendLeague weekendLeague;

    public ViewSelectedWLFragment() {
        // Required empty public constructor
    }


    public static ViewSelectedWLFragment newInstance(String param1, String param2) {
        ViewSelectedWLFragment fragment = new ViewSelectedWLFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weekendLeague = (WeekendLeague) getArguments().getSerializable(Constants.VIEW_SELECTED_PAST_WL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentViewSelectedWlBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_selected_wl, container, false);

        ViewSelectedWLFragmentHandlers viewSelectedWLFragmentHandlers = new ViewSelectedWLFragmentHandlers();
        binding.setHandlers(viewSelectedWLFragmentHandlers);
        binding.setWeekendLeague(weekendLeague);


        return binding.getRoot();
    }

    public class ViewSelectedWLFragmentHandlers{

        public void wlFragmentClick(View view){

            final Bundle b = new Bundle();
            switch(view.getId()) {
                case R.id.view_games_btn:
                    b.putSerializable(Constants.VIEW_PAST_WL_GAMES, weekendLeague);
                    break;
                case R.id.back_btn:
                    b.putSerializable(Constants.BACK_BTN, Constants.BACK_BTN);
                    break;

            }
            if(mListener!=null) mListener.onViewSelectedWLFragmentInteraction(b);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnViewSelectedWLFragmentInteractionListener {
        void onViewSelectedWLFragmentInteraction(Bundle args);
    }
}
