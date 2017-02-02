package com.futchampionsstats.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futchampionsstats.R;
import com.futchampionsstats.Utils.Constants;
import com.futchampionsstats.databinding.FragmentMainActivityBinding;


public class MainActivityFragment extends Fragment {


    private OnMainFragmentInteractionListener mListener;

    public MainActivityFragment() {
        // Required empty public constructor
    }

    public static MainActivityFragment newInstance(String param1, String param2) {
        MainActivityFragment fragment = new MainActivityFragment();
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
        // Inflate the layout for this fragment
        FragmentMainActivityBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_activity, container, false);
        MainFragmentHandlers handlers = new MainFragmentHandlers();
        binding.setHandlers(handlers);

        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainFragmentInteractionListener) {
            mListener = (OnMainFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public class MainFragmentHandlers{

        public void mainFragmentClick(View view){

            Bundle b = new Bundle();
            switch(view.getId()) {
                case R.id.wl_btn:
                    b.putString(Constants.WL, Constants.WL);
                    break;
                case R.id.past_wl_btn:
                    b.putString(Constants.PAST_WL, Constants.PAST_WL);
                    break;
            }
            if(mListener!=null) mListener.onMainFragmentInteraction(b);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMainFragmentInteractionListener {
        void onMainFragmentInteraction(Bundle args);
    }
}
