package com.ruben.project.seliga.ui.management.adapters;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.ruben.project.seliga.R;
import com.ruben.project.seliga.data.model.Payments;
import com.ruben.project.seliga.data.repository.CustomerRepository;
import com.ruben.project.seliga.data.repository.PaymentsRepository;
import com.ruben.project.seliga.ui.common.ConfirmationDialog;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private final List<Payments> paymentList;
    private final Context context;
    private final Application application;

    public PaymentAdapter(List<Payments> paymentList, Context context, Application application) {
        this.paymentList = paymentList;
        this.context = context;
        this.application = application;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.management_list_item, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payments payment = paymentList.get(position);

        CustomerRepository customerRepository = new CustomerRepository(application);
        customerRepository.getCustomerById(payment.getCustomerId()).observe((LifecycleOwner) context, customer -> {
            if (customer != null) {
                String text = customer.getName() + " - Pagou";

                if (payment.getValue() != -1) {
                    String formatted = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(payment.getValue());
                    text += " " + formatted;
                }

                text += " em " + new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(payment.getDate());

                holder.itemText.setText(text);
            }
        });

        holder.itemRemove.setOnClickListener(v -> {
            ConfirmationDialog confirmationDialog = new ConfirmationDialog(context);
            PaymentsRepository paymentsRepository = new PaymentsRepository(application);

            confirmationDialog.setTitleText(context.getString(R.string.payment_removal));
            confirmationDialog.setMessageText(context.getString(R.string.payment_removal_question, holder.itemText.getText()));

            confirmationDialog.setOnPositiveButtonClickListener(v2 -> {
                paymentsRepository.delete(payment);
                paymentList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, paymentList.size());
            });

            confirmationDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView itemText;
        ImageButton itemRemove;

        public PaymentViewHolder(@NonNull View view) {
            super(view);
            itemText = view.findViewById(R.id.item_text);
            itemRemove = view.findViewById(R.id.item_remove);
        }
    }

}
