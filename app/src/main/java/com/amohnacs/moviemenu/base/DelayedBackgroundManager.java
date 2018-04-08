package com.amohnacs.moviemenu.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.util.DisplayMetrics;

import com.amohnacs.moviemenu.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by adrianmohnacs on 4/6/18.
 * <p></p>
 * {@link BackgroundManager} supports background image continuity between multiple Activities.
 * We are attaching our current activity to the manager and settings the activity's background
 * being supplied by the BackgroundManager
 */
public class DelayedBackgroundManager {
    private static final String TAG = DelayedBackgroundManager.class.getSimpleName();
    private static final int BACKGROUND_UPDATE_DELAY = 300;

    private final Handler mHandler = new Handler();

    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundUri;
    private BackgroundManager mBackgroundManager;

    private static volatile DelayedBackgroundManager instance;

    private Context context;

    public static DelayedBackgroundManager getInstance(Activity activity) {
        if (instance == null) {
            synchronized (DelayedBackgroundManager.class) {
                if (instance == null) {
                    instance = new DelayedBackgroundManager(activity);
                }
            }
        }
        return instance;
    }

    private DelayedBackgroundManager(Activity activity) {
        this.context = activity;

        mBackgroundManager = BackgroundManager.getInstance(activity);
        mBackgroundManager.attach(activity.getWindow());
        mDefaultBackground = activity.getResources().getDrawable(R.drawable.default_background);
        mMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void updateBackground(String uri) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;

        Glide.with(context)
                .load(uri)
                .centerCrop()
                .error(mDefaultBackground)
                .into(new SimpleTarget<GlideDrawable>(width, height) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable>
                                                        glideAnimation) {
                        mBackgroundManager.setDrawable(resource);
                    }
                });
        mBackgroundTimer.cancel();
    }

    private void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    public Timer getBackgroundTimer() {
        return mBackgroundTimer;
    }

    private void setBackgroundUri(String backgroundUri) {
        this.mBackgroundUri = backgroundUri;
    }

    public void updateBackgroundWithDelay(String url) {
        setBackgroundUri(url);
        startBackgroundTimer();
    }

    private class UpdateBackgroundTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateBackground(mBackgroundUri);
                }
            });
        }
    }
}
