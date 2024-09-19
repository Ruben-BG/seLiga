package com.ruben.project.seliga.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import com.ruben.project.seliga.data.database.AppDatabase;
import com.ruben.project.seliga.data.database.CustomerDao;
import com.ruben.project.seliga.data.model.Customer;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomerRepository {
    private final CustomerDao customerDao;
    private final ExecutorService executorService;

    public CustomerRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        customerDao = db.customerDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Customer customer) {
        executorService.execute(() -> customerDao.insert(customer));
    }

    public void update(Customer customer) {
        executorService.execute(() -> customerDao.update(customer));
    }

    public void delete(Customer customer) {
        executorService.execute(() -> customerDao.delete(customer));
    }

    public LiveData<Customer> getCustomerById(int id) {
        return customerDao.getCustomerById(id);
    }

    public LiveData<List<Customer>> getAllCustomers() {
        return customerDao.getAllCustomers();
    }

    public LiveData<Customer> getCustomerByName(String name) {
        return customerDao.getCustomerByName(name);
    }
}