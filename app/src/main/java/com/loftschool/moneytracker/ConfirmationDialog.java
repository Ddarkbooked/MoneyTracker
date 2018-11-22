package com.loftschool.moneytracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;



public class ConfirmationDialog extends DialogFragment {

    private ConfirmationDialogListener confirmationDialogListener;

    public void setConfirmationDialogListener(ConfirmationDialogListener confirmationDialogListener) {
        this.confirmationDialogListener = confirmationDialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity()); // Не выживает при повороте экрана
                dialog.setTitle(R.string.app_name)
                    .setCancelable(false)
                    .setMessage("Вы уверены?")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            confirmationDialogListener.removeSelectedItems();
                            confirmationDialogListener.closeActionMode();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confirmationDialogListener.closeActionMode();
                        }
                    });

        return dialog.create();
    }

}
