package com.twisstosin.popularmovies1.Networks;

import android.content.Context;


import com.twisstosin.popularmovies1.DataModels.Movie;
import com.twisstosin.popularmovies1.DataModels.MoviesRes;
import com.twisstosin.popularmovies1.Adapters.CustomMovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by twisstosin on 4/3/2017.
 */

public class JsonParser {

    private static MoviesRes moviesRes = new MoviesRes();
    private static List<Movie> movie_list;
    private static CustomMovieAdapter customMovieAdapter;

    public static void getSimpleMovieStringsFromJson(Context context, String movieJsonStr)
        throws JSONException{
        JSONObject movieJson = new JSONObject(movieJsonStr);

        moviesRes.setPage(movieJson.getDouble("page"));

        JSONArray results = movieJson.getJSONArray("results");

        moviesRes.setTotalPages(movieJson.getDouble("total_pages"));

        moviesRes.setTotalResults(movieJson.getDouble("total_results"));

        for (int i=0; i<results.length(); i++){
            JSONObject obj = results.getJSONObject(i);

            Movie addMovie = new Movie();
            addMovie.posterPath = obj.getString("poster_path");
            addMovie.voteCount = obj.getLong("vote_count");
            addMovie.overview = obj.getString("overview");
            addMovie.voteAverage = obj.getDouble("vote_average");
            addMovie.releaseDate = obj.getString("release_date");
            addMovie.id = obj.getInt("id");

            movie_list.add(addMovie);
        }
    }
}
