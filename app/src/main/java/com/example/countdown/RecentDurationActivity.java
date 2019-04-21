package com.example.countdown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class RecentDurationActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_duration);

        listView = findViewById(R.id.listView);
        List<Integer> recentDurations = RecentDurations.getInstance().getRecentDurations(this);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, recentDurations);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            int duration = recentDurations.get(position);
            setResult(RESULT_OK, new Intent().putExtra("duration", duration));
            RecentDurations.getInstance().addDuration(RecentDurationActivity.this, duration);
            finish();
        });
    }
}
