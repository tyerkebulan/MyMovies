package com.eton.mymovies.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {


    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";

    private static final String BASE_URL_VIDEOS = "https://api.themoviedb.org/3/movie/%s/videos";

    private static final String BASE_URL_REVIEWS = "https://api.themoviedb.org/3/movie/%s/reviews";


    private static final String PARAMS_API_KEY = "api_key";
    private static final String PARAMS_API_LANGUAGE = "language";
    private static final String PARAMS_SORT_BY = "sort_by";
    private static final String INCLUDE_ADULT = "include_adult";
    private static final String INCLUDE_VIDEO = "include_video";
    private static final String PARAMS_PAGE = "page";
    private static final String PARAMS_MIN_VOTE_COUNT = "vote_count.gte";


    private   static  final String API_KEY = "3d892fa62834897e150cccedeb145c42";

    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_VOTE = "vote_average.desc";
    private static final String MIN_VOTE_COUNT_VALUE = "1000";

    public static final int POPULARITY = 0;
    public static final int TOP_RATED = 1;

    public static URL buildVideosURL(int id,String language){


       Uri uri = Uri.parse(String.format(BASE_URL_VIDEOS,id)).buildUpon()
               .appendQueryParameter(PARAMS_API_KEY,API_KEY)
               .appendQueryParameter(PARAMS_API_LANGUAGE,language).build();

        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL buildReviewsURL(int id,String lang){


        Uri uri = Uri.parse(String.format(BASE_URL_REVIEWS,id)).buildUpon()
                .appendQueryParameter(PARAMS_API_LANGUAGE,lang)
                .appendQueryParameter(PARAMS_API_KEY,API_KEY)
                .build();

        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static URL buildURL(int sortBy,int page,String language){
        URL result = null;
        String methodOfSort ;
        boolean adult = false;
        if(sortBy == POPULARITY){
            methodOfSort = SORT_BY_POPULARITY;
        }
        else {
            methodOfSort = SORT_BY_VOTE;
        }
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY,API_KEY)
                .appendQueryParameter(PARAMS_API_LANGUAGE,language)
                .appendQueryParameter(PARAMS_SORT_BY,methodOfSort)
                .appendQueryParameter(PARAMS_MIN_VOTE_COUNT,MIN_VOTE_COUNT_VALUE)
                .appendQueryParameter(INCLUDE_ADULT, String.valueOf(true))
                .appendQueryParameter(INCLUDE_VIDEO,String.valueOf(false))
                .appendQueryParameter(PARAMS_PAGE,Integer.toString(page)).build();

        try {
             result = new URL(uri.toString());
             Log.i("MyURL",uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getReviewJSONFromNetwork(int id,String lang){
        URL url = buildReviewsURL(id,lang);
        JSONObject result = null;
        try {
            result =  new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  result;
    }

    public static JSONObject getVideoJSONFromNetwork(int id,String lang){
        URL url = buildVideosURL(id,lang);
        JSONObject result = null;
        try {
            result =  new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  result;
    }

    public static JSONObject getJSONFromNetwork(int sortBy,int page,String lang){
        URL url = buildURL(sortBy,page,lang);
        JSONObject result = null;
        try {
           result =  new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  result;
    }

    public static class JSONLoader extends AsyncTaskLoader<JSONObject>{

        private Bundle bundle;

        private  OnStartLoadingListener onStartLoadingListener;
        public interface OnStartLoadingListener{
            void onStartLoading();
        }

        public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
            this.onStartLoadingListener = onStartLoadingListener;
        }

        public JSONLoader(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle=bundle;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if(onStartLoadingListener !=null){
                onStartLoadingListener.onStartLoading();
            }
            forceLoad();
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {
            if(bundle==null) {
                return null;
            }
            String urlAsString = bundle.getString("url");
            URL url = null;
            try {
                url = new URL(urlAsString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            JSONObject result1 = null;
            if (url == null ){
                return  null;
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder builder = new StringBuilder();
                String line= reader.readLine();
                while (line!=null){
                    builder.append(line);
                    line = reader.readLine();
                }
                result1 = new JSONObject(builder.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
            }
            return result1;
        }
    }

    private static class  JSONLoadTask extends AsyncTask<URL,Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject result1 = null;
            if (urls == null || urls.length == 0){
                return  null;
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) urls[0].openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder builder = new StringBuilder();
                String line= reader.readLine();
                while (line!=null){
                    builder.append(line);
                    line = reader.readLine();
                }
                result1 = new JSONObject(builder.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
            }
            return result1;
        }
    }

}
