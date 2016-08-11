package com.permenko.weather.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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
        showKeyboard();
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

    private void showKeyboard() {
        /*((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*/
    }

    private void hideKeyboard() {
        /*((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);*/

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
