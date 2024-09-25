package com.ruben.project.seliga.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ruben.project.seliga.data.model.Payments;

import java.util.Date;
import java.util.List;

@Dao
public interface PaymentsDao {

    @Insert
    void insert(Payments payment);

    @Update
    void update(Payments payment);

    @Delete
    void delete(Payments payment);

    @Query("DELETE FROM payments WHERE paid = 1")
    void deleteAllPaymentsPaid();

    @Query("DELETE FROM payments WHERE paid = 0")
    void deleteAllPaymentsNotPaid();

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'payments'")
    void resetCustomerIdSequence();

    @Query("SELECT * FROM payments WHERE id = :id")
    LiveData<Payments> getPaymentById(int id);

    @Query("SELECT * FROM payments WHERE date = :date")
    LiveData<List<Payments>> getPaymentsByDate(Date date);

    @Query("SELECT * FROM payments")
    LiveData<List<Payments>> getAllPayments();

    @Query("SELECT * FROM payments WHERE paid = 1 ORDER BY id DESC")
    LiveData<List<Payments>> getAllPaymentsPaidDesc();

    @Query("SELECT * FROM payments WHERE paid = 0 ORDER BY id DESC")
    LiveData<List<Payments>> getAllPaymentsNotPaidDesc();

    @Query("SELECT * FROM payments")
    List<Payments> getAll();
}