package com.example.hcspaymentapp;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ConfirmDeleteDialog extends AppCompatDialogFragment {
    public interface Host {

        void onConfirm();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle("Удаление счётчика")
                .setMessage("Это действие нельзя отменить")
                .setPositiveButton("Подтвердить", (dialog, which) ->
                        ((Host) requireActivity()).onConfirm())
                .setNegativeButton("Отменить", null)
                .create();
    }
}
