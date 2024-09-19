package com.ruben.project.seliga.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.ruben.project.seliga.R;

public class HeaderDialog {

    private Dialog dialog;
    private String title = "";

    private LinearLayout nameContainer;
    private TextInputLayout nameInputLayout;
    private ImageButton addNameButton;
    private Button valueButton;
    private TextInputLayout valueInputLayout;
    private Button dateButton;
    private TextInputLayout dateInputLayout;
    private Button addButton;
    private Button clearButton;
    private LinearLayout valueContainer;
    private LinearLayout dateContainer;

    public HeaderDialog(Context context) {
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.header_dialog_itens, null);

        dialog = new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setView(view)
                .setNegativeButton(context.getString(R.string.close), (dialogInterface, i) -> dialogInterface.dismiss())
                .create();

        nameContainer = view.findViewById(R.id.name_container);
        nameInputLayout = view.findViewById(R.id.name_input_layout);
        addNameButton = view.findViewById(R.id.add_name_button);
        valueContainer = view.findViewById(R.id.value_container);
        dateContainer = view.findViewById(R.id.date_container);
        valueButton = view.findViewById(R.id.value_button);
        valueInputLayout = view.findViewById(R.id.value_input_layout);
        dateButton = view.findViewById(R.id.date_button);
        dateInputLayout = view.findViewById(R.id.date_input_layout);
        addButton = view.findViewById(R.id.add_button);
        clearButton = view.findViewById(R.id.clear_button);
    }

    public void show() {
        dialog.show();
    }

    //Visibility
    public void setNameContainerVisibility(boolean visible) {
        nameContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setAddNameButtonVisibility(boolean visible) {
        addNameButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setValueContainerVisibility(boolean visible) {
        valueContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setDateContainerVisibility(boolean visible) {
        dateContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setAddButtonVisibility(boolean visible) {
        addButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    //Actions
    public void setOnAddNameButtonClickListener(View.OnClickListener listener) {
        addNameButton.setOnClickListener(listener);
    }

    public void setOnValueButtonClickListener(View.OnClickListener listener) {
        valueButton.setOnClickListener(listener);
    }

    public void setOnDateButtonClickListener(View.OnClickListener listener) {
        dateButton.setOnClickListener(listener);
    }

    public void setOnAddButtonClickListener(View.OnClickListener listener) {
        addButton.setOnClickListener(listener);
    }

    public void setOnClearButtonClickListener(View.OnClickListener listener) {
        clearButton.setOnClickListener(listener);
    }

    //Background alterations
    public void setValueButtonColor(int color) {
        valueButton.setBackgroundColor(color);
    }

    public void setDateButtonColor(int color) {
        dateButton.setBackgroundColor(color);
    }

    public void setAddButtonColor(int color) {
        addButton.setBackgroundColor(color);
    }

    public void setClearButtonColor(int color) {
        clearButton.setBackgroundColor(color);
    }

    //Text color alterations
    public void setValueButtonTextColor(int color) {
        valueButton.setTextColor(color);
    }

    public void setDateButtonTextColor(int color) {
        dateButton.setTextColor(color);
    }

    public void setAddButtonTextColor(int color) {
        addButton.setTextColor(color);
    }

    public void setClearButtonTextColor(int color) {
        clearButton.setTextColor(color);
    }

    //Text alterations
    public void setTitle(String title) {
        this.title = title;
        dialog.setTitle(title);
    }

    public void setValueButtonText(String text) {
        valueButton.setText(text);
    }

    public void setDateButtonText(String text) {
        dateButton.setText(text);
    }

    public void setAddButtonText(String text) {
        addButton.setText(text);
    }

    public void setClearButtonText(String text) {
        clearButton.setText(text);
    }

    //Text input text getters
    public String getNameInputText() {
        if (nameInputLayout.getEditText() != null) {
            return nameInputLayout.getEditText().getText().toString();
        }
        return "";
    }

    public String getValueInputText() {
        if (valueInputLayout.getEditText() != null) {
            return valueInputLayout.getEditText().getText().toString();
        }
        return "";
    }

    public String getDateInputText() {
        if (dateInputLayout.getEditText() != null) {
            return dateInputLayout.getEditText().getText().toString();
        }
        return "";
    }

}