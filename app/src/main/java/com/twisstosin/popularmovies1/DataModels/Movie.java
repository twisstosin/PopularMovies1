package com.twisstosin.popularmovies1.DataModels;

/**
 * Created by twisstosin on 4/3/2017.
 */

import java.util.List;

public class Movie {

    public String posterPath;
    public boolean adult;
    public String overview;
    public String releaseDate;
    public List<Long> genreIds = null;
    public long id;
    public String originalTitle;
    public String originalLanguage;
    public String title;
    public String backdropPath;
    public double popularity;
    public long voteCount;
    public boolean video;
    public double voteAverage;


}