package com.example.countdown;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import static android.media.RingtoneManager.TYPE_ALARM;

public class EndCountdown extends AppCompatActivity {
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_countdown);
        Uri actualDefaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, TYPE_ALARM);
        player = MediaPlayer.create(this, actualDefaultRingtoneUri);
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.setLooping(true);
        player.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.stop();
    }

    public void quit(View view) {
        finish();
    }
}
