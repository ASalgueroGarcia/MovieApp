package com.antonio.movieapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antonio.movieapp.R;
import com.antonio.movieapp.adapter.FavoriteAdapter;
import com.antonio.movieapp.db.AppDatabase;

public class FavoritesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadFavorites(recyclerView);
    }

    private void loadFavorites(RecyclerView recyclerView) {
        recyclerView.setAdapter(new FavoriteAdapter(
                AppDatabase.getInstance(requireContext()).movieDao().getAll(),
                fav -> {
                    // Pulsación larga → borrar
                    AppDatabase.getInstance(requireContext()).movieDao().delete(fav);
                    loadFavorites(recyclerView);
                },
                fav -> {
                    // Pulsación normal → abrir detalle
                    Bundle bundle = new Bundle();
                    bundle.putInt("movieId", fav.id);
                    bundle.putString("title", fav.title);
                    bundle.putString("overview", fav.overview != null ? fav.overview : "");
                    bundle.putString("posterPath", fav.posterPath);
                    bundle.putFloat("rating", (float) fav.voteAverage);
                    bundle.putString("releaseDate", fav.releaseDate);
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_favorites_to_detail, bundle);
                }
        ));
    }
}