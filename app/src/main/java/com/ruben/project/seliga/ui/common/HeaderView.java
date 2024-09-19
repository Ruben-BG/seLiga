package com.ruben.project.seliga.ui.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import com.ruben.project.seliga.R;

public class HeaderView extends LinearLayout {

    private TextView titleTextView;
    private ImageButton actionButton;

    public HeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.header_layout, this, true);
        titleTextView = findViewById(R.id.header_title);
        actionButton = findViewById(R.id.header_button);
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setActionButton(String buttonText, OnClickListener listener) {
        actionButton.setOnClickListener(listener);
    }

    public void setActionButtonImage(@DrawableRes int resId) {
        actionButton.setImageResource(resId);
    }
}