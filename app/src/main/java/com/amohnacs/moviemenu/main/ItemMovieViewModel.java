package com.amohnacs.moviemenu.main;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.amohnacs.moviemenu.model.Movie;
import com.bumptech.glide.Glide;

/**
 * Created by adrianmohnacs on 4/6/18.
 */

public class ItemMovieViewModel extends BaseObservable {
    //sample url for image http://image.tmdb.org/t/p/w1280/3kcEGnYBHDeqmdYf8ZRbKdfmlUy.jpg
    private static final String GLIDE_IMAGE_ROOT = "http://image.tmdb.org/t/p/w1280";

    private Movie movie;
    private Context context;

    public ItemMovieViewModel(Movie movie, Context context) {
        this.movie = movie;
        this.context = context;
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url){
        Glide.with(imageView.getContext()).load(GLIDE_IMAGE_ROOT + url).into(imageView);
    }

    public String getTitle() {
        return movie.getTitle();
    }

   public String getOverview() {
        return movie.getOverview();
   }

   public String getPopularity() {
        return String.valueOf((int) movie.getPopularity());
   }

   public String getBackdropUrl() {
        return movie.getBackdropPath();
   }

   public String getPosterUrl() {
        return movie.getPosterPath();
   }

    public void setMovie(Movie movie) {
        this.movie = movie;
        notifyChange();
    }

    public void onItemClick(){
        //context.startActivity(UserDetailActivity.fillDetail(v.getContext(), user));
        if (movie != null) {
            MovieViewModel.getInstance(context).onMoviePosterClicked(movie.getId());
        }
    }
}
