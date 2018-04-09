package com.amohnacs.moviemenu.main;

import android.content.Context;

import java.util.ArrayList;
import java.util.Observable;

import android.databinding.ObservableInt;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.util.Log;
import android.view.View;

import com.amohnacs.moviemenu.BuildConfig;
import com.amohnacs.moviemenu.R;
import com.amohnacs.moviemenu.model.MovieMenuRow;
import com.amohnacs.moviemenu.MovieMenu;
import com.amohnacs.moviemenu.domain.MovieDatabaseClient;
import com.amohnacs.moviemenu.domain.RetrofitClientGenerator;
import com.amohnacs.moviemenu.model.Movie;
import com.amohnacs.moviemenu.model.MovieResponse;
import com.amohnacs.moviemenu.utils.CollectionUtils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by adrianmohnacs on 4/5/18.
 */

public class MovieViewModel extends Observable implements MVVM.ViewActions {
    private static final String TAG = MovieViewModel.class.getSimpleName();

    private static final int EIGHTEEN_MOVIES_ID = 0;
    private static final int SEVENTEEN_MOVIES_ID = 1;
    private static final int SIXTEEN_MOVIES_ID = 2;
    private static final int FIFTEEN_MOVIES_ID = 3;

    private static final int EIGHTEEN_YEAR = 2018;
    private static final int SEVENTEEN_YEAR = 2017;
    private static final int SIXTEEN_YEAR = 2016;
    private static final int FIFTEEN_YEAR = 2015;

    private static volatile MovieViewModel instance;

    private List<Movie> movieList;
    private Context context;
    private MovieDatabaseClient movieDatabaseClient;

    private List<MovieMenuRow> rows;

    private MovieViewModel(Context context) {
        this.context = context;
        movieDatabaseClient = RetrofitClientGenerator.generateClient(MovieDatabaseClient.class);

        movieList = new ArrayList<>();

        rows = staticallyGenerateDataRows();
    }

    public static MovieViewModel getInstance(Context context) {
        if (instance == null) {
            synchronized (MovieViewModel.class) {
                if (instance == null) {
                    instance = new MovieViewModel(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void getMovies() {
        if (movieDatabaseClient != null) {

            getMovieByYear(EIGHTEEN_MOVIES_ID, EIGHTEEN_YEAR);
            getMovieByYear(SEVENTEEN_MOVIES_ID, SEVENTEEN_YEAR);
            getMovieByYear(SIXTEEN_MOVIES_ID, SIXTEEN_YEAR);
            getMovieByYear(FIFTEEN_MOVIES_ID, FIFTEEN_YEAR);
        } else {
            Log.e(TAG, "MovieDatabaseClient is has not been instantiated");
        }
    }

    private void getMovieByYear(final int movieId, int movieYear) {
        movieDatabaseClient.getMovies(BuildConfig.ApiKey, movieYear)
                .subscribeOn(MovieMenu.create(context).subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "RxJava onSubscribe");
                    }

                    @Override
                    public void onNext(MovieResponse movieResponse) {
                        Log.d(TAG, "RxJava onNext");
                        bindMovieResponse(movieResponse, movieId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "RxJava onComplete");
                    }
                });
    }

    @Override
    public void onMoviePosterClicked(long id) {

    }

    private List<MovieMenuRow> staticallyGenerateDataRows() {
        rows = new ArrayList<>();

        MoviePresenter moviePresenter = new MoviePresenter(context);

        rows.add(new MovieMenuRow(
                context.getString(R.string.eighteen_title),
                EIGHTEEN_MOVIES_ID,
                new ArrayObjectAdapter(moviePresenter)));

        rows.add(new MovieMenuRow(
                context.getString(R.string.seventeen_title),
                SEVENTEEN_MOVIES_ID,
                new ArrayObjectAdapter(moviePresenter)));

        rows.add(new MovieMenuRow(
                context.getString(R.string.sixteen_title),
                SIXTEEN_MOVIES_ID,
                new ArrayObjectAdapter(moviePresenter)));

        rows.add(new MovieMenuRow(
                context.getString(R.string.fifteen_title),
                FIFTEEN_MOVIES_ID,
                new ArrayObjectAdapter(moviePresenter)));

        return rows;
    }

    /**
     * Binds a movie response to it's adapter
     * @param response
     *      The response from TMDB API
     * @param id
     *      The ID / position of the row
     */
    private void bindMovieResponse(MovieResponse response, int id) {
        MovieMenuRow row = rows.get(id);
        for(Movie movie : response.getResults()) {
            if (movie.getPosterPath() != null && !movie.getTitle().equals("Fifty Shades Freed")) { // Avoid showing movie without posters
                row.getAdapter().add(movie);
            }
        }
    }

    public List<MovieMenuRow> getRows() {
        if (CollectionUtils.isEmpty(rows)) {
            rows = staticallyGenerateDataRows();
        }
        return rows;
    }

    // TODO: 4/6/18 how do we release our RxJava instance and MovieDatabaseClient?
}
