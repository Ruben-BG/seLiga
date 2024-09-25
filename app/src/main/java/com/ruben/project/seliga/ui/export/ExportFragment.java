package com.ruben.project.seliga.ui.export;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.ruben.project.seliga.R;
import com.ruben.project.seliga.ui.common.ConfirmationDialog;
import com.ruben.project.seliga.ui.common.HeaderView;
import com.ruben.project.seliga.viewmodel.ExportViewModel;

public class ExportFragment extends Fragment {

    private ExportViewModel exportViewModel;
    private ActivityResultLauncher<Intent> manageAllFilesAccessPermissionLauncher;
    private ActivityResultLauncher<String[]> requestPermissionsLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manageAllFilesAccessPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (hasPermissions()) {
                        Toast.makeText(requireContext(), "Permissions not granted", Toast.LENGTH_SHORT).show();
                    }
                });

        requestPermissionsLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    if (hasPermissions()) {
                        Toast.makeText(requireContext(), "Permissions not granted", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_export, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        exportViewModel = new ViewModelProvider(this).get(ExportViewModel.class);

        MaterialButton paymentButton = view.findViewById(R.id.export_payment_button);
        MaterialButton chargeButton = view.findViewById(R.id.export_charges_button);

        exportViewModel.getIsPaymentInputEnabled().observe(getViewLifecycleOwner(), isEnabled -> {
            int color = isEnabled ? R.color.secondary : R.color.uncheckedButtonBackground;
            paymentButton.setBackgroundColor(ContextCompat.getColor(requireContext(), color));
        });

        exportViewModel.getIsChargeInputEnabled().observe(getViewLifecycleOwner(), isEnabled -> {
            int color = isEnabled ? R.color.secondary : R.color.uncheckedButtonBackground;
            chargeButton.setBackgroundColor(ContextCompat.getColor(requireContext(), color));
        });

        paymentButton.setOnClickListener(v -> exportViewModel.togglePaymentInput());
        chargeButton.setOnClickListener(v -> exportViewModel.toggleChargeInput());

        HeaderView headerView = view.findViewById(R.id.header_view_export);
        headerView.setTitle(getString(R.string.export_data));
        headerView.setActionButtonImage(R.drawable.mdi_export);

        headerView.setActionButton("Export", v -> {
            ConfirmationDialog confirmationDialog = new ConfirmationDialog(getContext());
            confirmationDialog.setTitleText(getString(R.string.export_confirmation));
            confirmationDialog.setMessageText(getString(R.string.export_confirmation_question));

            confirmationDialog.setOnPositiveButtonClickListener(dialog -> {
                if (!hasPermissions()) {
                    exportViewModel.exportData(requireContext());
                } else {
                    requestPermissions();
                }
            });

            confirmationDialog.show();
        });
    }

    private boolean hasPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return !Environment.isExternalStorageManager();
        } else {
            int writePermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            return writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                manageAllFilesAccessPermissionLauncher.launch(intent);
            } catch (Exception e) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + requireContext().getPackageName()));
                manageAllFilesAccessPermissionLauncher.launch(intent);
            }
        } else {
            requestPermissionsLauncher.launch(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            });
        }
    }
}