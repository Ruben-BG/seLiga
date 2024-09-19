package com.ruben.project.seliga.ui.management.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ruben.project.seliga.R;
import com.ruben.project.seliga.data.model.Payments;
import com.ruben.project.seliga.data.repository.CustomerRepository;
import com.ruben.project.seliga.ui.common.ConfirmationDialog;
import com.ruben.project.seliga.ui.common.HeaderDialog;
import com.ruben.project.seliga.ui.common.HeaderView;
import com.ruben.project.seliga.ui.management.adapters.ChargeAdapter;
import com.ruben.project.seliga.ui.management.viewmodels.ChargeViewModel;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class ChargeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_charge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ChargeViewModel chargeViewModel = new ChargeViewModel(requireActivity().getApplication());

        RecyclerView chargeList = view.findViewById(R.id.charge_list);
        chargeList.setLayoutManager(new LinearLayoutManager(getContext()));

        chargeViewModel.getAllCharges().observe(getViewLifecycleOwner(), charges -> {
            ChargeAdapter chargeAdapter = new ChargeAdapter(charges, getContext(), requireActivity().getApplication());
            chargeList.setAdapter(chargeAdapter);
        });

        HeaderView headerView = view.findViewById(R.id.header_view_charge);
        headerView.setTitle(getString(R.string.charge_list));
        headerView.setActionButton("Add", v -> {

            HeaderDialog headerDialog = new HeaderDialog(getContext());
            CustomerRepository customerRepository = new CustomerRepository(requireActivity().getApplication());
            AtomicReference<Boolean> isNotValueInputEnabled = new AtomicReference<>(true);
            AtomicReference<Boolean> isNotDateInputEnabled = new AtomicReference<>(true);
            AtomicReference<Integer> selectedCustomerId = new AtomicReference<>(-1);

            headerDialog.setTitle(getString(R.string.manage_charges));

            headerDialog.setNameContainerVisibility(false);

            headerDialog.setValueButtonText(getString(R.string.no_value));
            headerDialog.setDateButtonText(getString(R.string.today));
            headerDialog.setAddButtonText(getString(R.string.add_charge));
            headerDialog.setClearButtonText(getString(R.string.clear_charge_list));

            // Configuração do campo de seleção de cliente (dropdown)
            customerRepository.getCustomerNames().observe(getViewLifecycleOwner(), customerNames -> {
                if (customerNames != null && !customerNames.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            requireContext(),
                            R.layout.list_item,
                            customerNames
                    );
                    headerDialog.setSelectNameInputAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), R.string.no_clients_registered, Toast.LENGTH_SHORT).show();
                    Log.d("ChargeFragment", "No customer names available");
                }
            });

            headerDialog.setOnSelectNameInputItemClickListener((parent, view1, position, id) -> {
                String selectedName = parent.getItemAtPosition(position).toString();
                customerRepository.getCustomerByName(selectedName).observe(getViewLifecycleOwner(), customer -> {
                    selectedCustomerId.set(customer.getId());
                });
            });

            // Configuração dos botões de valor e data
            headerDialog.setOnValueButtonClickListener(v1 -> {
                headerDialog.setValueInputEnabled(!isNotValueInputEnabled.get());
                isNotValueInputEnabled.set(!isNotValueInputEnabled.get());

                if (isNotValueInputEnabled.get()) {
                    headerDialog.setValueButtonColor(ContextCompat.getColor(requireContext(), R.color.uncheckedButtonBackground));
                } else {
                    headerDialog.setValueButtonColor(ContextCompat.getColor(requireContext(), R.color.secondary));
                }
            });

            headerDialog.setOnDateButtonClickListener(v1 -> {
                headerDialog.setDateInputEnabled(!isNotDateInputEnabled.get());
                isNotDateInputEnabled.set(!isNotDateInputEnabled.get());

                if (isNotDateInputEnabled.get()) {
                    headerDialog.setDateButtonColor(ContextCompat.getColor(requireContext(), R.color.uncheckedButtonBackground));
                } else {
                    headerDialog.setDateButtonColor(ContextCompat.getColor(requireContext(), R.color.secondary));
                }
            });

            // Configuração do botão de adicionar pagamento
            headerDialog.setOnAddButtonClickListener(v1 -> {
                if (selectedCustomerId.get() != -1) {
                    double value;
                    Date date;

                    if (isNotValueInputEnabled.get()) {
                        String valueText = headerDialog.getValueInputText();
                        if (!valueText.isEmpty()) {
                            valueText = valueText
                                    .replace("R$", "")
                                    .replace(".", "")
                                    .replace(",", ".")
                                    .replaceAll("\\s+", "");
                            try {
                                value = Double.parseDouble(valueText);
                            } catch (NumberFormatException e) {
                                Log.e("ChargeFragment", "Error parsing value: ", e);
                                Toast.makeText(getContext(), R.string.invalid_charge_value, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            value = -1;
                        }
                        if (value < 0) {
                            Toast.makeText(getContext(), R.string.invalid_charge_value, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        value = -1.0;
                    }

                    if (isNotDateInputEnabled.get()) {
                        date = headerDialog.getDateInputText();
                        if (date == null) {
                            Toast.makeText(getContext(), R.string.invalid_charge_date, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        date = new Date();
                    }

                    Payments charge = new Payments(
                            selectedCustomerId.get(),
                            false,
                            value,
                            date
                    );

                    chargeViewModel.insert(charge);

                    headerDialog.clearAllPaymentDialog();

                    isNotValueInputEnabled.set(true);
                    headerDialog.setValueButtonColor(ContextCompat.getColor(requireContext(), R.color.uncheckedButtonBackground));

                    isNotDateInputEnabled.set(true);
                    headerDialog.setDateButtonColor(ContextCompat.getColor(requireContext(), R.color.uncheckedButtonBackground));

                    selectedCustomerId.set(-1);

                    Toast.makeText(getContext(), R.string.charge_added_successfully, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.no_client_selected, Toast.LENGTH_SHORT).show();
                }
            });

            // Configuração do botão de limpar lista de pagamentos
            headerDialog.setOnClearButtonClickListener(v1 -> {
                ConfirmationDialog confirmationDialog = new ConfirmationDialog(getContext());
                confirmationDialog.setTitleText(getString(R.string.clear_charge_list));
                confirmationDialog.setMessageText(getString(R.string.clear_list_question, getString(R.string.menu_charges).toLowerCase()));

                confirmationDialog.setOnPositiveButtonClickListener(v2 -> {
                    chargeViewModel.deleteAllPaymentsNotPaid();
                    Toast.makeText(getContext(), getContext().getText(R.string.list_cleared_successfully), Toast.LENGTH_SHORT).show();
                    confirmationDialog.dismiss();
                });

                confirmationDialog.show();
            });

            headerDialog.show();
        });

    }
}