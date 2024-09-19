package com.ruben.project.seliga.ui.common;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ruben.project.seliga.R;

public class ConfirmationDialog {

    private final AlertDialog dialog;

    public ConfirmationDialog(Context context) {
        dialog = new MaterialAlertDialogBuilder(context)
                .setNegativeButton(context.getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void setTitleText(String text) {
        dialog.setTitle(text);
    }

    public void setMessageText(String text) {
        dialog.setMessage(text);
    }

    public void setOnPositiveButtonClickListener(View.OnClickListener listener) {
        dialog.setButton(
                AlertDialog.BUTTON_POSITIVE,
                dialog.getContext().getString(R.string.yes),
                (dialogInterface, i) -> listener.onClick(null)
        );
    }
}