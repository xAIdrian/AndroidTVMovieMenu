package com.amohnacs.moviemenu;

import android.app.Application;
import android.content.Context;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by adrianmohnacs on 4/6/18.
 * <p></p>
 * Maintaining a reference to our root application to help manage scheduling and threading with RxJava
 */
public class MovieMenu extends Application {

    private Scheduler scheduler;

    private static MovieMenu get(Context context) {
        return (MovieMenu) context.getApplicationContext();
    }

    public static MovieMenu create(Context context) {
        return MovieMenu.get(context);
    }

    public Scheduler subscribeScheduler() {
        if (scheduler == null) {
            scheduler = Schedulers.io();
        }

        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
