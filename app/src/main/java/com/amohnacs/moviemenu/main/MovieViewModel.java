package com.amohnacs.moviemenu.main;

import android.content.Context;

import java.util.ArrayList;
import java.util.Observable;

import android.databinding.ObservableInt;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.amohnacs.moviemenu.BuildConfig;
import com.amohnacs.moviemenu.utils.ListUtils;
import com.amohnacs.moviemenu.MovieMenu;
import com.amohnacs.moviemenu.domain.MovieDatabaseClient;
import com.amohnacs.moviemenu.domain.RetrofitClientGenerator;
import com.amohnacs.moviemenu.model.Movie;
import com.amohnacs.moviemenu.model.MovieResponse;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by adrianmohnacs on 4/5/18.
 */

public class MovieViewModel extends Observable implements MVVM.ViewActions {
    private static final String TAG = MovieViewModel.class.getSimpleName();

    private static volatile MovieViewModel instance;

    private List<Movie> movieList;
    private Context context;
    private MovieDatabaseClient movieDatabaseClient;

    public ObservableInt progressBar;
    public ObservableInt movieRecyclerView;

    private MovieViewModel(Context context) {
        this.context = context;
        movieDatabaseClient = RetrofitClientGenerator.generateClient(MovieDatabaseClient.class);

        movieList = new ArrayList<>();
        progressBar = new ObservableInt(View.GONE);
        movieRecyclerView = new ObservableInt(View.GONE);
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

                            updateMovieList(movieResponse.getResults());
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

    private void updateMovieList(List<Movie> movies) {
        movieList.addAll(movies);
        setChanged();
        notifyObservers();
    }

    @Nullable
    public List<Movie> getMovieList() {
        if (!ListUtils.isEmpty(movieList)) {
            return movieList;
        }
        return null;
    }

    // TODO: 4/6/18 how do we release our RxJava instance and MovieDatabaseClient?
}
