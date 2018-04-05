package com.amohnacs.moviemenu.domain;

import com.amohnacs.moviemenu.model.Movie;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by adrianmohnacs on 4/5/18.
 */

public interface MovieDatabaseClient {

    @GET("discover/movie")
    Observable<List<Movie>> getMovies(
            @Query("api_key") String apiKey,
            @Query("primary_release_year") int releaseYear
    );
}
