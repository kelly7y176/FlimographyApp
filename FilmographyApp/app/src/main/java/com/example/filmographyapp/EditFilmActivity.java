package com.example.filmographyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditFilmActivity extends AppCompatActivity {
    private EditText titleEditText, yearEditText, roleEditText;
    private DatabaseHelper dbHelper;
    private int filmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_film); // Reuse existing layout

        titleEditText = findViewById(R.id.titleEditText);
        yearEditText = findViewById(R.id.yearEditText);
        roleEditText = findViewById(R.id.roleEditText);
        Button saveButton = findViewById(R.id.saveButton);
        dbHelper = new DatabaseHelper(this);

        // Get film details from Intent
        Intent intent = getIntent();
        filmId = intent.getIntExtra("FILM_ID", -1);
        String title = intent.getStringExtra("FILM_TITLE");
        int year = intent.getIntExtra("FILM_YEAR", 0);
        String role = intent.getStringExtra("FILM_ROLE");

        // Pre-fill fields
        titleEditText.setText(title);
        yearEditText.setText(String.valueOf(year));
        roleEditText.setText(role);

        saveButton.setOnClickListener(v -> {
            String newTitle = titleEditText.getText().toString();
            String newYearStr = yearEditText.getText().toString();
            String newRole = roleEditText.getText().toString();

            if (newTitle.isEmpty() || newYearStr.isEmpty() || newRole.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            int newYear;
            try {
                newYear = Integer.parseInt(newYearStr);
                if (newYear < 1900 || newYear > 2025) {
                    Toast.makeText(this, "Year must be between 1900 and 2025", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid year", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.updateFilm(filmId, newTitle, newYear, newRole)) {
                Toast.makeText(this, "Film updated", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Error updating film", Toast.LENGTH_SHORT).show();
            }
        });
    }
}