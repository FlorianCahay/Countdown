package com.example.countdown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DurationActivity extends AppCompatActivity {
    private EditText hours;
    private EditText minutes;
    private EditText seconds;
    private Button validationButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hours = findViewById(R.id.editTextHours);
        hours.addTextChangedListener(timeWatcher);
        minutes = findViewById(R.id.editTextMinutes);
        minutes.addTextChangedListener(timeWatcher);
        seconds = findViewById(R.id.editTextSeconds);
        seconds.addTextChangedListener(timeWatcher);
        validationButton = findViewById(R.id.validationButton);
    }



    // Extrait un entier du champ e
    protected int extract(EditText e) {
        if (!e.getText().toString().matches(""))
            return Integer.parseInt(e.getText().toString());
        return 0;
    }

    // Convertit une heure ou une minute en seconde (type: 0 = heure, 1 = minute)
    protected int convertToSecond(int value, int type) {
        if (type == 0) {
            return value*3600;
        } else if (type == 1) {
            return value*60;
        }
        return value;
    }

    // Renvoie le temps choisi en seconde
    public void onClickValidate(View view) {

        int valueHours = extract(hours);
        int valueMinutes = extract(minutes);
        int valueSeconds = extract(seconds);
        int sum = convertToSecond(valueHours, 0) + convertToSecond(valueMinutes, 1) + valueSeconds;

        String result = "Duration: " + String.valueOf(sum) + " seconds";
        Toast t = Toast.makeText(this, result, Toast.LENGTH_SHORT);
        t.show();

        Intent intent = new Intent();
        intent.putExtra("duration", sum);
        setResult(RESULT_OK, intent);
        finish();
    }

    private final TextWatcher timeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (extract(hours) > 23 || extract(hours) < 0) {
                hours.setError("Invalid value (must be between 0 and 23)");
                validationButton.setEnabled(false);
            } else hours.setError(null);

            if (extract(minutes) > 59 || extract(minutes) < 0) {
                minutes.setError("Invalid value (must be between 0 and 59");
                validationButton.setEnabled(false);
            } else minutes.setError(null);
            if (extract(seconds) > 59 || extract(seconds) < 0) {
                seconds.setError("Invalid value (must be between 0 and 59");
                validationButton.setEnabled(false);
            } else seconds.setError(null);
            if (hours.getError() == null && minutes.getError() == null && seconds.getError() == null)
                validationButton.setEnabled(true);
        }
    };
}
