package com.example.filmographyapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FilmAdapter.OnFilmDeleteListener, FilmAdapter.OnFilmEditListener {
    private DatabaseHelper dbHelper;
    private RecyclerView filmRecyclerView;
    private FilmAdapter filmAdapter;
    private ArrayList<Film> filmList;
    private EditText searchEditText;
    private Button searchButton, aboutButton, helpButton, addButton;
    private static final int EDIT_REQUEST_CODE = 2; // Unique request code for edit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);
        filmRecyclerView = findViewById(R.id.filmRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        aboutButton = findViewById(R.id.aboutButton);
        helpButton = findViewById(R.id.helpButton);
        addButton = findViewById(R.id.addButton);

        filmList = new ArrayList<>();
        filmAdapter = new FilmAdapter(this, filmList);
        filmRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        filmRecyclerView.setAdapter(filmAdapter);

        new LoadFilmsTask().execute(false, "");

        searchButton.setOnClickListener(v -> {
            String searchTitle = searchEditText.getText().toString();
            new LoadFilmsTask().execute(!searchTitle.isEmpty(), searchTitle);
        });

        aboutButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AboutActivity.class)));
        helpButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HelpActivity.class)));
        addButton.setOnClickListener(v -> startActivityForResult(new Intent(MainActivity.this, AddFilmActivity.class), 1));
    }

    private class LoadFilmsTask extends AsyncTask<Object, Void, ArrayList<Film>> {
        @Override
        protected ArrayList<Film> doInBackground(Object... params) {
            boolean isSearch = (boolean) params[0];
            String searchTitle = (String) params[1];
            ArrayList<Film> films = new ArrayList<>();
            Cursor cursor = isSearch ? dbHelper.searchByTitle(searchTitle) : dbHelper.getAllFilms();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                int year = cursor.getInt(2);
                String role = cursor.getString(3);
                films.add(new Film(id, title, year, role));
            }
            cursor.close();
            return films;
        }

        @Override
        protected void onPostExecute(ArrayList<Film> result) {
            filmList.clear();
            filmList.addAll(result);
            filmAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1 || requestCode == EDIT_REQUEST_CODE) && resultCode == RESULT_OK) {
            new LoadFilmsTask().execute(false, ""); // Refresh list for add or edit
        }
    }

    @Override
    public void onFilmDelete(int id) {
        new DeleteFilmTask().execute(id);
    }

    private class DeleteFilmTask extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... ids) {
            return dbHelper.deleteFilm(ids[0]);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // List updated in adapter
            }
        }
    }

    @Override
    public void onFilmEdit(Film film) {
        Intent intent = new Intent(MainActivity.this, EditFilmActivity.class);
        intent.putExtra("FILM_ID", film.getId());
        intent.putExtra("FILM_TITLE", film.getTitle());
        intent.putExtra("FILM_YEAR", film.getYear());
        intent.putExtra("FILM_ROLE", film.getRole());
        startActivityForResult(intent, EDIT_REQUEST_CODE);
    }
}