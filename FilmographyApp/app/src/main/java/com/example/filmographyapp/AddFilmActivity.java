package com.example.filmographyapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddFilmActivity extends AppCompatActivity {
    private EditText titleEditText, yearEditText, roleEditText;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_film);

        titleEditText = findViewById(R.id.titleEditText);
        yearEditText = findViewById(R.id.yearEditText);
        roleEditText = findViewById(R.id.roleEditText);
        Button saveButton = findViewById(R.id.saveButton);
        dbHelper = new DatabaseHelper(this);

        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String yearStr = yearEditText.getText().toString();
            String role = roleEditText.getText().toString();

            if (title.isEmpty() || yearStr.isEmpty() || role.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            int year;
            try {
                year = Integer.parseInt(yearStr);
                if (year < 1900 || year > 2025) {
                    Toast.makeText(this, "Year must be between 1900 and 2025", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid year", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.addFilm(title, year, role)) {
                Toast.makeText(this, "Film added", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Error adding film", Toast.LENGTH_SHORT).show();
            }
        });
    }
}