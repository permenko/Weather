package com.permenko.weather.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.google.gson.Gson;
import com.permenko.weather.R;
import com.permenko.weather.data.City;

public class ErrorDialog extends DialogFragment {

    private static final String MESSAGE = "MESSAGE";

    public static ErrorDialog newInstance(int message) {
        ErrorDialog dialog = new ErrorDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(MESSAGE, message);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int message = getArguments().getInt(MESSAGE);
        return new AlertDialog.Builder(getContext())
                .setMessage(getString(message))
                .setPositiveButton(R.string.action_ok, null)
                .create();
    }

}
