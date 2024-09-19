package com.ruben.project.seliga.ui.management.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ruben.project.seliga.data.model.Payments;
import com.ruben.project.seliga.data.repository.PaymentsRepository;

import java.util.List;

public class PaymentViewModel extends AndroidViewModel {
    private final PaymentsRepository paymentsRepository;
    private final LiveData<List<Payments>> allPayments;

    public PaymentViewModel(Application application) {
        super(application);
        paymentsRepository = new PaymentsRepository(application);
        allPayments = paymentsRepository.getAllPaymentsPaidDesc();
    }

    public LiveData<List<Payments>> getAllPayments() {
        return allPayments;
    }

    public void insert(Payments payment) {
        paymentsRepository.insert(payment);
    }

    public void deleteAllPaymentsPaid() {
        paymentsRepository.deleteAllPaymentsPaid();
    }
}