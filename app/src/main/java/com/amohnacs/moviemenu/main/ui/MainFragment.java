package com.amohnacs.moviemenu.main.ui;

import java.util.List;
import java.util.Timer;

import android.content.Intent;
import android.os.Bundle;
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
import android.util.Log;
import android.widget.Toast;

import com.amohnacs.moviemenu.R;
import com.amohnacs.moviemenu.base.TimedBackgroundManager;
import com.amohnacs.moviemenu.error.BrowseErrorActivity;
import com.amohnacs.moviemenu.detailsref.ui.DetailsActivity;
import com.amohnacs.moviemenu.main.MovieViewModel;
import com.amohnacs.moviemenu.model.Movie;
import com.amohnacs.moviemenu.model.MovieMenuRow;
import com.amohnacs.moviemenu.utils.CollectionUtils;

public class MainFragment extends BrowseFragment {
    private static final String TAG = MainFragment.class.getSimpleName();

    private ArrayObjectAdapter rowsAdapter;
    private TimedBackgroundManager backgroundImageManager;
    private MovieViewModel viewModel;
    private List<MovieMenuRow> rows;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        viewModel = MovieViewModel.getInstance(getActivity());
        backgroundImageManager = new TimedBackgroundManager(getActivity());

        setupUIElements();
        // TODO: 4/6/18 indeterminate loader
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timer mBackgroundTimer = backgroundImageManager.getBackgroundTimer();
        if (null != mBackgroundTimer) {
            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
    }

    private void setupUIElements() {
        //this code shows an image in teh fragment's title bar
        // TODO: 4/7/18 this image has gross white corners that aren't cut.  We need to change this or change the background to white
        setBadgeDrawable(getActivity().getResources().getDrawable(R.drawable.movie_banner));
        setTitle(getString(R.string.browse_title)); // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(getResources().getColor(R.color.fastlane_background));
        // set search icon color
        setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));

        createDataRows();
        viewModel.getMovies();
    }

    /**
     * Creates the RowsAdapter for the Fragment
     * The ListRowPresenter tells to render ListRow objects
     * <p></p>
     * Adds a new {@link MovieMenuRow} to the adapter. Each row will contain a collection of Movies
     * that will be rendered using the MoviePresenter.
     *
     */
    private void createDataRows() {
        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        rows = viewModel.getRows();

        if (!CollectionUtils.isEmpty(rows)) {
            for (int i = 0; i < rows.size(); i++) {
                MovieMenuRow row = rows.get(i);

                HeaderItem headerItem = new HeaderItem(row.getId(), row.getTitle());
                ListRow listRow = new ListRow(headerItem, row.getAdapter());

                rowsAdapter.add(listRow);
            }
        }

        setAdapter(rowsAdapter);
    }

    /**
     * This onClickListener is when the specific view comes into focus, the user is about to select something
     */
    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Movie) {
                backgroundImageManager.setBackgroundUri(((Movie) item).getBackdropPath());
                backgroundImageManager.startBackgroundTimer();
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
}
