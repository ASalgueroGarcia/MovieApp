package com.antonio.movieapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antonio.movieapp.R;
import com.antonio.movieapp.adapter.MovieAdapter;
import com.antonio.movieapp.api.RetrofitClient;
import com.antonio.movieapp.model.Movie;
import com.antonio.movieapp.model.MovieResponse;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListFragment extends Fragment {

    // ⚠️ Reemplaza con tu API key real de https://www.themoviedb.org
    private static final String API_KEY = "79990d72f445d40f741b8725a82db990";

    private MovieAdapter adapter;
    private LinearLayoutManager layoutManager;
    private int currentPage = 1;
    private boolean isLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MovieAdapter(new ArrayList<>(), movie -> {
            Bundle bundle = new Bundle();
            bundle.putInt("movieId", movie.id);
            bundle.putString("title", movie.title);
            bundle.putString("overview", movie.overview);
            bundle.putString("posterPath", movie.posterPath);
            bundle.putFloat("rating", (float) movie.voteAverage);
            bundle.putString("releaseDate", movie.releaseDate);
            Navigation.findNavController(view).navigate(R.id.action_list_to_detail, bundle);
        });
        recyclerView.setAdapter(adapter);

        // Cargar primera página
        loadMovies();

        // Detectar cuando el usuario llega al final → cargar siguiente página
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                // Si estamos cerca del final y no estamos cargando ya
                if (!isLoading && (visibleItemCount + firstVisibleItem) >= totalItemCount - 5) {
                    currentPage++;
                    loadMovies();
                }
            }
        });
    }

    private void loadMovies() {
        if (isLoading) return;
        isLoading = true;

        RetrofitClient.getService().getPopularMovies(API_KEY, currentPage).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                isLoading = false;
                if (response.body() == null || response.body().results == null) return;
                adapter.addMovies(response.body().results);
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                isLoading = false;
                if (getContext() != null) {
                    Toast.makeText(requireContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_favorites) {
            Navigation.findNavController(requireView()).navigate(R.id.action_list_to_favorites);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}