package com.antonio.movieapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MovieResponse {
    @SerializedName("results")
    public List<Movie> results;
}