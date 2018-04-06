package com.amohnacs.moviemenu.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.amohnacs.moviemenu.R;
import com.amohnacs.moviemenu.databinding.ViewMovieCardBinding;
import com.amohnacs.moviemenu.main.ui.MovieCardView;
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
        ViewMovieCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_movie_card ,parent, false);
        return new ViewHolder(new MovieCardView(context, binding));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        //((MovieCardView) viewHolder.view).bind((Movie) item);
        if (item instanceof Movie) {
            ((MovieCardView) viewHolder.view).bind((Movie) item);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        Log.d(TAG, "onUnbindViewHolder");
        MovieCardView cardView = ((MovieCardView) viewHolder.view);
        //todo :: Remove references to images so that the garbage collector can free up memory
    }
}
