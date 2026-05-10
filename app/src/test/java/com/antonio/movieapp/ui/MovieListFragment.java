package com.antonio.movieapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antonio.movieapp.R;
import com.antonio.movieapp.adapter.MovieAdapter;
import com.antonio.movieapp.api.RetrofitClient;
import com.antonio.movieapp.model.Movie;
import com.antonio.movieapp.model.MovieResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListFragment extends Fragment {

    private static final String API_KEY = "TU_API_KEY_AQUI";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RetrofitClient.getService().getPopularMovies(API_KEY)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Movie> movies = response.body().results;
                            MovieAdapter adapter = new MovieAdapter(movies, movie -> {
                                Bundle bundle = new Bundle();
                                bundle.putInt("movieId", movie.id);
                                bundle.putString("title", movie.title);
                                bundle.putString("overview", movie.overview);
                                bundle.putString("posterPath", movie.posterPath);
                                bundle.putFloat("rating", (float) movie.voteAverage);
                                bundle.putString("releaseDate", movie.releaseDate);
                                NavHostFragment.findNavController(MovieListFragment.this)
                                        .navigate(R.id.action_list_to_detail, bundle);
                            });
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_favorites) {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_list_to_favorites);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}