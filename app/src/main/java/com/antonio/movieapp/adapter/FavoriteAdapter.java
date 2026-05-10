package com.antonio.movieapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antonio.movieapp.R;
import com.antonio.movieapp.db.FavoriteMovie;
import com.bumptech.glide.Glide;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<FavoriteMovie> favorites;
    private OnDeleteClickListener deleteListener;
    private OnClickListener clickListener;

    public interface OnDeleteClickListener {
        void onDelete(FavoriteMovie movie);
    }

    public interface OnClickListener {
        void onClick(FavoriteMovie movie);
    }

    public FavoriteAdapter(List<FavoriteMovie> favorites,
                           OnDeleteClickListener deleteListener,
                           OnClickListener clickListener) {
        this.favorites = favorites;
        this.deleteListener = deleteListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteMovie fav = favorites.get(position);
        holder.tvTitle.setText(fav.title);
        holder.tvRating.setText("⭐ " + fav.voteAverage);
        Glide.with(holder.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w500" + fav.posterPath)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.ivPoster);

        // Pulsación normal → abrir detalle
        holder.itemView.setOnClickListener(v -> clickListener.onClick(fav));

        // Pulsación larga → borrar
        holder.itemView.setOnLongClickListener(v -> {
            deleteListener.onDelete(fav);
            favorites.remove(position);
            notifyItemRemoved(position);
            return true;
        });
    }

    @Override
    public int getItemCount() { return favorites.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle, tvRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvRating = itemView.findViewById(R.id.tvRating);
        }
    }
}