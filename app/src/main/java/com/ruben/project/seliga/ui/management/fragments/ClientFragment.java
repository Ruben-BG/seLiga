package com.ruben.project.seliga.ui.management.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ruben.project.seliga.R;
import com.ruben.project.seliga.data.model.Customer;
import com.ruben.project.seliga.data.repository.CustomerRepository;
import com.ruben.project.seliga.ui.common.ConfirmationDialog;
import com.ruben.project.seliga.ui.common.HeaderDialog;
import com.ruben.project.seliga.ui.common.HeaderView;
import com.ruben.project.seliga.ui.management.adapters.ClientAdapter;
import com.ruben.project.seliga.ui.management.viewmodels.ClientViewModel;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClientFragment extends Fragment {

    private ClientAdapter adapter;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.client_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ClientAdapter(new ArrayList<>(), getContext(), requireActivity().getApplication());
        recyclerView.setAdapter(adapter);

        ClientViewModel clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);
        clientViewModel.getAllCustomers().observe(getViewLifecycleOwner(), customers -> {
            adapter.setClientList(customers);
        });

        HeaderView headerView = view.findViewById(R.id.header_view);
        headerView.setTitle(getString(R.string.client_list));
        headerView.setActionButton("Add", v -> {
            HeaderDialog headerDialog = new HeaderDialog(getContext());
            CustomerRepository customerRepository = new CustomerRepository(requireActivity().getApplication());

            headerDialog.setTitle(getString(R.string.manage_clients));

            headerDialog.setSelectNameInputVisibility(false);
            headerDialog.setValueContainerVisibility(false);
            headerDialog.setDateContainerVisibility(false);
            headerDialog.setAddButtonVisibility(false);

            headerDialog.setClearButtonText(getString(R.string.clear_client_list));

            headerDialog.setOnAddNameButtonClickListener(v1 -> {
                String name = headerDialog.getNameInputText().trim();
                if (!name.isEmpty()) {
                    executorService.execute(() -> {
                        Customer customer = customerRepository.getCustomerByNameSync(name);
                        requireActivity().runOnUiThread(() -> {
                            if (customer == null) {
                                Customer newCustomer = new Customer(name);
                                customerRepository.insert(newCustomer);
                                headerDialog.clearNameInputText();
                                Toast.makeText(getContext(), getContext().getText(R.string.client_added_successfully), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), getContext().getText(R.string.client_already_exists), Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                } else {
                    Toast.makeText(getContext(), getContext().getText(R.string.invalid_client_name), Toast.LENGTH_SHORT).show();
                }
            });

            headerDialog.setOnClearButtonClickListener(v1 -> {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v1.getWindowToken(), 0);

                ConfirmationDialog confirmationDialog = new ConfirmationDialog(getContext());
                confirmationDialog.setTitleText(getString(R.string.clear_client_list));
                confirmationDialog.setMessageText(getString(R.string.clear_list_question, getString(R.string.menu_clients).toLowerCase()));

                confirmationDialog.setOnPositiveButtonClickListener(v2 -> {
                    customerRepository.deleteAllCustomers();
                    adapter.setClientList(new ArrayList<>());
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), getContext().getText(R.string.list_cleared_successfully), Toast.LENGTH_SHORT).show();
                    confirmationDialog.dismiss();
                });

                confirmationDialog.show();
            });

            headerDialog.show();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}