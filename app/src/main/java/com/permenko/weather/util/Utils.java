package com.permenko.weather.util;

import com.permenko.weather.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

//todo: rename
public class Utils {

    //todo: add something like "updated 10 min ago" for each city
    public static String getDate() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    //hardcoded list of cities for non empty first launch
    public static List<Integer> getInitialData() {
        List<Integer> initialData = new ArrayList<>();
        initialData.add(524901); //moscow
        initialData.add(2950159); //berlin
        initialData.add(658225); //helsinki
        initialData.add(2643743); //london
        initialData.add(625144); //minsk
        initialData.add(588409); //tallinn
        initialData.add(593116); //vilnius
        initialData.add(456172); //riga
        return initialData;
    }

    //todo: change icons (quality too bad)
    public static int getIcon(String iconName) {
        switch (iconName) {
            case "01d":
                return R.drawable.d01;
            case "01n":
                return R.drawable.n01;
            case "02d":
                return R.drawable.d02;
            case "02n":
                return R.drawable.n02;
            case "03d":
                return R.drawable.dn03;
            case "03n":
                return R.drawable.dn03;
            case "04d":
                return R.drawable.dn03;
            case "04n":
                return R.drawable.dn03;
            case "09d":
                return R.drawable.dn09;
            case "09n":
                return R.drawable.dn09;
            case "10d":
                return R.drawable.d10;
            case "10n":
                return R.drawable.n10;
            case "11d":
                return R.drawable.dn11;
            case "11n":
                return R.drawable.dn11;
            case "13d":
                return R.drawable.dn13;
            case "13n":
                return R.drawable.dn13;
            case "50d":
                return R.drawable.dn50;
            case "50n":
                return R.drawable.dn50;
            default:
                return android.R.color.transparent;
        }
    }
}
