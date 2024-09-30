package com.ruben.project.seliga.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ruben.project.seliga.data.model.Customer;
import com.ruben.project.seliga.data.model.Payments;
import com.ruben.project.seliga.data.repository.CustomerRepository;
import com.ruben.project.seliga.data.repository.PaymentsRepository;
import com.ruben.project.seliga.data.repository.UserRepository;
import com.ruben.project.seliga.util.ClientWeek;
import com.ruben.project.seliga.util.DateUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PaymentsRepository paymentsRepository;

    private final Date startOfWeek = DateUtils.getStartOfWeek();
    private final Date endOfWeek = DateUtils.getEndOfWeek();

    private final MutableLiveData<String> userName = new MutableLiveData<>();

    public HomeViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application);
        customerRepository = new CustomerRepository(application);
        paymentsRepository = new PaymentsRepository(application);

        loadUserData();
    }

    private void loadUserData() {
        userName.setValue(userRepository.getUserName());
    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<Integer> getClientCount() {
        return customerRepository.getCustomerCount();
    }

    public LiveData<Integer> getPaymentCount() {
        return paymentsRepository.getPaymentCount();
    }

    public LiveData<Integer> getChargeCount() {
        return paymentsRepository.getChargeCount();
    }

    public LiveData<Integer> getWeeklyClientCount() {
        return paymentsRepository.getWeeklyClientsCount(startOfWeek, endOfWeek);
    }

    public LiveData<List<Payments>> getPaymentsOfTheWeek() {
        return paymentsRepository.getPaymentsOfTheWeek(startOfWeek, endOfWeek);
    }

    public LiveData<List<ClientWeek>> getClientWeekList() {
        return paymentsRepository.getDistinctClientsWithCount(startOfWeek, endOfWeek);
    }

    public Integer getWeeklyPaymentCount(List<Payments> payments) {
        return (int) payments.stream().filter(Payments::isPaid).count();
    }

    public Integer getWeeklyChargeCount(List<Payments> payments) {
        return (int) payments.stream().filter(payment -> !payment.isPaid()).count();
    }

    public LiveData<Customer> getCustomerById(int id) {
        return customerRepository.getCustomerById(id);
    }

}
