package com.permenko.weather.view.cities.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.google.gson.Gson;
import com.permenko.weather.R;
import com.permenko.weather.model.City;

public class CompareCityDialog extends DialogFragment {

    private static final String CITY = "CITY";

    private ClickListener mClickListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mClickListener = (ClickListener) context;
        } catch (ClassCastException e) {
            //Reminder for the developer
            throw new ClassCastException(context.toString()
                    + " must implement ClickListener");
        }

    }

    public static CompareCityDialog newInstance(@NonNull City city) {
        CompareCityDialog dialog = new CompareCityDialog();
        Bundle bundle = new Bundle();
        bundle.putString(CITY, new Gson().toJson(city));
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final City city = new Gson().fromJson(getArguments().getString(CITY), City.class);
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.error_city_not_found_title)
                .setMessage(getString(R.string.error_city_not_found_text, city.getName()))
                .setPositiveButton(R.string.action_add, (dialog, i) -> mClickListener.onDialogComparePositiveClick(city))
                .setNegativeButton(R.string.action_cancel, (dialog, i) -> dialog.dismiss())
                .create();
    }

    public interface ClickListener {
        void onDialogComparePositiveClick(City city);
    }

}
