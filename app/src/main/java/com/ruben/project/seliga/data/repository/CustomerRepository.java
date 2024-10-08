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
import java.util.concurrent.Future;

public class CustomerRepository {
    private final CustomerDao customerDao;
    private final ExecutorService executorService;

    public CustomerRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        customerDao = db.customerDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Customer customer) {
        executorService.execute(() -> {
            try {
                customerDao.insert(customer);
                Log.d("CustomerRepository", "Customer inserido com sucesso.");
            } catch (Exception e) {
                Log.e("CustomerRepository", "Erro ao inserir Customer: ", e);
            }
        });
    }

    public void insertAll(List<Customer> customers) {
        executorService.execute(() -> {
            try {
                customerDao.insertAll(customers);
                Log.d("CustomerRepository", "Todos os Customers foram inseridos com sucesso.");
            } catch (Exception e) {
                Log.e("CustomerRepository", "Erro ao inserir todos os Customers: ", e);
            }
        });
    }

    public Future<Boolean> exists(int customerId) {
        return executorService.submit(() -> customerDao.exists(customerId));
    }

    public void delete(Customer customer) {
        executorService.execute(() -> {
            try {
                customerDao.delete(customer);
                Log.d("CustomerRepository", "Customer excluído com sucesso.");
            } catch (Exception e) {
                Log.e("CustomerRepository", "Erro ao excluir Customer: ", e);
            }
        });
    }

    public void deleteAllCustomers() {
        executorService.execute(() -> {
            try {
                customerDao.deleteAllCustomers();
                customerDao.resetCustomerIdSequence();
                Log.d("CustomerRepository", "Todos os Customers foram excluídos com sucesso.");
            } catch (Exception e) {
                Log.e("CustomerRepository", "Erro ao excluir todos os Customers: ", e);
            }
        });
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

    public Customer getCustomerByNameSync(String name) {
        Future<Customer> future = executorService.submit(() -> customerDao.getCustomerByNameSync(name));
        try {
            return future.get();
        } catch (Exception e) {
            Log.e("CustomerRepository", "Erro ao buscar Customer: ", e);
            return null;
        }
    }

    public LiveData<List<String>> getCustomerNames() {
        return customerDao.getAllCustomerNames();
    }

    public LiveData<Integer> getCustomerCount() {
        return customerDao.getCustomerCount();
    }
}
