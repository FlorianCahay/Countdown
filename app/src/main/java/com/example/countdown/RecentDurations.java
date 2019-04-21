package com.example.countdown;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecentDurations {
    private static final RecentDurations instance = new RecentDurations();

    static RecentDurations getInstance() {
        return instance;
    }

    // Obtention des dernières durées utilisées
    List<Integer> getRecentDurations(Context context) {
        List<Integer> liste = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        for (Map.Entry<String, ?> entry : prefs.getAll().entrySet()) {
            liste.add((Integer) entry.getValue());
        }
        return liste;
    }

    // Ajout d'une nouvelle durée
    void addDuration(Context context, int duration) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(String.valueOf(duration), duration);
        editor.commit();
    }
}
