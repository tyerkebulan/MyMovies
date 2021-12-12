package com.eton.mymovies.data;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eton.mymovies.FavoriteActivity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MovieViewModel extends ViewModel {


    private static MovieDatabase database;
    private LiveData<List<Movie>> movies;
    private LiveData<List<FavouriteMovie>> favouriteMovies;
    public void getALlMobies(Application application) {
        database = MovieDatabase.getInstance(application);
        movies = database.movieDao().getAllMovies();
        favouriteMovies = database.movieDao().getAllFavouriteMovies();
    }
    public Movie getMovieById(int id){
        try {
            return new GetMovieTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FavouriteMovie getFavouriteMovieById(int id){
        try {
            return new GetFavouriteMovieTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<FavouriteMovie>> getFavouriteMovies() {
        return favouriteMovies;
    }

    public void insertMovie(Movie movie){
        new InsertMovieTask().execute(movie);
    }

    public void insertFavouriteMovie(FavouriteMovie movie){
        new InsertFavouriteMovieTask().execute(movie);
    }

    public void deleteAllMovies(){

            new DeleteAllMovieTask().execute();

    }
    public void deleteMovie(Movie movie){

        new DeleteMovieTask().execute(movie);

    }

    public void deleteFavouriteMovie(FavouriteMovie movie){

        new DeleteFavouriteMovieTask().execute(movie);

    }


    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    private static class DeleteMovieTask extends AsyncTask<Movie,Void,Void>{


        @Override
        protected Void doInBackground(Movie... movies) {
            if(movies != null && movies.length>0){
                database.movieDao().deleteMovie(movies[0]);
            }
            return null;
        }
    }
    private static class DeleteFavouriteMovieTask extends AsyncTask<FavouriteMovie,Void,Void>{


        @Override
        protected Void doInBackground(FavouriteMovie... movies) {
            if(movies != null && movies.length>0){
                database.movieDao().deleteFavouriteMovie(movies[0]);
            }
            return null;
        }
    }
    private static class DeleteAllMovieTask extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {

                 database.movieDao().deleteAllMovies();

            return null;
        }
    }
    private static class GetMovieTask extends AsyncTask<Integer,Void,Movie>{

        @Override
        protected Movie doInBackground(Integer... integers) {
            if(integers != null && integers.length>0){
                return database.movieDao().getOneById(integers[0]);
            }
            return null;
        }
    }
    private static class GetFavouriteMovieTask extends AsyncTask<Integer,Void,FavouriteMovie>{

        @Override
        protected FavouriteMovie doInBackground(Integer... integers) {
            if(integers != null && integers.length>0){
                return database.movieDao().getOneFavouriteMovieById(integers[0]);
            }
            return null;
        }
    }


    private static class InsertMovieTask extends AsyncTask<Movie,Void,Void>{

            @Override
            protected Void doInBackground(Movie... movies) {
                if(movies!=null && movies.length>0){
                    database.movieDao().inserMovie(movies[0]);
                }
                return null;
            }
    }

    private static class InsertFavouriteMovieTask extends AsyncTask<FavouriteMovie,Void,Void>{

        @Override
        protected Void doInBackground(FavouriteMovie... movies) {
            if(movies!=null && movies.length>0){
                database.movieDao().inserFavouriteMovie(movies[0]);
            }
            return null;
        }
    }
}
