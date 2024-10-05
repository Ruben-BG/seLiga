package com.ruben.project.seliga.ui.startup.importData;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ruben.project.seliga.R;
import com.ruben.project.seliga.data.model.Customer;
import com.ruben.project.seliga.data.model.Payments;
import com.ruben.project.seliga.data.model.User;
import com.ruben.project.seliga.data.repository.CustomerRepository;
import com.ruben.project.seliga.data.repository.PaymentsRepository;
import com.ruben.project.seliga.data.repository.UserRepository;
import com.ruben.project.seliga.ui.startup.StartupActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class ImportDataFragment extends Fragment {

    AtomicReference<Boolean> isFileImported = new AtomicReference<>(false);
    private MaterialButton buttonImport;
    private CircularProgressIndicator progressIndicator;
    private MaterialButton buttonPrevious;
    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                getActivity();
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        readFile(uri);
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_import_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonImport = view.findViewById(R.id.button_import);
        buttonPrevious = view.findViewById(R.id.button_previous);
        MaterialButton buttonNext = view.findViewById(R.id.button_next);

        progressIndicator = new CircularProgressIndicator(requireContext());
        progressIndicator.setIndicatorSize(48);
        progressIndicator.setTrackThickness(4);

        buttonImport.setOnClickListener(v -> {
            if (!isFileImported.get()) {
                openFilePicker();
            } else {
                Toast.makeText(getContext(), R.string.file_already_imported, Toast.LENGTH_SHORT).show();
            }
        });

        buttonPrevious.setOnClickListener(v -> {
            if (getActivity() instanceof StartupActivity) {
                ((StartupActivity) getActivity()).showFirstTimeFragment();
            }
        });

        buttonNext.setOnClickListener(v -> {
            if (isFileImported.get()) {
                if (getActivity() instanceof StartupActivity) {
                    ((StartupActivity) getActivity()).intentToMainActivity();
                }
            } else {
                Toast.makeText(getContext(), R.string.import_file_first, Toast.LENGTH_SHORT).show();
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getActivity() instanceof StartupActivity && !isFileImported.get()) {
                    ((StartupActivity) getActivity()).showFirstTimeFragment();
                }
            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        filePickerLauncher.launch(intent);
    }

    private void readFile(Uri uri) {
        try (InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            buttonImport.setIcon(progressIndicator.getIndeterminateDrawable());
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String json = stringBuilder.toString();
            validateJson(json);
        } catch (IOException e) {
            Log.e("ImportDataFragment", "Error reading file", e);
            buttonImport.setIcon(null);
        }
    }

    private void validateJson(String json) {
        try {
            Gson gson = new Gson();
            ExportData exportData = gson.fromJson(json, ExportData.class);
            // Verifica se os dados são compatíveis
            if (exportData.customers != null && exportData.payments != null && exportData.users != null) {
                UserRepository userRepository = new UserRepository(requireActivity().getApplication());
                CustomerRepository customerRepository = new CustomerRepository(requireActivity().getApplication());
                PaymentsRepository paymentsRepository = new PaymentsRepository(requireActivity().getApplication());

                userRepository.insert(exportData.users.get(0));
                customerRepository.insertAll(exportData.customers);

                // Verifica se os dados referenciados existem antes de inserir os pagamentos
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                for (Payments payment : exportData.payments) {
                    executorService.execute(() -> {
                        try {
                            boolean exists = customerRepository.exists(payment.getCustomerId()).get();
                            if (exists) {
                                paymentsRepository.insert(payment);
                            } else {
                                Log.e("ImportDataFragment", "Customer ID " + payment.getCustomerId() + " does not exist");
                            }
                        } catch (Exception e) {
                            Log.e("ImportDataFragment", "Error checking customer ID", e);
                        }
                    });
                }

                isFileImported.set(true);
                buttonPrevious.setEnabled(false);
                buttonImport.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.secondary));
                buttonImport.setText(R.string.imported_text);
            } else {
                throw new JsonSyntaxException("Invalid data");
            }
        } catch (JsonSyntaxException e) {
            Log.e("ImportDataFragment", "Invalid JSON format", e);
            Toast.makeText(getContext(), R.string.invalid_file_format, Toast.LENGTH_SHORT).show();
        } finally {
            buttonImport.setIcon(null);
        }
    }

    private static class ExportData {
        List<Customer> customers;
        List<Payments> payments;
        List<User> users;
    }
}