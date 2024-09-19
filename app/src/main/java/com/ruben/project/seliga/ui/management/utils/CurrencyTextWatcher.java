package com.ruben.project.seliga.ui.management.utils;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyTextWatcher implements TextWatcher {
    private final EditText editText;
    private String current = "";

    public CurrencyTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // No action needed
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals(current)) {
            editText.removeTextChangedListener(this);

            String cleanString = s.toString().replaceAll("\\D", "");

            if (!cleanString.isEmpty()) {
                try {
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format((parsed / 100));

                    current = formatted;
                    editText.setText(formatted);
                    editText.setSelection(formatted.length());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            editText.addTextChangedListener(this);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // No action needed
    }
}