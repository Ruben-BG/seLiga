package com.ruben.project.seliga.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.ruben.project.seliga.data.database.AppDatabase;
import com.ruben.project.seliga.data.database.PaymentsDao;
import com.ruben.project.seliga.data.model.Payments;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PaymentsRepository {
    private final PaymentsDao paymentsDao;
    private final ExecutorService executorService;

    public PaymentsRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        paymentsDao = db.paymentsDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Payments payment) {
        executorService.execute(() -> paymentsDao.insert(payment));
    }

    public void update(Payments payment) {
        executorService.execute(() -> paymentsDao.update(payment));
    }

    public void delete(Payments payment) {
        executorService.execute(() -> paymentsDao.delete(payment));
    }

    public LiveData<Payments> getPaymentById(int id) {
        return paymentsDao.getPaymentById(id);
    }

    public LiveData<List<Payments>> getPaymentsByDate(Date date) {
        return paymentsDao.getPaymentsByDate(date);
    }

    public LiveData<List<Payments>> getAllPayments() {
        return paymentsDao.getAllPayments();
    }
}