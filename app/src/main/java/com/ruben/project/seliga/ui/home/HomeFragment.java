package com.ruben.project.seliga.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ruben.project.seliga.R;
import com.ruben.project.seliga.ui.adapters.EventAdapter;
import com.ruben.project.seliga.ui.common.HeaderView;
import com.ruben.project.seliga.util.ClientWeek;
import com.ruben.project.seliga.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        TextView title = view.findViewById(R.id.textView_home_title);

        TextView clientCount = view.findViewById(R.id.textView_client_count);
        TextView paymentCount = view.findViewById(R.id.textView_payment_count);
        TextView chargeCount = view.findViewById(R.id.textView_charge_count);

        TextView titleWeeklyClientCount = view.findViewById(R.id.textView_weekly_clients_count);
        TextView listWeeklyClientCount = view.findViewById(R.id.textView_week_clients_list);

        TextView titleWeeklyPaymentCount = view.findViewById(R.id.textView_weekly_financial_operations);
        TextView weeklyPaymentCount = view.findViewById(R.id.textView_weekly_payments);
        TextView weeklyChargeCount = view.findViewById(R.id.textView_weekly_charges);

        HeaderView headerView = view.findViewById(R.id.header_weekly_events);
        RecyclerView recyclerView = view.findViewById(R.id.weekly_event_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        homeViewModel.getUserName().observe(getViewLifecycleOwner(), userName ->
                title.setText(getString(R.string.hello_user, userName))
        );

        homeViewModel.getClientCount().observe(getViewLifecycleOwner(), count ->
                clientCount.setText(getString(R.string.clients_count, String.valueOf(count)))
        );

        homeViewModel.getPaymentCount().observe(getViewLifecycleOwner(), count ->
                paymentCount.setText(getString(R.string.payments_count, String.valueOf(count)))
        );

        homeViewModel.getChargeCount().observe(getViewLifecycleOwner(), count ->
                chargeCount.setText(getString(R.string.charges_count, String.valueOf(count)))
        );

        homeViewModel.getWeeklyClientCount().observe(getViewLifecycleOwner(), count -> {
            titleWeeklyClientCount.setText(getString(R.string.clients_week, String.valueOf(count)));
        });

        homeViewModel.getClientWeekList().observe(getViewLifecycleOwner(), clientWeeks -> {
            StringBuilder stringBuilder = new StringBuilder();

            clientWeeks.sort((o1, o2) -> o2.count() - o1.count());
            for (ClientWeek clientWeek : clientWeeks) {
                homeViewModel.getCustomerById(clientWeek.customerId()).observe(getViewLifecycleOwner(), customer -> {
                    if (customer != null) {
                        stringBuilder.append(customer.getName())
                                .append(" - ")
                                .append(clientWeek.count())
                                .append("; ");
                    }
                    listWeeklyClientCount.setText(stringBuilder.toString());
                });
            }
        });

        homeViewModel.getPaymentsOfTheWeek().observe(getViewLifecycleOwner(), payments -> {
            titleWeeklyPaymentCount.setText(getString(R.string.operations_week, String.valueOf(payments.size())));
            weeklyPaymentCount.setText(getString(R.string.payment_operation_week, String.valueOf(homeViewModel.getWeeklyPaymentCount(payments))));
            weeklyChargeCount.setText(getString(R.string.charge_operation_week, String.valueOf(homeViewModel.getWeeklyChargeCount(payments))));

            EventAdapter weeklyEventAdapter = new EventAdapter(payments, getContext(), requireActivity().getApplication());
            recyclerView.setAdapter(weeklyEventAdapter);
        });

        headerView.setTitle(getString(R.string.events_week));
        headerView.setActionButtonVisibility(false);
    }
}