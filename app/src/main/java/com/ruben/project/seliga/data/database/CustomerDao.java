package com.ruben.project.seliga.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ruben.project.seliga.data.model.Customer;

import java.util.List;

@Dao
public interface CustomerDao {

    @Insert
    void insert(Customer customer);

    @Update
    void update(Customer customer);

    @Delete
    void delete(Customer customer);

    @Query("DELETE FROM customer")
    void deleteAllCustomers();

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'customer'")
    void resetCustomerIdSequence();

    @Query("SELECT * FROM customer WHERE id = :id")
    LiveData<Customer> getCustomerById(int id);

    @Query("SELECT * FROM customer")
    LiveData<List<Customer>> getAllCustomers();

    @Query("SELECT * FROM customer WHERE name = :name LIMIT 1")
    LiveData<Customer> getCustomerByName(String name);

    @Query("SELECT name FROM customer")
    LiveData<List<String>> getAllCustomerNames();

    @Query("SELECT * FROM customer WHERE name = :name LIMIT 1")
    Customer getCustomerByNameSync(String name);
}