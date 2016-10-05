package com.permenko.weather.view.cities.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.permenko.weather.R;
import com.permenko.weather.model.City;

public class DeleteCityDialog extends DialogFragment {

    private static final String POSITION = "POSITION";
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

    public static DeleteCityDialog newInstance(@NonNull City city, int position) {
        DeleteCityDialog dialog = new DeleteCityDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CITY, city);
        bundle.putInt(POSITION, position);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final int position = getArguments().getInt(POSITION);
        final City city = (City) getArguments().getSerializable(CITY);
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.city_delete_title)
                .setPositiveButton(R.string.action_delete, (dialog, i) -> mClickListener.onDialogDeletePositiveClick(position, city))
                .setNegativeButton(R.string.action_cancel, (dialog, i) -> dialog.dismiss())
                .create();
    }

    public interface ClickListener {
        void onDialogDeletePositiveClick(int position, City city);
    }

}
