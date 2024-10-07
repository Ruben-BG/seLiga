package com.ruben.project.seliga.ui.common;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.ruben.project.seliga.R;
import com.ruben.project.seliga.ui.management.utils.CurrencyTextWatcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class HeaderDialog {

    private Dialog dialog;
    private String title = "";

    private LinearLayout nameContainer;
    private TextInputLayout nameInputLayout;
    private ImageButton addNameButton;
    private TextInputLayout selectNameInputLayout;
    private MaterialAutoCompleteTextView selectNameInput;
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
        selectNameInputLayout = view.findViewById(R.id.client_select_input_layout);
        selectNameInput = view.findViewById(R.id.client_select_input);
        valueContainer = view.findViewById(R.id.value_container);
        dateContainer = view.findViewById(R.id.date_container);
        valueButton = view.findViewById(R.id.value_button);
        valueInputLayout = view.findViewById(R.id.value_input_layout);
        dateButton = view.findViewById(R.id.date_button);
        dateInputLayout = view.findViewById(R.id.date_input_layout);
        addButton = view.findViewById(R.id.add_button);
        clearButton = view.findViewById(R.id.clear_button);

        if (valueInputLayout.getEditText() != null) {
            valueInputLayout.getEditText().addTextChangedListener(new CurrencyTextWatcher(valueInputLayout.getEditText()));
        }

        setupDateInputLayout(context);
    }

    public void show() {
        dialog.show();
    }

    //Visibility
    public void setNameContainerVisibility(boolean visible) {
        nameContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setSelectNameInputVisibility(boolean visible) {
        selectNameInputLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
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

    public void setOnSelectNameInputItemClickListener(AdapterView.OnItemClickListener listener) {
        selectNameInput.setOnItemClickListener(listener);
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

    //Set adapter
    public void setSelectNameInputAdapter(ArrayAdapter<String> adapter) {
        selectNameInput.setAdapter(adapter);
    }

    //Background alterations
    public void setValueButtonColor(int color) {
        valueButton.setBackgroundColor(color);
    }

    public void setDateButtonColor(int color) {
        dateButton.setBackgroundColor(color);
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

    //Text input getters
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

    public Date getDateInputText() {
        if (dateInputLayout.getEditText() != null) {
            return convertStringToDate(dateInputLayout.getEditText().getText().toString());
        }
        return null;
    }

    //Text input text clearers
    public void clearNameInputText() {
        if (nameInputLayout.getEditText() != null) {
            nameInputLayout.getEditText().setText("");
            nameInputLayout.getEditText().clearFocus();
        }
    }

    public void clearSelectNameInputText() {
        if (selectNameInputLayout.getEditText() != null) {
            selectNameInputLayout.getEditText().setText("");
            selectNameInputLayout.getEditText().clearFocus();
        }
    }

    public void clearValueInputText() {
        if (valueInputLayout.getEditText() != null) {
            valueInputLayout.getEditText().setText("");
            valueInputLayout.getEditText().clearFocus();
        }
    }

    public void clearDateInputText() {
        if (dateInputLayout.getEditText() != null) {
            dateInputLayout.getEditText().setText("");
            dateInputLayout.getEditText().clearFocus();
        }
    }

    //Text input enabled
    public void setValueInputEnabled(boolean enabled) {
        valueInputLayout.setEnabled(enabled);
    }

    public void setDateInputEnabled(boolean enabled) {
        dateInputLayout.setEnabled(enabled);
    }

    //Others
    private void setupDateInputLayout(Context context) {
        Objects.requireNonNull(dateInputLayout.getEditText()).setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        Objects.requireNonNull(dateInputLayout.getEditText()).setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
                    },
                    year, month, day);
            datePickerDialog.show();
        });
    }

    private Date convertStringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("d-M-yyyy");
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void clearAllPaymentDialog() {
        clearSelectNameInputText();
        clearValueInputText();
        clearDateInputText();
        setValueInputEnabled(true);
        setDateInputEnabled(true);
    }

}