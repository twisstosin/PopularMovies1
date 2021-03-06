package com.twisstosin.popularmovies1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.twisstosin.popularmovies1.Adapters.CustomMovieAdapter;
import com.twisstosin.popularmovies1.DataModels.Movie;
import com.twisstosin.popularmovies1.DataModels.MoviesRes;
import com.twisstosin.popularmovies1.Networks.NetworkProcess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private TextView mMovieCountTextView, mPageTextView, mCurrentPageTextView, ofTextView;
    private ArrayList<Movie> movie_list;
    private Toast mToast;
    private MoviesRes moviesRes;
    private ImageView rightArrow, leftArrow;
    int currentPageNo = 1, totalPageNo = 1;
    private CoordinatorLayout coordinatorLayout;
    private String SORT_POPULAR = "movie/popular";
    private String SORT_RATED = "movie/top_rated";
    String sortType = SORT_POPULAR;
    CustomMovieAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Popular Movies");
        setSupportActionBar(toolbar);


        //Initializing variables for the various views used in the MainActivity

        movie_list = new ArrayList<Movie>();
        moviesRes = new MoviesRes();
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mMovieCountTextView = (TextView) findViewById(R.id.total_movies);
        mPageTextView = (TextView) findViewById(R.id.tv_total_pages);
        mPageTextView.setVisibility(View.GONE);
        rightArrow = (ImageView) findViewById(R.id.right_arrow);
        rightArrow.setOnClickListener(this);
        leftArrow = (ImageView) findViewById(R.id.left_arrow);
        leftArrow.setVisibility(View.GONE);
        leftArrow.setOnClickListener(this);
        ofTextView = (TextView) findViewById(R.id.tv_of);
        ofTextView.setVisibility(View.GONE);
        mCurrentPageTextView = (TextView) findViewById(R.id.tv_page_no);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_main);

        if(currentPageNo == 1){
            leftArrow.setVisibility(View.GONE);
        }


        //Setting the RecyclerView to a fixed size
        mRecyclerView = (RecyclerView) findViewById(R.id.movies_rv);
        mRecyclerView.setHasFixedSize(true);
        //Setting the layout manager for the RecyclerView
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        //Setting the adapter for the RecyclerView
        adapter = new CustomMovieAdapter(movie_list);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);



        if (isNetworkAvailable()){
            loadMovies(sortType,currentPageNo);

        }else{
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Internet Connect, Please" +
                    " Turn ON data and click RETRY",Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMovies(sortType, currentPageNo);
                }
            });
            snackbar.show();
        }
    }

    public  void showViews(){
        mPageTextView.setVisibility(View.VISIBLE);
        ofTextView.setVisibility(View.VISIBLE);
    }
    public void setTextViews(long pages, long results){
        mMovieCountTextView.setText(String.valueOf(results));
        mPageTextView.setText(String.valueOf(pages));
    }
    public void updateUI(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    public void showLoading(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void loadMovies(String sort, int page){
        URL movieRequestUrl = NetworkProcess.buildUrl(sort, page);
        new FetchMovieTask().execute(movieRequestUrl);
    }

    public class FetchMovieTask extends AsyncTask<URL, Void, Void>{
        long totalPages = 0;
        long totalResults = 0;
        @Override
        protected void onPreExecute() {
            showLoading();
        }

        @Override
        protected Void doInBackground(URL... params) {
            URL movieRequestUrl = params[0];
            try{
                final String jsonResponse =
                        NetworkProcess.getResponseFromHttpUrl(movieRequestUrl);

                try {
                    movie_list.clear();
                    JSONObject object = new JSONObject(jsonResponse);
                    totalPages = object.getLong("total_pages");
                    totalPageNo = (int) totalPages;
                    totalResults = object.getLong("total_results");
                    JSONArray jsonArray = object.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);


                        Movie addMovies = new Movie();
                        addMovies.overview = obj.getString("overview");
                        addMovies.releaseDate = obj.getString("release_date");
                        addMovies.title = obj.getString("title");
                        addMovies.voteAverage = obj.getDouble("vote_average");
                        addMovies.voteCount = obj.getLong("vote_count");
                        addMovies.id = obj.getInt("id");
                        addMovies.posterPath = obj.getString("poster_path");
                        movie_list.add(addMovies);
                    }

                }catch (JSONException j){
                    j.printStackTrace();
                    return null;
                }
                return null;
            }catch (Exception e){
                e.printStackTrace();
                Log.d("Exeption", e.getMessage());
                return null;
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (movie_list != null && movie_list.size() != 0) {
                updateUI();
                showViews();
                adapter.setMovieData(movie_list);
                setTextViews(totalPages, totalResults);
            }
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.right_arrow:
                if (currentPageNo < totalPageNo) {
                    currentPageNo++;
                    mCurrentPageTextView.setText(String.valueOf(currentPageNo));
                    leftArrow.setVisibility(View.VISIBLE);
                    loadMovies(sortType, currentPageNo);
                    break;
                } else if (currentPageNo == (totalPageNo - 1)) {
                    currentPageNo++;
                    mCurrentPageTextView.setText(String.valueOf(currentPageNo));
                    rightArrow.setVisibility(View.GONE);
                    loadMovies(sortType, currentPageNo);
                    break;
                }
            case R.id.left_arrow:
                if (currentPageNo > 1) {
                    currentPageNo--;
                    mCurrentPageTextView.setText(String.valueOf(currentPageNo));
                    loadMovies(sortType, currentPageNo);
                    break;
                } else if (currentPageNo == 2) {
                    leftArrow.setVisibility(View.INVISIBLE);
                    currentPageNo--;
                    mCurrentPageTextView.setText(String.valueOf(currentPageNo));
                    loadMovies(sortType, currentPageNo);
                    break;
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_sort_popular:
                if (sortType != SORT_POPULAR) {
                    sortType = SORT_POPULAR;
                    loadMovies(sortType, currentPageNo);
                }
                return true;
            case R.id.action_sort_rating:
                if (sortType != SORT_RATED){
                    sortType = SORT_RATED;
                    loadMovies(sortType, currentPageNo);
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isNetworkAvailable(){
        boolean status = false;
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            status = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return status;

    }
}
