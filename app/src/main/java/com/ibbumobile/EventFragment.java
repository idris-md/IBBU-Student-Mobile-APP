package com.ibbumobile;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class EventFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = NewsFragment.class.getSimpleName();

    private NewsAdapter mEventAdapter;

    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";

    private static final int EVENT_LOADER = 5;
    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] EVENT_COLUMNS = {

            DataContract.EventEntry.COLUMN_ID,
            DataContract.EventEntry.COLUMN_TITLE,
            DataContract.EventEntry.COLUMN_DESCRIPTION,
            DataContract.EventEntry.COLUMN_VENUE,
            DataContract.EventEntry.COLUMN_START_DATE,
            DataContract.EventEntry.COLUMN_CLOSE_DATE

    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_EVENT_ID = 0;
    static final int COL_EVENT_TITLE = 1;
    static final int COL_EVENT_DESC= 2;
    static final int COL_EVENT_VENUE = 3;
    static final int COL_EVENT_START_DATE = 4;
    static final int COL_EVENT_CLOSE_DATE = 4;



    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    public EventFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) view.findViewById(R.id.listview_event);
        mListView.setAdapter(mEventAdapter);
        // We'll call our MainActivity
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    ((EventFragment.Callback) getActivity())
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
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String sortOrder = DataContract.NewsEntry.COLUMN_DATE + " ASC";

        String locationSetting = Utility.getPreferredLocation(getActivity());
        Uri weatherForLocationUri = DataContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting, System.currentTimeMillis());

        return new CursorLoader(getActivity(),
                weatherForLocationUri,
                EVENT_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mEventAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mEventAdapter.swapCursor(null);
    }

}
