package com.amohnacs.bitcointv.main;

import android.content.Context;
import android.graphics.Color;
import android.support.v17.leanback.widget.Presenter;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amohnacs.bitcointv.R;

/**
 * Created by adrianmohnacs on 4/5/18.
 */

public class GridItemPresenter extends Presenter {
    private static final String TAG = GridItemPresenter.class.getSimpleName();

    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;

    private Context context;

    public GridItemPresenter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        TextView view = new TextView(parent.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setBackgroundColor(context.getResources().getColor(R.color.default_background));
        view.setTextColor(Color.WHITE);
        view.setGravity(Gravity.CENTER);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ((TextView) viewHolder.view).setText((String) item);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }
}
