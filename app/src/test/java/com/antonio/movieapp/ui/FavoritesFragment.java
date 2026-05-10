package com.antonio.movieapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antonio.movieapp.R;
import com.antonio.movieapp.adapter.FavoriteAdapter;
import com.antonio.movieapp.db.AppDatabase;
import com.antonio.movieapp.db.FavoriteMovie;
import java.util.List;

public class FavoritesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadFavorites(recyclerView);
    }

    private void loadFavorites(RecyclerView recyclerView) {
        List<FavoriteMovie> favorites = AppDatabase.getInstance(getContext()).movieDao().getAll();
        FavoriteAdapter adapter = new FavoriteAdapter(favorites, fav -> {
            AppDatabase.getInstance(getContext()).movieDao().delete(fav);
            loadFavorites(recyclerView);
        });
        recyclerView.setAdapter(adapter);
    }
}