/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.amohnacs.bitcointv.main.ui;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amohnacs.bitcointv.details.error.BrowseErrorActivity;
import com.amohnacs.bitcointv.details.ui.DetailsActivity;
import com.amohnacs.bitcointv.R;
import com.amohnacs.bitcointv.main.CardPresenter;
import com.amohnacs.bitcointv.main.GridItemPresenter;
import com.amohnacs.bitcointv.model.Movie;
import com.amohnacs.bitcointv.model.MovieList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class MainFragment extends BrowseFragment {
    private static final String TAG = "MainFragment";

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int NUM_ROWS = 6;
    private static final int NUM_COLS = 15;

    private final Handler mHandler = new Handler();
    private ArrayObjectAdapter mRowsAdapter;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundUri;
    private BackgroundManager mBackgroundManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        prepareBackgroundManager();

        setupUIElements();

        loadRows();

        setupEventListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mBackgroundTimer) {
            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
    }

    private void loadRows() {
        // TODO: 4/5/18 this is static data. this has to go
        List<Movie> list = MovieList.setupMovies();

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        CardPresenter cardPresenter = new CardPresenter();

        int i;
        for (i = 0; i < NUM_ROWS; i++) {
            if (i != 0) {
                Collections.shuffle(list);
            }
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
            for (int j = 0; j < NUM_COLS; j++) {
                listRowAdapter.add(list.get(j % 5));
            }
            HeaderItem header = new HeaderItem(i, MovieList.MOVIE_CATEGORY[i]);
            mRowsAdapter.add(new ListRow(header, listRowAdapter));
        }

        HeaderItem gridHeader = new HeaderItem(i, "PREFERENCES");

        GridItemPresenter mGridPresenter = new GridItemPresenter(getActivity());
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
        gridRowAdapter.add(getResources().getString(R.string.grid_view));
        gridRowAdapter.add(getString(R.string.error_fragment));
        gridRowAdapter.add(getResources().getString(R.string.personal_settings));
        mRowsAdapter.add(new ListRow(gridHeader, gridRowAdapter));

        setAdapter(mRowsAdapter);
    }

    /**
     * {@link BackgroundManager} supports background image continuity between multiple Activities.
     * We are attaching our current activity to the manager and settings the activity's background
     * being supplied by the BackgroundManager
     */
    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mDefaultBackground = getResources().getDrawable(R.drawable.default_background);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupUIElements() {
        //this code shows an image in teh fragment's title bar
        // setBadgeDrawable(getActivity().getResources().getDrawable(
        // R.drawable.videos_by_google_banner));
        setTitle(getString(R.string.browse_title)); // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(getResources().getColor(R.color.fastlane_background));
        // set search icon color
        setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));
    }

    private void setupEventListeners() {
        setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Implement your own in-app search", Toast.LENGTH_LONG)
                        .show();
            }
        });

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    protected void updateBackground(String uri) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;

        Glide.with(getActivity())
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

    /**
     * This onClickListener is when the specific view comes into focus, the user is about to select something
     */
    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Movie) {
                mBackgroundUri = ((Movie) item).getBackgroundImageUrl();
                startBackgroundTimer();
            }
        }
    }

    /**
     * Once focused, the user clicks again, and this listener is engaged
     */
    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            //if the adapter item we clicked on holds a movie object
            if (item instanceof Movie) {
                Movie movie = (Movie) item;
                Log.d(TAG, "Item: " + item.toString());
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, movie);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME).toBundle();

                getActivity().startActivity(intent, bundle);

            } else if (item instanceof String) {

                if (((String) item).contains(getString(R.string.error_fragment))) {

                    Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
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
