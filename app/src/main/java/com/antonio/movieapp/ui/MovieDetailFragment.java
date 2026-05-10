package com.antonio.movieapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.antonio.movieapp.R;
import com.antonio.movieapp.db.AppDatabase;
import com.antonio.movieapp.db.FavoriteMovie;
import com.bumptech.glide.Glide;

public class MovieDetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args == null) return;

        ImageView ivPoster = view.findViewById(R.id.ivPoster);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvRating = view.findViewById(R.id.tvRating);
        TextView tvOverview = view.findViewById(R.id.tvOverview);
        TextView tvReleaseDate = view.findViewById(R.id.tvReleaseDate);
        Button btnFavorite = view.findViewById(R.id.btnFavorite);

        int movieId = args.getInt("movieId");
        String title = args.getString("title", "");
        String overview = args.getString("overview", "");
        String posterPath = args.getString("posterPath", "");
        float rating = args.getFloat("rating");
        String releaseDate = args.getString("releaseDate", "");

        tvTitle.setText(title);
        tvOverview.setText(overview);
        tvRating.setText("Puntuación: " + rating);
        tvReleaseDate.setText("Fecha: " + releaseDate);

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500" + posterPath)
                .into(ivPoster);

        AppDatabase db = AppDatabase.getInstance(requireContext());
        btnFavorite.setText(db.movieDao().isFavorite(movieId) > 0 ? "Quitar de favoritos" : "Añadir a favoritos");

        btnFavorite.setOnClickListener(v -> {
            if (db.movieDao().isFavorite(movieId) > 0) {
                FavoriteMovie fav = new FavoriteMovie();
                fav.id = movieId;
                db.movieDao().delete(fav);
                btnFavorite.setText("Añadir a favoritos");
                Toast.makeText(requireContext(), "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
            } else {
                FavoriteMovie fav = new FavoriteMovie();
                fav.id = movieId;
                fav.title = title;
                fav.posterPath = posterPath;
                fav.voteAverage = rating;
                fav.releaseDate = releaseDate;
                fav.overview = overview; // ← ahora se guarda la descripción
                db.movieDao().insert(fav);
                btnFavorite.setText("Quitar de favoritos");
                Toast.makeText(requireContext(), "Añadido a favoritos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}