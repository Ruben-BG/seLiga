package com.ruben.project.seliga.viewmodel;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ruben.project.seliga.R;
import com.ruben.project.seliga.data.database.AppDatabase;
import com.ruben.project.seliga.data.model.Customer;
import com.ruben.project.seliga.data.model.Payments;
import com.ruben.project.seliga.data.model.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isPaymentInputEnabled = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> isChargeInputEnabled = new MutableLiveData<>(true);

    public LiveData<Boolean> getIsPaymentInputEnabled() {
        return isPaymentInputEnabled;
    }

    public LiveData<Boolean> getIsChargeInputEnabled() {
        return isChargeInputEnabled;
    }

    public void togglePaymentInput() {
        isPaymentInputEnabled.setValue(Boolean.FALSE.equals(isPaymentInputEnabled.getValue()));
    }

    public void toggleChargeInput() {
        isChargeInputEnabled.setValue(Boolean.FALSE.equals(isChargeInputEnabled.getValue()));
    }

    public void exportData(Context context) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            List<Customer> customers = db.customerDao().getAll();
            List<Payments> payments = db.paymentsDao().getAll();
            List<User> users = db.userDao().getAll();

            if (!payments.isEmpty() && Boolean.TRUE.equals(isPaymentInputEnabled.getValue())) {
                payments.removeIf(Payments::isPaid);
            }

            if (!payments.isEmpty() && Boolean.TRUE.equals(isChargeInputEnabled.getValue())) {
                payments.removeIf(payment -> !payment.isPaid());
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(new ExportData(customers, payments, users));

            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(downloadsDir, "dados_app_seliga.json");

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(json);
                Log.d("ExportViewModel", "Data exported successfully to " + file.getAbsolutePath());
            } catch (IOException e) {
                Log.e("ExportViewModel", "Failed to export data", e);
            }
        }).start();

        new Handler(Looper.getMainLooper()).post(() -> {
            Toast.makeText(context, context.getText(R.string.data_exported_successfully), Toast.LENGTH_SHORT).show();
        });
    }

    private static class ExportData {
        List<Customer> customers;
        List<Payments> payments;
        List<User> users;

        ExportData(List<Customer> customers, List<Payments> payments, List<User> users) {
            this.customers = customers;
            this.payments = payments;
            this.users = users;
        }
    }
}