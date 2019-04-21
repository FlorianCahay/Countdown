package com.example.countdown;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class CountdownActivity extends AppCompatActivity {

    private static final int DURATION_REQUEST_CODE = 1;
    private static final int RECENT_DURATION_REQUEST_CODE = 2;
    private TextView tv;
    private Button startButton;
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
    private Clepsydra clepsydra;

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
        stopButton = findViewById(R.id.stopButton);
        tv = findViewById(R.id.countdownTextView);
        setDisabledButtons();
        clepsydra = findViewById(R.id.clepsydra);
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
        setDisabledButtons();
        Intent intent = new Intent(this, DurationActivity.class);
        intent.putExtra("initialDuration", 3600);
        startActivityForResult(intent, DURATION_REQUEST_CODE);
    }

    // Quand on choisit une durée et qu'on revient sur cette activité
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            int x = data.getIntExtra("duration", 0);
            RecentDurations.getInstance().addDuration(this, x);
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

    // Désactive les boutons
    public void setDisabledButtons() {
        startButton.setEnabled(false);
        stopButton.setEnabled(false);
    }

    // Lorsque l'on appuie sur le bouton START
    public void onStartButton(View view) {
        isActive = true;
        startTime = SystemClock.elapsedRealtime()/1000;
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + (remaining*1000)-1000 , alarmIntent);
        handler.post(refreshRunnable);
    }

    // Lorsque l'on appuie sur le bouton STOP
    public void onStopButton(View view) {
        isActive = false;
        countdown = remaining;
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        am.cancel(alarmIntent);
    }

    // Met à jour l'affichage du temps
    private void setRemainingSeconds() {
        if (remaining == 0) {
            tv.setText("00:00:00");
            clepsydra.setFillRatio(0.0);
        }
        if (refreshment && remaining >= 0) {
            long hours = remaining/3600;
            long minutes = (remaining%3600)/60;
            long seconds = remaining%60;
            tv.setText(String.format(Locale.FRANCE, "%02d:%02d:%02d", hours, minutes, seconds));
            if (countdown > 0) {
                clepsydra.setFillRatio((double)remaining/(double)countdown);
            }

        }
    }


    public void onRecentDurationsButton(View view) {
        Intent intent = new Intent(this, RecentDurationActivity.class);
        intent.putExtra("initialDuration", 3600);
        startActivityForResult(intent, RECENT_DURATION_REQUEST_CODE);
    }
}
