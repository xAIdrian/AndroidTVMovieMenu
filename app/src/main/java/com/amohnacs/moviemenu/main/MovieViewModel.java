package com.amohnacs.moviemenu.main;

import android.content.Context;
import android.support.annotation.NonNull;

import com.amohnacs.moviemenu.BuildConfig;
import com.amohnacs.moviemenu.domain.MovieDatabaseClient;
import com.amohnacs.moviemenu.domain.RetrofitClientGenerator;
import com.amohnacs.moviemenu.model.Movie;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by adrianmohnacs on 4/5/18.
 */

public class MovieViewModel {

    private static volatile MovieViewModel instance;

    private Context context;
    private MovieDatabaseClient movieDatabaseClient;

    private MovieViewModel(Context context) {
        this.context = context;
        movieDatabaseClient = RetrofitClientGenerator.generateClient(MovieDatabaseClient.class);
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

    public Observable<List<Movie>> getMoviesByYear(@NonNull Integer releaseYear) {
        return movieDatabaseClient.getMovies(BuildConfig.ApiKey, releaseYear);
    }
}
