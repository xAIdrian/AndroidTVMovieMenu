package com.amohnacs.moviemenu.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.amohnacs.moviemenu.R;
import com.amohnacs.moviemenu.databinding.ViewMovieCardBinding;
import com.amohnacs.moviemenu.main.ui.MovieCardViewHolder;
import com.amohnacs.moviemenu.model.Movie;

import java.util.Collections;
import java.util.List;

/**
 * Created by adrianmohnacs on 4/6/18.
 */

public class MoviePresenter extends Presenter {
    private static final String TAG = MoviePresenter.class.getSimpleName();

    private List<Movie> movieList;
    private Context context;

    public MoviePresenter(Context context) {
        this.context = context;
        this.movieList = Collections.emptyList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ViewMovieCardBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.view_movie_card, parent,false);
        return new ViewHolder(new MovieCardViewHolder(context, binding));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        if (item instanceof Movie) {
            MovieCardViewHolder movieCardViewHolder = ((MovieCardViewHolder) viewHolder.view);
            movieCardViewHolder.bind((Movie) item);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        Log.d(TAG, "onUnbindViewHolder");
        //todo :: Remove references to images so that the garbage collector can free up memory
    }


}
