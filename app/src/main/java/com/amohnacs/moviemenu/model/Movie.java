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

package com.amohnacs.moviemenu.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Movie class represents video entity with title, tagLine, image thumbs and video url.
 */
public class Movie implements Serializable {

    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("poster_path")
    private String posterPath;
    private String videoUrl;
    @SerializedName("overview")
    private String overview;
    @SerializedName("popularity")
    private double popularity;

    public Movie() {
    }

    public Movie(long id, String title, String backdropPath, String posterPath, String videoUrl, String overview, double popularity) {
        this.id = id;
        this.title = title;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.videoUrl = videoUrl;
        this.overview = overview;
        this.popularity = popularity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }
}
