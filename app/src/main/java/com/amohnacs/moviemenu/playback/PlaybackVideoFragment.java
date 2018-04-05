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

package com.amohnacs.moviemenu.playback;

import android.os.Bundle;
import android.support.v17.leanback.app.VideoSupportFragment;
import android.support.v17.leanback.app.VideoSupportFragmentGlueHost;
import android.support.v17.leanback.media.MediaPlayerGlue;
import android.support.v17.leanback.media.PlaybackGlue;

import com.amohnacs.moviemenu.details.ui.DetailsActivity;
import com.amohnacs.moviemenu.model.Movie;

/**
 * Handles video playback with media controls.
 */
public class PlaybackVideoFragment extends VideoSupportFragment {
    private static final String TAG = "PlaybackVideoFragment";

    private MediaPlayerGlue mMediaPlayerGlue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Movie movie = (Movie) getActivity()
                .getIntent().getSerializableExtra(DetailsActivity.MOVIE);

        VideoSupportFragmentGlueHost glueHost =
                new VideoSupportFragmentGlueHost(PlaybackVideoFragment.this);

        mMediaPlayerGlue = new MediaPlayerGlue(getActivity());
        mMediaPlayerGlue.setHost(glueHost);
        mMediaPlayerGlue.setMode(MediaPlayerGlue.NO_REPEAT);
        mMediaPlayerGlue.setPlayerCallback(new PlaybackGlue.PlayerCallback() {
            @Override
            public void onReadyForPlayback() {
                mMediaPlayerGlue.play();

            }
        });
        mMediaPlayerGlue.setTitle(movie.getTitle());
        //mMediaPlayerGlue.setArtist(movie.getTagLine());
        mMediaPlayerGlue.setVideoUrl(movie.getVideoUrl());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMediaPlayerGlue != null) {
            mMediaPlayerGlue.pause();
        }
    }

    /**
     * Avoid playback during low-power mode
     */
    @Override
    public void onStop() {
        // App-specific method to stop playback
        mMediaPlayerGlue.pause();
        super.onStop();
    }
}