package com.ruben.project.seliga.ui.startup.addName;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.ruben.project.seliga.R;
import com.ruben.project.seliga.data.model.User;
import com.ruben.project.seliga.data.repository.UserRepository;
import com.ruben.project.seliga.ui.startup.StartupActivity;

public class NameAddFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_name_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserRepository userRepository = new UserRepository(requireActivity().getApplication());
        TextInputLayout inputName = view.findViewById(R.id.name_add_input_layout);
        MaterialButton buttonPrevious = view.findViewById(R.id.button_previous);
        MaterialButton buttonNext = view.findViewById(R.id.button_next);

        buttonPrevious.setOnClickListener(v -> {
            if (getActivity() instanceof StartupActivity) {
                ((StartupActivity) getActivity()).showFirstTimeFragment();
            }
        });

        buttonNext.setOnClickListener(v -> {
            if (inputName.getEditText().getText().toString().isEmpty() || inputName.getEditText().getText().toString().length() < 3) {
                inputName.setError(getString(R.string.invalid_name));
            } else {
                inputName.setError(null);

                User user = new User(inputName.getEditText().getText().toString(), true);
                userRepository.insert(user);

                if (getActivity() instanceof StartupActivity) {
                    ((StartupActivity) getActivity()).intentToMainActivity();
                }
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getActivity() instanceof StartupActivity) {
                    ((StartupActivity) getActivity()).showFirstTimeFragment();
                }
            }
        });
    }
}