package com.example.tapis_project_v0.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.tapis_project_v0.R;

public class CustomSpinner extends Dialog {
    private final ProgressBar progressBar;

    public CustomSpinner(@NonNull Context context) {
        super(context);
        setContentView(R.layout.spinner);
        this.progressBar = findViewById(R.id.progressBar);
    }
}
