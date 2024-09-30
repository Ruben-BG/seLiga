package com.ruben.project.seliga.ui.adapters;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.ruben.project.seliga.R;
import com.ruben.project.seliga.data.model.Payments;
import com.ruben.project.seliga.data.repository.CustomerRepository;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final List<Payments> eventList;
    private final Context context;
    private final Application application;

    public EventAdapter(List<Payments> eventList, Context context, Application application) {
        this.eventList = eventList;
        this.context = context;
        this.application = application;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekly_event_list_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Payments payment = eventList.get(position);

        CustomerRepository customerRepository = new CustomerRepository(application);
        customerRepository.getCustomerById(payment.getCustomerId()).observe((LifecycleOwner) context, customer -> {
            if (customer != null) {
                String text = customer.getName();
                String dateText = new SimpleDateFormat("dd/MM", Locale.getDefault()).format(payment.getDate());

                if (payment.isPaid()) {
                    text += " - Pagou";
                } else {
                    text += " - Devendo";
                }

                if (payment.getValue() != -1) {
                    String formatted = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(payment.getValue());
                    text += " " + formatted;
                }

                holder.itemDetail.setText(text);
                holder.itemDate.setText(dateText);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemDetail;
        private final TextView itemDate;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            itemDetail = itemView.findViewById(R.id.textView_detail_item_text);
            itemDate = itemView.findViewById(R.id.textView_date_text);
        }
    }

}
