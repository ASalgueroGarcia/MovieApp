package com.antonio.movieapp.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class FavoriteMovie {
    @PrimaryKey
    public int id;
    public String title;
    public String posterPath;
    public double voteAverage;
    public String releaseDate;
}