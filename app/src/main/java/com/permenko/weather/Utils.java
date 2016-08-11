package com.permenko.weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static String getDate() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    public static List<Integer> getInitialData() {
        List<Integer> initialData = new ArrayList<>();
        initialData.add(232422);
        initialData.add(2653265);
        initialData.add(524901);
        initialData.add(498677);
        initialData.add(563464);
        initialData.add(487846);
        initialData.add(3098030);
        initialData.add(658225);
        initialData.add(2759145);
        initialData.add(588409);
        initialData.add(3580477);
        initialData.add(2643743);
        initialData.add(551487);
        return initialData;
    }
}
