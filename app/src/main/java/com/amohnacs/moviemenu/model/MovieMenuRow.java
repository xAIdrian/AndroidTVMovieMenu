package com.amohnacs.moviemenu.model;

import android.support.v17.leanback.widget.ArrayObjectAdapter;

/**
 * Created by adrianmohnacs on 4/6/18.
 */

public class MovieMenuRow {

    private String title;
    private long id;
    private ArrayObjectAdapter adapter;

    public MovieMenuRow(String title, long id, ArrayObjectAdapter adapter) {
        this.title = title;
        this.id = id;
        this.adapter = adapter;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public ArrayObjectAdapter getAdapter() {
        return adapter;
    }
}
