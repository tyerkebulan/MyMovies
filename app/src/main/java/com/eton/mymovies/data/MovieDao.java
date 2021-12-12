package com.eton.mymovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies")
    LiveData<List<Movie>>  getAllMovies();

    @Query("SELECT * FROM movies WHERE id == :movieId")
    Movie getOneById(int movieId);

    @Query("DELETE FROM movies")
    void deleteAllMovies();

    @Insert()
    void inserMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM favourite_movies")
    LiveData<List<FavouriteMovie>>  getAllFavouriteMovies();

    @Insert()
    void inserFavouriteMovie(FavouriteMovie movie);

    @Delete
    void deleteFavouriteMovie(FavouriteMovie movie);

    @Query("SELECT * FROM favourite_movies WHERE id == :movieId")
    FavouriteMovie getOneFavouriteMovieById(int movieId);

}
