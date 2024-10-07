package com.ruben.project.seliga.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.ruben.project.seliga.data.database.AppDatabase;
import com.ruben.project.seliga.data.database.PaymentsDao;
import com.ruben.project.seliga.data.model.Payments;
import com.ruben.project.seliga.util.ClientWeek;

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
        executorService.execute(() -> {
            try {
                paymentsDao.insert(payment);
                Log.d("PaymentsRepository", "Pagamento inserido com sucesso.");
            } catch (Exception e) {
                Log.e("PaymentsRepository", "Erro ao inserir pagamento: ", e);
            }
        });
    }

    public void delete(Payments payment) {
        executorService.execute(() -> {
            try {
                paymentsDao.delete(payment);
                Log.d("PaymentsRepository", "Pagamento excluído com sucesso.");
            } catch (Exception e) {
                Log.e("PaymentsRepository", "Erro ao excluir pagamento: ", e);
            }
        });
    }

    public void deleteAllPaymentsPaid() {
        executorService.execute(() -> {
            try {
                paymentsDao.deleteAllPaymentsPaid();
                if (paymentsDao.getAllPayments().getValue() == null || paymentsDao.getAllPayments().getValue().isEmpty()) {
                    paymentsDao.resetCustomerIdSequence();
                }
                Log.d("PaymentsRepository", "Todos os pagamentos excluídos com sucesso.");
            } catch (Exception e) {
                Log.e("PaymentsRepository", "Erro ao excluir todos os pagamentos: ", e);
            }
        });
    }

    public void deleteAllPaymentsNotPaid() {
        executorService.execute(() -> {
            try {
                paymentsDao.deleteAllPaymentsNotPaid();
                Log.d("PaymentsRepository", "Todos os pagamentos não pagos excluídos com sucesso.");
                if (paymentsDao.getAllPayments().getValue() == null || paymentsDao.getAllPayments().getValue().isEmpty()) {
                    paymentsDao.resetCustomerIdSequence();
                }
            } catch (Exception e) {
                Log.e("PaymentsRepository", "Erro ao excluir todos os pagamentos não pagos: ", e);
            }
        });
    }

    public LiveData<List<Payments>> getAllPaymentsPaidDesc() {
        return paymentsDao.getAllPaymentsPaidDesc();
    }

    public LiveData<List<Payments>> getAllPaymentsNotPaidDesc() {
        return paymentsDao.getAllPaymentsNotPaidDesc();
    }

    public LiveData<Integer> getPaymentCount() {
        return paymentsDao.getPaymentCount();
    }

    public LiveData<Integer> getChargeCount() {
        return paymentsDao.getChargeCount();
    }

    public LiveData<List<Payments>> getPaymentsOfTheWeek(Date start, Date end) {
        return paymentsDao.getPaymentsOfTheWeek(start, end);
    }

    public LiveData<Integer> getWeeklyClientsCount(Date start, Date end) {
        return paymentsDao.getCustomerCountOfTheWeek(start, end);
    }

    public LiveData<List<ClientWeek>> getDistinctClientsWithCount(Date start, Date end) {
        return paymentsDao.getDistinctClientsWithCount(start, end);
    }
}
