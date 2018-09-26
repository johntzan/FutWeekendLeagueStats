package com.futchampionsstats.ui.wl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.futchampionsstats.BuildConfig;
import com.futchampionsstats.R;

public class InfoFragment extends Fragment {

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        ImageView backBtn = view.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null) getActivity().onBackPressed();
            }
        });

        TextView versionTxt = view.findViewById(R.id.versionTxt);
        TextView privacyTxt = view.findViewById(R.id.privacyTxt);
        TextView termsTxt = view.findViewById(R.id.termsTxt);

        versionTxt.setText("Version: " + BuildConfig.VERSION_NAME);
        privacyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fut-stats.firebaseapp.com/privacy-policy"));
                startActivity(browserIntent);
            }
        });

        termsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fut-stats.firebaseapp.com/terms"));
                startActivity(browserIntent);
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
