package com.example.countdown;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class CountdownActivity extends AppCompatActivity {

    private static final int DURATION_REQUEST_CODE = 1;
    private TextView tv;
    private Button startButton;
    private Button refreshButton;
    private Button stopButton;
    private long countdown = 0;
    private long startTime = 0;
    private long remaining = 0;
    private long t = 0;
    private boolean isActive = false;
    private boolean refreshment = true;
    private AlarmManager am;
    private PendingIntent alarmIntent;
    private Handler handler;

    Runnable refreshRunnable = () -> {
        if (remaining < 0) {
            isActive = false;
            refreshment = false;
        }
        if (isActiveCountdown()) {
            // rafraîchissement
            t = SystemClock.elapsedRealtime()/1000;
            remaining = countdown - (t - startTime);
            setRemainingSeconds();
            // rappel dans 1 seconde
            handler.postDelayed(getRefreshRunnable(), 1000);
        }
    };

    private Runnable getRefreshRunnable() {
        return refreshRunnable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        handler = new Handler();
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmIntent = PendingIntent.getActivity(this, 1, new Intent(this, EndCountdown.class), 0);

        startButton = findViewById(R.id.startButton);
        refreshButton = findViewById(R.id.refreshButton);
        stopButton = findViewById(R.id.stopButton);
        tv = findViewById(R.id.countdownTextView);
        setDisabledButtons();
    }

    // Quand l'activité n'est plus au 1er plan
    @Override
    protected void onResume() {
        super.onResume();
        refreshment = true;
        setRemainingSeconds();
    }

    // Quand l'activité revient au 1er plan
    @Override
    protected void onPause() {
        super.onPause();
        refreshment = false;
    }

    // Lorsque l'on appuie sur le bouton SET DURATION
    public void changeTime(View view)
    {
        remaining = 0;
        setDisabledButtons();
        Intent intent = new Intent(this, DurationActivity.class);
        intent.putExtra("initialDuration", 3600);
        startActivityForResult(intent, DURATION_REQUEST_CODE);
    }

    // Quand on choisit une durée et qu'on revient sur cette activité
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            long x = data.getLongExtra("duration", 0);
            isActive = false;
            // durée en seconde
            countdown = x;
            remaining = x;
            setRemainingSeconds();
            startButton.setEnabled(true);
        }
    }

    public boolean isActiveCountdown() {
        if (remaining < 0) {
            setDisabledButtons();
        }
        return isActive;
    }

    public void setDisabledButtons() {
        startButton.setEnabled(false);
        refreshButton.setEnabled(false);
        stopButton.setEnabled(false);
    }

    // Lorsque l'on appuie sur le bouton START
    public void onStartButton(View view) {
        isActive = true;
        startTime = SystemClock.elapsedRealtime()/1000;
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        refreshButton.setEnabled(true);
        am.setExact(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + remaining , alarmIntent);
        handler.post(refreshRunnable);
    }

    // Lorsque l'on appuie sur le bouton REFRESH
    public void onRefreshButton(View view) {
        t = SystemClock.elapsedRealtime()/1000;
        Log.i("Refresh", String.valueOf(t));
        remaining = countdown - (t - startTime);
        Log.i("Remaining", String.valueOf(remaining));
        setRemainingSeconds();
    }

    // Lorsque l'on appuie sur le bouton STOP
    public void onStopButton(View view) {
        isActive = false;
        countdown = remaining;
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        refreshButton.setEnabled(false);
        am.cancel(alarmIntent);
    }

    // Met à jour l'affichage du temps
    private void setRemainingSeconds() {
        if (refreshment && remaining >= 0) {
            long hours = remaining/3600;
            long minutes = (remaining%3600)/60;
            long seconds = remaining%60;
            tv.setText(String.format(Locale.FRANCE, "%02d:%02d:%02d", hours, minutes, seconds));
        }
    }


}
