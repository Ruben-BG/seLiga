package com.ruben.project.seliga.ui.management.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ruben.project.seliga.data.model.Customer;
import com.ruben.project.seliga.data.repository.CustomerRepository;

import java.util.List;

public class ClientViewModel extends AndroidViewModel {
    private final LiveData<List<Customer>> allCustomers;

    public ClientViewModel(Application application) {
        super(application);
        CustomerRepository repository = new CustomerRepository(application);
        allCustomers = repository.getAllCustomers();
    }

    public LiveData<List<Customer>> getAllCustomers() {
        return allCustomers;
    }
}