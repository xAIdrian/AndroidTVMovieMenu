package com.amohnacs.moviemenu.main.ui;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.amohnacs.moviemenu.R;
import com.amohnacs.moviemenu.base.BindableCardView;
import com.amohnacs.moviemenu.databinding.ViewMovieCardBinding;
import com.amohnacs.moviemenu.main.ItemMovieViewModel;
import com.amohnacs.moviemenu.model.Movie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.amohnacs.moviemenu.main.ItemMovieViewModel.GLIDE_IMAGE_ROOT;

/**
 * Created by adrianmohnacs on 4/6/18.
 */

public class MovieCardViewHolder extends BindableCardView<Movie> {
    private static final String TAG = MovieCardViewHolder.class.getSimpleName();

    @BindView(R.id.poster_iv)
    ImageView imageView;
    @BindView(R.id.popularity_tv)
    TextView textView;

    private Context context;

    public MovieCardViewHolder(Context context) {
        super(context);
        this.context = context;
        ButterKnife.bind(this);
    }

    @Override
    public void bind(Movie data) {
        // TODO: 4/7/18 replace with databinding
        Glide.with(getContext())
                .load(GLIDE_IMAGE_ROOT + data.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        textView.setText(String.valueOf((int) data.getPopularity()));
    }

    public ImageView getImageView() {
        return imageView;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.view_movie_card;
    }
}
