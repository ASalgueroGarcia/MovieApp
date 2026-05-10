package com.antonio.movieapp.model;

import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("id")
    public int id;

    @SerializedName("title")
    public String title;

    @SerializedName("overview")
    public String overview;

    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("vote_average")
    public double voteAverage;

    @SerializedName("release_date")
    public String releaseDate;
}