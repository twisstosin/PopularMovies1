package com.twisstosin.popularmovies1.DataModels;

import java.util.List;

/**
 * Created by twisstosin on 4/3/2017.
 */

public class MoviesRes {

    public double page;
    public List<Movie> results = null;
    public double totalResults;
    public double totalPages;

    public double getPage() {
        return page;
    }

    public void setPage(double page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public double getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(double totalPages) {
        this.totalPages = totalPages;
    }

    public double getTotalResults() {

        return totalResults;
    }

    public void setTotalResults(double totalResults) {
        this.totalResults = totalResults;
    }
}
