package com.ibbumobile;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ibbumobile.common.Utility;

public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = NewsFragment.class.getSimpleName();

    private NewsAdapter mNewsAdapter;

    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";

    private static final int NEWS_LOADER = 5;
    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] NEWS_COLUMNS = {

            DataContract.NewsEntry.COLUMN_ID,
            DataContract.NewsEntry.COLUMN_DATE,
            DataContract.NewsEntry.COLUMN_TITLE,
            DataContract.NewsEntry.COLUMN_CONTENT,
            DataContract.NewsEntry.COLUMN_PHOTO
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_NEWS_ID = 0;
    static final int COL_NEWS_DATE = 1;
    static final int COL_NEWS_TITLE = 2;
    static final int COL_NEWS_CONTENT = 3;
    static final int COL_NEWS_PHOTO = 4;


    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    public NewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mNewsAdapter = new NewsAdapter(getActivity(), null, 0);


        View view = inflater.inflate(R.layout.fragment_news, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) view.findViewById(R.id.listview_news);
        mListView.setAdapter(mNewsAdapter);
        // We'll call our MainActivity
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    ((Callback) getActivity())

                            .onItemSelected(DataContract.NewsEntry.buildNewsEntryUri(cursor.getPosition())

                            );
                }
                mPosition = position;
            }
        });


        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(NEWS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String sortOrder = DataContract.NewsEntry.COLUMN_DATE + " ASC";

        Uri newsUri = DataContract.NewsEntry.buildNewsEntryUri(
               500);

        return new CursorLoader(getActivity(),
                DataContract.NewsEntry.CONTENT_URI,
                NEWS_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mNewsAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNewsAdapter.swapCursor(null);
    }

}
