package com.amohnacs.moviemenu.main.ui;

import android.content.Context;

import com.amohnacs.moviemenu.R;
import com.amohnacs.moviemenu.base.BindableCardView;
import com.amohnacs.moviemenu.databinding.ViewMovieCardBinding;
import com.amohnacs.moviemenu.main.ItemMovieViewModel;
import com.amohnacs.moviemenu.model.Movie;

/**
 * Created by adrianmohnacs on 4/6/18.
 */

public class MovieCardView extends BindableCardView<Movie> {
    private static final String TAG = MovieCardView.class.getSimpleName();

    private ViewMovieCardBinding binding;
    private Context context;

    public MovieCardView(Context context, ViewMovieCardBinding binder) {
        super(context);
        this.binding = binder;
        this.context = context;
    }

    @Override
    public void bind(Movie data) {
        if (binding.getMovieViewModel() == null) {
            binding.setMovieViewModel(new ItemMovieViewModel(data, context));
        } else {
            binding.getMovieViewModel().setMovie(data);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.view_movie_card;
    }
}
