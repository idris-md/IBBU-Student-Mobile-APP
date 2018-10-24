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

public class NoticeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = NoticeFragment.class.getSimpleName();
    private NoticeAdapter mNoticeAdapter;

    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";

    private static final int NOTICE_LOADER = 5;
    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] NOTICE_COLUMNS = {

            DataContract.NoticeEntry.COLUMN_ID,
            DataContract.NoticeEntry.COLUMN_DATE,
            DataContract.NoticeEntry.COLUMN_AUTHOR,
            DataContract.NoticeEntry.COLUMN_CONTENT
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_NOTICE_ID = 0;
    static final int COL_NOTICE_DATE = 1;
    static final int COL_NOTICE_AUTHOR = 2;
    static final int COL_NOTICE_CONTENT = 3;


    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    public NoticeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mNoticeAdapter = new NoticeAdapter(getActivity(), null, 0);

        View view = inflater.inflate(R.layout.fragment_notice, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = view.findViewById(R.id.listview_notice);
        mListView.setAdapter(mNoticeAdapter);
        // We'll call our MainActivity
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                // CursorAdapter returns a cursor at the correct position for getItem(), or null
//                // if it cannot seek to that position.
//                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
//                if (cursor != null) {
//                    ((Callback) getActivity())
//                            .onItemSelected(DataContract.NoticeEntry.buildNoticeEntryUri(cursor.getPosition())
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
        getLoaderManager().initLoader(NOTICE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String sortOrder = DataContract.NoticeEntry.COLUMN_DATE + " ASC";

        String locationSetting = Utility.getPreferredLocation(getActivity());
        Uri noticeUri = DataContract.NoticeEntry.buildNoticeEntryUri(
               700);

        return new CursorLoader(getActivity(),
                DataContract.NoticeEntry.CONTENT_URI,
                NOTICE_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mNoticeAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mNoticeAdapter.swapCursor(null);

    }
}
