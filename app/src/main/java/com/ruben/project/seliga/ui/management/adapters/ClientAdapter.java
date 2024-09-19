package com.ruben.project.seliga.ui.management.adapters;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ruben.project.seliga.R;
import com.ruben.project.seliga.data.model.Customer;
import com.ruben.project.seliga.data.repository.CustomerRepository;
import com.ruben.project.seliga.ui.common.ConfirmationDialog;

import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    private List<Customer> clientList;
    private final Context context;
    private final Application application;

    public ClientAdapter(List<Customer> clientList, Context context, Application application) {
        this.clientList = clientList;
        this.context = context;
        this.application = application;
    }

    public void setClientList(List<Customer> clientList) {
        this.clientList = clientList;
        notifyItemInserted(clientList.size());
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.management_list_item, parent, false);
        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        Customer client = clientList.get(position);
        holder.itemText.setText(client.getName());

        holder.itemRemove.setOnClickListener(v -> {
            ConfirmationDialog confirmationDialog = new ConfirmationDialog(context);
            CustomerRepository customerRepository = new CustomerRepository(application);

            confirmationDialog.setTitleText(context.getString(R.string.client_removal));
            confirmationDialog.setMessageText(context.getString(R.string.client_removal_question, client.getName()));

            confirmationDialog.setOnPositiveButtonClickListener(v2 -> {
                customerRepository.delete(client);
                clientList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, clientList.size());
                Toast.makeText(context, context.getText(R.string.item_removed_from_list), Toast.LENGTH_SHORT).show();
            });

            confirmationDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    public static class ClientViewHolder extends RecyclerView.ViewHolder {
        TextView itemText;
        ImageButton itemRemove;

        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.item_text);
            itemRemove = itemView.findViewById(R.id.item_remove);
        }
    }
}