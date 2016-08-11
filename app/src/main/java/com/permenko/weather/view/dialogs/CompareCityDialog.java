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

public class CompareCityDialog extends DialogFragment {

    private static final String CITY = "CITY";

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

    public static CompareCityDialog newInstance(City city) {
        CompareCityDialog dialog = new CompareCityDialog();
        Bundle bundle = new Bundle();
        bundle.putString(CITY, new Gson().toJson(city));
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final City city = new Gson().fromJson(getArguments().getString(CITY), City.class);
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.error_city_not_found_title)
                .setMessage(getString(R.string.error_city_not_found_text, city.getName()))
                .setPositiveButton(R.string.action_add, (dialog, i) -> listener.addCity(city))
                .setNegativeButton(R.string.action_cancel, (dialog, i) -> dialog.dismiss())
                .create();
    }

    public interface ClickListener {
        void addCity(City city);
    }

}
