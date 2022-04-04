package com.example.hcspaymentapp;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class EditDialog extends AppCompatDialogFragment {
    public static EditDialog create(long counterId) {
        Bundle args = new Bundle();
        args.putLong(ARG_ID, counterId);
        EditDialog dialog = new EditDialog();
        dialog.setArguments(args);
        return dialog;
    }

    private static final String ARG_ID = "ARG_ID";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.d_edit, null, false);
        final ContentValues cv = new ContentValues();
        EditText tariffText = view.findViewById(R.id.d_edit_tariff);
        long tariffId = getArguments().getLong(ARG_ID);
        long counterId = getArguments().getLong(ARG_ID);
        ConnectionDatabase repo = ConnectionDatabase.getInstance(getContext());
        Tariff tariff = repo.getTariff(tariffId);
        Counter counter = repo.getCounter(counterId);
        tariffText.setText(Double.toString(tariff.tariff_tariff));

        ColorPicker colorPicker = new ScrollColorPicker(view, ColorPicker.COLORS);
        colorPicker.setColor(counter.color);

        AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                .setTitle("Edit counter")
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = tariffText.getText().toString();
                    repo.changeTariff(counterId, name);
                    int color = colorPicker.getColor();
                    repo.changeColor(counterId, color);
                })
                .setView(view)
                .create();
        InputFilters.nameFilter(tariffText, alertDialog);
        return alertDialog;
    }
}
