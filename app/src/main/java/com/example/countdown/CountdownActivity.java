package com.example.countdown;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CountdownActivity extends AppCompatActivity {

    private static final int DURATION_REQUEST_CODE = 1;
    private TextView tv;
    private long countdown = 0;
    private long startTime = 0;
    private long remaining = 0;
    private long t = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        tv = findViewById(R.id.countdownTextView);
    }

    public void changeTime(View view)
    {
        Intent intent = new Intent(this, DurationActivity.class);
        intent.putExtra("initialDuration", 3600);
        startActivityForResult(intent, DURATION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            long x = data.getLongExtra("duration", 0);

            // durée en seconde
            countdown = x;
            remaining = x;
            setRemainingSeconds();
        }
    }

    public void onStartButton(View view) {
        startTime = SystemClock.elapsedRealtime()/1000;
        findViewById(R.id.startButton).setEnabled(false);

    }

    public void onRefreshButton(View view) {
        t = SystemClock.elapsedRealtime()/1000;
        remaining = countdown - (t - startTime);
        setRemainingSeconds();
    }

    public void onStopButton(View view) {
        findViewById(R.id.startButton).setEnabled(true);
        findViewById(R.id.stopButton).setEnabled(false);
    }

    private void setRemainingSeconds() {
        long hours = remaining/3600;
        long minutes = (remaining%3600)/60;
        long seconds = remaining%60;
        tv.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }
}
