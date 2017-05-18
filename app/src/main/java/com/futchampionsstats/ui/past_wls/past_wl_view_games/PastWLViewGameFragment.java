package com.futchampionsstats.ui.past_wls.past_wl_view_games;

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

public class PastWLViewGameFragment extends Fragment implements PastWLViewGameContract.View{


    private OnPastWLViewGameFragmentInteractionListener mListener;

    private PastWLViewGameContract.Presenter mPresenter;
    private FragmentPastWlViewGameBinding binding;

    public PastWLViewGameFragment() {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_past_wl_view_game, container, false);

        PastWLViewGameHandlers handlers = new PastWLViewGameHandlers();
        binding.setHandlers(handlers);


        return binding.getRoot();
    }

    @Override
    public void setPresenter(PastWLViewGameContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showGame(Game game) {
        binding.setGame(game);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    public class PastWLViewGameHandlers{

        public void onBackBtnClick(View view) {
            if (mListener != null) mListener.onPastWLViewGameBackBtnClick();
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
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPastWLViewGameFragmentInteractionListener {
        void onPastWLViewGameBackBtnClick();
    }
}
