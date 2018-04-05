package com.amohnacs.moviemenu;

import android.content.Context;

/**
 * Created by adrianmohnacs on 4/5/18.
 */

public class ViewUtils {

    public static int convertDpToPixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
