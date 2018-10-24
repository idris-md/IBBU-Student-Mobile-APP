package com.ibbumobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PeopleActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

//        if (findViewById(R.id.fragment_container) != null) {
//
//            // However, if we're being restored from a previous state,
//            // then we don't need to do anything and should return or else
//            // we could end up with overlapping fragments.
//            if (savedInstanceState != null) {
//                return;
//            }
//
//            // Create a new Fragment to be placed in the activity layout
//            PeopleFragment firstFragment = new PeopleFragment();
//
//            // In case this activity was started with special instructions from an
//            // Intent, pass the Intent's extras to the fragment as arguments
//            firstFragment.setArguments(getIntent().getExtras());
//
//            // Add the fragment to the 'fragment_container' FrameLayout
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment_container, firstFragment).commit();
//      }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        EditText search = findViewById(R.id.search);
        TextView bookmark = findViewById(R.id.bookmark);
        TextView staffDir = findViewById(R.id.staffDir);
        TextView studentDir = findViewById(R.id.studentDir);

        search.setOnClickListener(this);
        bookmark.setOnClickListener(this);
        staffDir.setOnClickListener(this);
        studentDir.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.search:

                break;
            case R.id.bookmark:
                showBookmark();
                break;
            case R.id.staffDir:
                showStafDir();
                break;
            case R.id.studentDir:
                showStudentsDir();
                break;


        }
    }

    private void showStudentsDir() {

        Intent intent = new Intent(this, StudentsActivity.class);
        startActivity(intent);

    }

    private void showStafDir() {
        Intent intent = new Intent(this, StaffsDirActivity.class);
        startActivity(intent);
    }

    private void showBookmark() {

        Intent intent = new Intent(this, BookmarkActivity.class);
        startActivity(intent);

    }
}
