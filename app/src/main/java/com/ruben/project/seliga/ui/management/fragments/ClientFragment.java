package com.ruben.project.seliga.ui.management.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ruben.project.seliga.R;
import com.ruben.project.seliga.data.model.Customer;
import com.ruben.project.seliga.data.repository.CustomerRepository;
import com.ruben.project.seliga.ui.common.HeaderDialog;
import com.ruben.project.seliga.ui.common.HeaderView;

import java.util.Objects;

public class ClientFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HeaderView headerView = view.findViewById(R.id.header_view);
        headerView.setTitle(getString(R.string.client_list));
        headerView.setActionButton("Add", v -> {
            HeaderDialog headerDialog = new HeaderDialog(getContext());

            headerDialog.setTitle(getString(R.string.manage_clients));

            headerDialog.setValueContainerVisibility(false);
            headerDialog.setDateContainerVisibility(false);
            headerDialog.setAddButtonVisibility(false);

            headerDialog.setOnAddNameButtonClickListener(v1 -> {
        if (!Objects.equals(headerDialog.getNameInputText(), "")) {
            String name = headerDialog.getNameInputText();
            CustomerRepository customerRepository = new CustomerRepository(requireActivity().getApplication());

            customerRepository.getCustomerByName(name).observe(getViewLifecycleOwner(), customer -> {
                if (customer == null) {
                    Customer newCustomer = new Customer(name);
                    customerRepository.insert(newCustomer);
                    Toast.makeText(getContext(), getContext().getText(R.string.client_added_successfully), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getContext().getText(R.string.client_already_exists), Toast.LENGTH_SHORT).show();
                }
            });
        }
});

            headerDialog.show();
        });

        RecyclerView recyclerView = view.findViewById(R.id.client_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}