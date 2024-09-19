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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChargeAdapter extends RecyclerView.Adapter<ChargeAdapter.ChargeViewHolder> {

    private final List<Payments> chargeList;
    private final Context context;
    private final Application application;

    public ChargeAdapter(List<Payments> chargeList, Context context, Application application) {
        this.chargeList = chargeList;
        this.context = context;
        this.application = application;
    }

    @NonNull
    @Override
    public ChargeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.management_list_item, parent, false);
        return new ChargeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChargeViewHolder holder, int position) {
        Payments charges = chargeList.get(position);

        CustomerRepository customerRepository = new CustomerRepository(application);
        customerRepository.getCustomerById(charges.getCustomerId()).observe((LifecycleOwner) context, customer -> {
            if (customer != null) {
                String text = customer.getName() + " - Deve";

                if (charges.getValue() != -1) {
                    String formatted = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(charges.getValue());
                    text += " " + formatted;
                }

                if (charges.getDate().after(new Date())) {
                    text += " até " + new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(charges.getDate());
                } else {
                    text += " desde " + new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(charges.getDate());
                }

                holder.itemText.setText(text);
            }
        });

        holder.itemRemove.setOnClickListener(v -> {
            ConfirmationDialog confirmationDialog = new ConfirmationDialog(context);
            PaymentsRepository paymentsRepository = new PaymentsRepository(application);

            confirmationDialog.setTitleText(context.getString(R.string.payment_removal));
            confirmationDialog.setMessageText(context.getString(R.string.payment_removal_question, holder.itemText.getText()));

            confirmationDialog.setOnPositiveButtonClickListener(v2 -> {
                paymentsRepository.delete(charges);
                chargeList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, chargeList.size());
            });

            confirmationDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return chargeList.size();
    }


    public static class ChargeViewHolder extends RecyclerView.ViewHolder {
        TextView itemText;
        ImageButton itemRemove;

        public ChargeViewHolder(@NonNull View view) {
            super(view);
            itemText = view.findViewById(R.id.item_text);
            itemRemove = view.findViewById(R.id.item_remove);
        }
    }

}
