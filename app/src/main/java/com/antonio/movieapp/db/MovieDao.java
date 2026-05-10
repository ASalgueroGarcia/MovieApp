package com.antonio.movieapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteMovie movie);

    @Delete
    void delete(FavoriteMovie movie);

    @Query("SELECT * FROM favorites")
    List<FavoriteMovie> getAll();

    @Query("SELECT COUNT(*) FROM favorites WHERE id = :id")
    int isFavorite(int id);
}