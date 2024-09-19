package com.ruben.project.seliga.ui.management.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ruben.project.seliga.data.model.Payments;
import com.ruben.project.seliga.data.repository.PaymentsRepository;

import java.util.List;

public class ChargeViewModel extends AndroidViewModel {
    private final PaymentsRepository paymentsRepository;
    private final LiveData<List<Payments>> allPayments;

    public ChargeViewModel(Application application) {
        super(application);
        paymentsRepository = new PaymentsRepository(application);
        allPayments = paymentsRepository.getAllPaymentsNotPaidDesc();
    }

    public LiveData<List<Payments>> getAllCharges() {
        return allPayments;
    }

    public void insert(Payments payment) {
        paymentsRepository.insert(payment);
    }

    public void deleteAllPaymentsNotPaid() {
        paymentsRepository.deleteAllPaymentsNotPaid();
    }
}
