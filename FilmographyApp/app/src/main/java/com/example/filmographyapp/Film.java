package com.example.filmographyapp;

public class Film {
    private int id; // Add this
    private String title;
    private int year;
    private String role;

    public Film(int id, String title, int year, String role) { // Update constructor
        this.id = id;
        this.title = title;
        this.year = year;
        this.role = role;
    }

    public int getId() { return id; } // Add getter
    public String getTitle() { return title; }
    public int getYear() { return year; }
    public String getRole() { return role; }
}