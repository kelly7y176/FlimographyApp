package com.example.filmographyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {
    private ArrayList<Film> filmList;
    private Context context;
    private OnFilmDeleteListener deleteListener;
    private OnFilmEditListener editListener; // New callback

    // Delete callback interface
    public interface OnFilmDeleteListener {
        void onFilmDelete(int id);
    }

    // Edit callback interface
    public interface OnFilmEditListener {
        void onFilmEdit(Film film);
    }

    public FilmAdapter(Context context, ArrayList<Film> filmList) {
        this.context = context;
        this.filmList = filmList;
        if (context instanceof OnFilmDeleteListener) {
            this.deleteListener = (OnFilmDeleteListener) context;
        }
        if (context instanceof OnFilmEditListener) {
            this.editListener = (OnFilmEditListener) context;
        }
    }

    @Override
    public FilmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_item, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmViewHolder holder, int position) {
        Film film = filmList.get(position);
        holder.titleTextView.setText(film.getTitle());
        holder.yearTextView.setText(String.valueOf(film.getYear()));
        holder.roleTextView.setText(film.getRole());

        holder.deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onFilmDelete(film.getId());
                filmList.remove(position);
                notifyItemRemoved(position);
            }
        });

        holder.editButton.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onFilmEdit(film);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    public static class FilmViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, yearTextView, roleTextView;
        Button deleteButton, editButton;

        public FilmViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            roleTextView = itemView.findViewById(R.id.roleTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}