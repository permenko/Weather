package com.permenko.weather.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.permenko.weather.R;

public class AddCityDialog extends DialogFragment {

    private ClickListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ClickListener) context;
        } catch (ClassCastException e) {
            //do something
            e.printStackTrace();
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setView(R.layout.dialog_add_city)
                .setTitle(R.string.add_city)
                .setPositiveButton(R.string.action_add, (dialog, i) -> {
                    final AlertDialog alertDialog = (AlertDialog) dialog;
                    final String cityName = ((EditText) alertDialog.findViewById(R.id.city_name_input)).getText().toString();
                    listener.getWeather(cityName);
                })
                .setNegativeButton(R.string.action_cancel, (dialog, i) -> {
                    hideKeyboard();
                    dialog.dismiss();
                })
                .create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        showKeyboard();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    // should call only when dialog created
    private void showKeyboard() {
        android.app.Dialog dialog = getDialog();
        if (dialog != null) dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public interface ClickListener {
        void getWeather(String cityName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideKeyboard();
    }
}
