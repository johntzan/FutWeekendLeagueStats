package com.futchampionsstats.ui.pastwls;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futchampionsstats.R;
import com.futchampionsstats.databinding.FragmentPastWlViewGameBinding;
import com.futchampionsstats.models.Game;
import com.futchampionsstats.utils.Constants;

public class PastWLViewGameFragment extends Fragment {


    private OnPastWLViewGameFragmentInteractionListener mListener;
    private Game game;

    public PastWLViewGameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            game = (Game) getArguments().getSerializable(Constants.VIEW_PAST_WL_GAME);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentPastWlViewGameBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_past_wl_view_game, container, false);
        PastWLViewGameHandlers handlers = new PastWLViewGameHandlers();

        binding.setGame(game);
        binding.setHandlers(handlers);


        return binding.getRoot();
    }

    public class PastWLViewGameHandlers{
        public void onClick(View view) {

            Bundle b = new Bundle();
            switch (view.getId()) {
                case R.id.back_btn:
                    b.putSerializable(Constants.BACK_BTN, Constants.BACK_BTN);
                    break;
            }
            if (mListener != null) mListener.onPastWLViewGameFragmentInteraction(b);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPastWLViewGameFragmentInteractionListener) {
            mListener = (OnPastWLViewGameFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPastWLViewGameFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPastWLViewGameFragmentInteractionListener {
        void onPastWLViewGameFragmentInteraction(Bundle args);
    }
}
