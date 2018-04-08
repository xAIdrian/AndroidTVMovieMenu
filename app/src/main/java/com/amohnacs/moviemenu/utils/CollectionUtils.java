package com.amohnacs.moviemenu.utils;

import java.util.Collection;

/**
 * Created by adrianmohnacs on 4/6/18.
 */

public class CollectionUtils {

    public static <T extends Collection> boolean isEmpty(T collection) {
        if (collection != null) {
            if (!collection.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
