package com.ruben.project.seliga.ui.startup.firstTime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.ruben.project.seliga.R;
import com.ruben.project.seliga.ui.startup.StartupActivity;

public class FirstTimeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_time, container, false);

        Button buttonYes = view.findViewById(R.id.button_first_time_yes);
        buttonYes.setOnClickListener(v -> {
            if (getActivity() instanceof StartupActivity) {
                ((StartupActivity) getActivity()).showNameAddFragment();
            }
        });

        Button buttonNo = view.findViewById(R.id.button_first_time_no);
        buttonNo.setOnClickListener(v -> {
            if (getActivity() instanceof StartupActivity) {
                ((StartupActivity) getActivity()).showImportDataFragment();
            }
        });

        return view;
    }
}