package com.amohnacs.moviemenu.main;

import android.content.Context;

import java.util.ArrayList;
import java.util.Observable;

import android.databinding.ObservableInt;
import android.support.annotation.Nullable;
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
    public static final int EIGHTEEN_MOVIES = 0;

    private static volatile MovieViewModel instance;

    private List<Movie> movieList;
    private Context context;
    private MovieDatabaseClient movieDatabaseClient;

    public ObservableInt progressBar;
    public ObservableInt movieRecyclerView;
    private List<MovieMenuRow> rows;

    private MovieViewModel(Context context) {
        this.context = context;
        movieDatabaseClient = RetrofitClientGenerator.generateClient(MovieDatabaseClient.class);

        movieList = new ArrayList<>();
        progressBar = new ObservableInt(View.GONE);
        movieRecyclerView = new ObservableInt(View.GONE);

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
        progressBar.set(View.VISIBLE);
        movieRecyclerView.set(View.GONE);

        if (movieDatabaseClient != null) {
            movieDatabaseClient.getMovies(BuildConfig.ApiKey, 2018)
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
                            progressBar.set(View.GONE);
                            movieRecyclerView.set(View.VISIBLE);

                            bindMovieResponse(movieResponse, EIGHTEEN_MOVIES);
                            //updateMovieList(movieResponse.getResults());
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            progressBar.set(View.GONE);
                            movieRecyclerView.set(View.GONE);
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "RxJava onComplete");
                        }
                    });
        } else {
            Log.e(TAG, "MovieDatabaseClient is has not been instantiated");
        }
    }

    @Override
    public void onMoviePosterClicked(long id) {

    }

    private List<MovieMenuRow> staticallyGenerateDataRows() {
        rows = new ArrayList<>();

        MoviePresenter moviePresenter = new MoviePresenter(context);

        rows.add(new MovieMenuRow(
                context.getString(R.string.top_title),
                EIGHTEEN_MOVIES,
                new ArrayObjectAdapter(moviePresenter)));

        return rows;
    }

    /*
    private void updateMovieList(List<Movie> movies) {
        movieList.addAll(movies);
        setChanged();
        notifyObservers();
    }
    */

    /**
     * Binds a movie response to it's adapter
     * @param response
     *      The response from TMDB API
     * @param id
     *      The ID / position of the row
     */
    private void bindMovieResponse(MovieResponse response, int id) {
        MovieMenuRow row = rows.get(id);
        //row.setPage(row.getPage() + 1);
        for(Movie movie : response.getResults()) {
            if (movie.getPosterPath() != null) { // Avoid showing movie without posters
                row.getAdapter().add(movie);
                //notifying our Observer pattern that things have changed
                setChanged();
                notifyObservers();
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
