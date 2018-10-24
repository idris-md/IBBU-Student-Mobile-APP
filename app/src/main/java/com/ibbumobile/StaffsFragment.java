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

public class StaffsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = NewsFragment.class.getSimpleName();

    private StaffsAdapter mStaffAdapter;

    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";

    private static final int STUDENTS_LOADER = 5;
    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] STAFFS_COLUMNS = {

            DataContract.StaffsEntry.COLUMN_ID,
            DataContract.StaffsEntry.COLUMN_FIRST_NAME,
            DataContract.StaffsEntry.COLUMN_SURNAME,
            DataContract.StaffsEntry.COLUMN_STAFF_ID,
            DataContract.StaffsEntry.COLUMN_EMAIL,
            DataContract.StaffsEntry.COLUMN_DEPARTMENT,
            DataContract.StaffsEntry.COLUMN_STAFF_TYPE

    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_STAFF_ID = 0;
    static final int COL_STAFF_FIRST_NAME = 1;
    static final int COL_STAFF_SURNAME = 2;
    static final int COL_STAFF_STAFF_ID = 3;
    static final int COL_STAFF_EMAIL = 4;
    static final int COL_STAFF_DEPARTMENT = 5;
    static final int COL_STAFF_STAFF_TYPE = 6;

    public StaffsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mStaffAdapter = new StaffsAdapter(getActivity(), null, 0);


        View view = inflater.inflate(R.layout.fragment_staff, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) view.findViewById(R.id.list_view);
        mListView.setAdapter(mStaffAdapter);
        // We'll call our MainActivity
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                // CursorAdapter returns a cursor at the correct position for getItem(), or null
//                // if it cannot seek to that position.
//                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
//                if (cursor != null) {
//                    ((NewsFragment.Callback) getActivity())
//
//                            .onItemSelected(DataContract.NewsEntry.buildNewsEntryUri(cursor.getPosition())
//
//                            );
//                }
//                mPosition = position;
//            }
//        });


        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(STUDENTS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortOrder = DataContract.StaffsEntry.COLUMN_STAFF_TYPE + " ASC";

        Uri studentUri = DataContract.NewsEntry.buildNewsEntryUri(
                500);

        return new CursorLoader(getActivity(),
                DataContract.StaffsEntry.CONTENT_URI,
                STAFFS_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mStaffAdapter.swapCursor(cursor);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mStaffAdapter.swapCursor(null);

    }


    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }


}
