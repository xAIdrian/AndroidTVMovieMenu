package com.amohnacs.moviemenu.main;

/**
 * Created by adrianmohnacs on 4/5/18.
 */

public interface MVVM {
    //to be implemented by our viewmodel
    interface ViewActions {
        void getMovies();
        void onMoviePosterClicked(long id);
    }

    interface DataModel {

    }

    // TODO: 4/5/18 do we need other interfaces for the ViewModel and DataModel interaction

}
